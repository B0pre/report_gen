package org.bopre.support.generator.core.yaml

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class YamlUtilsKtTest {

    @Test
    fun isVariableParameter() {
        org.junit.jupiter.api.assertAll(
            { assertEquals(true, isVariableParameter("\${param.value}"), "\${param.value}") },
            { assertEquals(true, isVariableParameter("\${pa\${name}ram}"), "\${pa\${name}ram}") },
            { assertEquals(true, isVariableParameter("\${param}"), "\${param}") },
            { assertEquals(false, isVariableParameter("\${param"), "\${param") },
            { assertEquals(false, isVariableParameter("param}"), "param}") },
            { assertEquals(false, isVariableParameter("pa\${one}ram"), "pa\${one}ram") },
            { assertEquals(false, isVariableParameter("param"), "param") }
        )
    }

    @Test
    fun removeSymbolsTest() {
        assertEquals("parameter.name", removeSymbols("\${parameter.name}"))
    }

}