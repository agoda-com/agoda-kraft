package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall
import org.jetbrains.kotlin.types.typeUtil.isNothing
import org.jetbrains.kotlin.types.typeUtil.isUnit

class IgnoredReturnValueRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "This rule reports when a function call's return value is ignored.",
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
            when {
                parent is KtValueArgument -> return
                parent is KtProperty -> return
                parent is KtReturnExpression -> return
                parent is KtBinaryExpression && parent.operationToken.toString() == "EQ" -> return
                parent is KtIfExpression && expression == parent.condition -> return
                parent is KtWhenConditionWithExpression -> return
                parent is KtBinaryExpression && parent.operationToken.toString() in setOf("GT", "LT", "GTEQ", "LTEQ", "EQEQ", "EXCLEQ") -> return
                parent is KtDotQualifiedExpression -> return
                parent is KtBlockExpression -> {
                    val isLastStatement = parent.statements.lastOrNull() == expression
                    val isOnlyStatement = parent.statements.size == 1
                    if (!isLastStatement && !isOnlyStatement) {
                        report(CodeSmell(
                            issue,
                            Entity.from(expression),
                            "The return value of this function call is ignored."
                        ))
                    }
                }
                else -> {
                    report(CodeSmell(
                        issue,
                        Entity.from(expression),
                        "The return value of this function call is ignored."
                    ))
                }
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
}