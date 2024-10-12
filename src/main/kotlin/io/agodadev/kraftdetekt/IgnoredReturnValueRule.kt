package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.resolve.bindingContextUtil.isUsedAsExpression
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.isNothing
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.isUnit

class IgnoredReturnValueRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Warning,
        "This rule reports when a function call's return value is ignored.",
        Debt.FIVE_MINS
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        val parent = expression.parent
        if (parent is KtExpression && !parent.isUsedAsExpression(bindingContext)) {
            val resolvedCall = expression.getResolvedCall(bindingContext) ?: return
            val returnType = resolvedCall.resultingDescriptor.returnType ?: return

            if (!returnType.isUnit() && !returnType.isNothing()) {
                report(CodeSmell(
                    issue,
                    Entity.from(expression),
                    "The return value of this function call is ignored."
                ))
            }
        }
    }
}

class IgnoredReturnValueRuleProvider : RuleSetProvider {
    override val ruleSetId: String = "custom-rules"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(IgnoredReturnValueRule(config))
        )
    }
}