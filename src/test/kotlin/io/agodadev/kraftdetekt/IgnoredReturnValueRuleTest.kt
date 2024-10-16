package io.agodadev.kraftdetekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.github.detekt.test.utils.KotlinCoreEnvironmentWrapper
import io.github.detekt.test.utils.createEnvironment
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
class IgnoredReturnValueRuleTest(private val env: KotlinCoreEnvironment) {

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

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLintWithContext(env, code)

        println("Number of findings: ${findings.size}")
        findings.forEachIndexed { index, finding ->
            println("Finding $index:")
            println("  Message: ${finding.message}")
            println("  Location: ${finding.entity.location}")
            println("  Signature: ${finding.entity.signature}")
        }

        assertThat(findings).hasSize(2)
        assertThat(findings.map { it.entity.signature }).containsExactlyInAnyOrder(
            "Test.kt\$returnsString()",
            "Test.kt\$returnsInt()"
        )
        findings.forEach { finding ->
            assertThat(finding.message).isEqualTo(IGNORED_RETURN_VALUE)
        }
    }

    @Test
    fun `does not report when return value is explicitly ignored with underscore`() {
        val code = """
            fun returnsString(): String = "Hello"
            fun returnsInt(): Int = 42
            
            fun test() {
                _ = returnsString()
                _ = returnsInt()
            }
        """.trimIndent()

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLintWithContext(env, code)

        assertThat(findings).isEmpty()
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

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLintWithContext(env, code)

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

        val findings = IgnoredReturnValueRule(Config.empty).compileAndLintWithContext(env, code)

        println("Number of findings: ${findings.size}")
        findings.forEachIndexed { index, finding ->
            println("Finding $index:")
            println("  Message: ${finding.message}")
            println("  Location: ${finding.entity.location}")
            println("  Signature: ${finding.entity.signature}")
        }

        assertThat(findings).isEmpty()
    }
}