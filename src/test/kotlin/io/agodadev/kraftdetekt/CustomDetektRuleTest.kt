package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.test.TestConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CustomDetektRuleTest {

    @Test
    fun test1() {
        val config = TestConfig()
        val rule = CustomDetektRule(config)
        Assertions.assertTrue(rule.issue.severity == io.gitlab.arturbosch.detekt.api.Severity.Warning)
    }

}