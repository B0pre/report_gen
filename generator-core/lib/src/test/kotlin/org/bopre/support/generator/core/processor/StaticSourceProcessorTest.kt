package org.bopre.support.generator.core.processor

import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.junit.Test
import kotlin.test.assertEquals

class StaticSourceProcessorTest {

    @Test
    fun `test source iteration`() {

        val source: LineSource = LineSource.static(
            listOf(
                Line.fromMap(
                    mapOf(
                        "col00" to "val00"
                    )
                ),
                Line.fromMap(
                    mapOf(
                        "col10" to "val10"
                    )
                )
            )
        )

        val expectedLines = 2;
        var actualLines = 0;

        for (line: Line in source.start()) {
            val value = line.getCell("col${actualLines}0")
            assertEquals("val${actualLines}0", value, "wrong value for line $actualLines")
            actualLines += 1;
        }
        assertEquals(expectedLines, actualLines, "wrong lines count")
    }

}