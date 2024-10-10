package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.Debt
import org.jetbrains.kotlin.psi.KtCallExpression

class CustomDetektRule(config: Config) : Rule(config) {
    override val issue = Issue(
        id = "CustomRule",
        severity = Severity.Warning,
        description = "Custom rule description",
        debt = Debt.FIVE_MINS
    )

    // ... (rest of the implementation)
}
