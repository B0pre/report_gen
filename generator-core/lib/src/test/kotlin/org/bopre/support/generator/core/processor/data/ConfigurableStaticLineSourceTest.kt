package org.bopre.support.generator.core.processor.data

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ConfigurableStaticLineSourceTest {

    @Test
    fun start() {

        val source = ConfigurableStaticLineSource.fromLines(
            lines = listOf(
                mapOf(
                    "field0" to ConfigurableStaticLineSource.dynamicField("cell_0_0"),
                    "field1" to ConfigurableStaticLineSource.constField("cell_0_1_value_static")
                ),
                mapOf(
                    "field0" to ConfigurableStaticLineSource.dynamicField("cell_1_0"),
                    "field1" to ConfigurableStaticLineSource.dynamicField("cell_1_1")
                )
            )
        )

        val props = RenderProperties.of(
            mapOf(
                "cell_0_0" to "cell_0_0_value",
                "cell_1_0" to "cell_1_0_value",
                "cell_1_1" to "cell_1_1_value"
            )
        )
        val generatedLines = source.start(props).toList()

        assertEquals("cell_0_0_value", generatedLines[0].getCell("field0"), "wrong field0 for line 0");
        assertEquals("cell_0_1_value_static", generatedLines[0].getCell("field1"), "wrong field1 for line 0");
        assertEquals("cell_1_0_value", generatedLines[1].getCell("field0"), "wrong field0 for line 1");
        assertEquals("cell_1_1_value", generatedLines[1].getCell("field1"), "wrong field1 for line 1");
    }

}