package io.agodadev.`kraft-ktlint`

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.ElementType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode

class UnusedReturnValueRule : Rule("unused-return-value") {
    // ... (same implementation as before)
}

// src/main/kotlin/io/agodadev/kraft-detekt/CustomDetektRule.kt
package io.agodadev.`kraft-detekt`

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtCallExpression

class CustomDetektRule(config: Config) : Rule(config) {
    // ... (same implementation as before)
}