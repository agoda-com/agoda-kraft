package io.agodadev.kraftdetekt
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLint
import io.gitlab.arturbosch.detekt.test.TestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IgnoredReturnValueRuleTest {

    @Test
    fun `reports ignored return values`() {
        val code = """
            fun returnsString(): String = "Hello"
            fun returnsInt(): Int = 42
            fun returnsNothing(): Nothing = throw Exception("Nothing")
            fun returnsUnit(): Unit {}
            
            fun test() {
                returnsString()
                returnsInt()
                returnsNothing()
                returnsUnit()
                val x = returnsString()
                if (returnsInt() > 0) {}
            }
        """.trimIndent()

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLint(code)

        assertThat(findings).hasSize(2)
        assertThat(findings[0].message).isEqualTo("The return value of this function call is ignored.")
        assertThat(findings[1].message).isEqualTo("The return value of this function call is ignored.")
    }

    @Test
    fun `does not report when return value is used`() {
        val code = """
            fun returnsString(): String = "Hello"
            fun returnsInt(): Int = 42
            
            fun test() {
                val s = returnsString()
                println(returnsInt())
                if (returnsString().isNotEmpty()) {}
            }
        """.trimIndent()

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLint(code)

        assertThat(findings).isEmpty()
    }

    @Test
    fun `does not report Unit and Nothing return types`() {
        val code = """
            fun returnsNothing(): Nothing = throw Exception("Nothing")
            fun returnsUnit(): Unit {}
            
            fun test() {
                returnsNothing()
                returnsUnit()
            }
        """.trimIndent()

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLint(code)

        assertThat(findings).isEmpty()
    }
}