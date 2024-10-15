package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall
import org.jetbrains.kotlin.types.typeUtil.isNothing
import org.jetbrains.kotlin.types.typeUtil.isUnit

const val IGNORED_RETURN_VALUE: String = "AG001: The return value of this function call is ignored. see https://github.com/agoda-com/agoda-kraft/blob/main/doc/AG001.md"

class IgnoredReturnValueRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "AG001: This rule reports when a function call's return value is ignored. see https://github.com/agoda-com/agoda-kraft/blob/main/doc/AG001.md",
        Debt.FIVE_MINS
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (expression.calleeExpression is KtConstructorCalleeExpression || isWithinThrowExpression(expression)) {
            return
        }

        val resolvedCall = expression.getResolvedCall(bindingContext) ?: return
        val returnType = resolvedCall.resultingDescriptor.returnType ?: return

        if (!returnType.isUnit() && !returnType.isNothing()) {
            val parent = expression.parent
            val ignoredExpressions = setOf(
                KtValueArgument::class,
                KtProperty::class,
                KtReturnExpression::class,
                KtDotQualifiedExpression::class,
                KtWhenConditionWithExpression::class
            )
            when {
                parent::class in ignoredExpressions -> return
                parent is KtBinaryExpression && parent.operationToken.toString() in setOf("EQ", "GT", "LT", "GTEQ", "LTEQ", "EQEQ", "EXCLEQ") -> return
                parent is KtIfExpression && expression == parent.condition -> return
                parent is KtBlockExpression -> {
                    val isLastStatement = parent.statements.lastOrNull() == expression
                    val isOnlyStatement = parent.statements.size == 1
                    if (!isLastStatement && !isOnlyStatement) {
                        reportIgnoredReturnValue(expression)
                    }
                }
                else -> reportIgnoredReturnValue(expression)
            }
        }
    }

    private fun isWithinThrowExpression(expression: KtExpression): Boolean {
        var current: PsiElement? = expression
        while (current != null && current !is KtFunction) {
            if (current is KtThrowExpression) {
                return true
            }
            current = current.parent
        }
        return false
    }

    private fun reportIgnoredReturnValue(expression: KtExpression) {
        report(CodeSmell(
            issue,
            Entity.from(expression),
            IGNORED_RETURN_VALUE
        ))
    }
}