package io.agodadev.kraftktlint

import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertNotNull

class UnusedReturnValueRuleTest {

    @Test
    fun test1() {
        val a = UnusedReturnValueRule()
        assertTrue(true)
        assertNotNull(a)
    }
}
