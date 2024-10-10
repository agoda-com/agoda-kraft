package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtCallExpression

class CustomDetektRule(config: Config, override val issue: Issue) : Rule(config) {
    // ... (same implementation as before)
}