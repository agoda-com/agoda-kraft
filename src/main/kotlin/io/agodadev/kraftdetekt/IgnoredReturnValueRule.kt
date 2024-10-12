package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall
import org.jetbrains.kotlin.resolve.BindingContext
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

        println("Visiting call expression: ${expression.text}")

        val resolvedCall = expression.getResolvedCall(bindingContext)
        if (resolvedCall == null) {
            println("  Resolved call is null")
            return
        }

        val returnType = resolvedCall.resultingDescriptor.returnType
        if (returnType == null) {
            println("  Return type is null")
            return
        }

        println("  Return type: $returnType")
        println("  Is Unit: ${returnType.isUnit()}")
        println("  Is Nothing: ${returnType.isNothing()}")

        if (!returnType.isUnit() && !returnType.isNothing()) {
            val parent = expression.parent
            println("  Parent: ${parent?.javaClass?.simpleName}")

            when {
                parent is KtValueArgument -> println("  Parent is KtValueArgument")
                parent is KtProperty -> println("  Parent is KtProperty")
                parent is KtReturnExpression -> println("  Parent is KtReturnExpression")
                parent is KtBinaryExpression && parent.operationToken.toString() == "EQ" -> println("  Parent is assignment")
                parent is KtIfExpression && expression == parent.condition -> println("  Parent is if condition")
                parent is KtWhenConditionWithExpression -> println("  Parent is when condition")
                parent is KtBinaryExpression && parent.operationToken.toString() in setOf("GT", "LT", "GTEQ", "LTEQ", "EQEQ", "EXCLEQ") -> println("  Parent is comparison")
                parent is KtDotQualifiedExpression -> println("  Parent is dot qualified expression")
                parent is KtBlockExpression -> {
                    println("  Parent is block expression")
                    if (parent.statements.lastOrNull() != expression) {
                        println("  Reporting issue: ignored return value in block")
                        report(CodeSmell(
                            issue,
                            Entity.from(expression),
                            "The return value of this function call is ignored."
                        ))
                    }
                }
                else -> {
                    println("  Reporting issue: general case")
                    report(CodeSmell(
                        issue,
                        Entity.from(expression),
                        "The return value of this function call is ignored."
                    ))
                }
            }
        } else {
            println("  Not reporting: return type is Unit or Nothing")
        }
    }
}