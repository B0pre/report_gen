package org.bopre.support.generator.core.processor

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.bopre.support.generator.core.processor.content.*
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.PoiDocumentRenderer
import org.bopre.support.generator.core.processor.render.PoiDocumentRendererBuilder
import java.io.File
import java.io.FileInputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorBuilderTest {

    @Test
    fun test() {

        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val someSource = LineSource.static(
                    listOf(
                        Line.fromMap(mapOf("id" to "01", "user" to "user_01")),
                        Line.fromMap(mapOf("id" to "02", "user" to "user_02")),
                        Line.fromMap(mapOf("id" to "03", "user" to "user_03"))
                    )
                )

        val columns = listOf(
            SimpleTableColumn(title = "identifier", id = "id"),
            SimpleTableColumn(title = "username", id = "user")
        )

        val linesToSkip = 2
        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
            SeparatorContent(linesToSkip),
            SimpleTableContent(columns, sourceId)
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)

        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource)
            .build()

        renderer.renderToFile(file, RenderProperties.empty())

        assertTrue(file.exists(), "file was not created")
        assertSheetInFile(
            file, 0, arrayOf(
                arrayOf("identifier", "username"),
                arrayOf("01", "user_01"),
                arrayOf("02", "user_02"),
                arrayOf("03", "user_03"),

                arrayOf("", ""),
                arrayOf("", ""),

                arrayOf("identifier", "username"),
                arrayOf("01", "user_01"),
                arrayOf("02", "user_02"),
                arrayOf("03", "user_03")
            )
        )
    }

    fun assertSheetInFile(file: File, sheetId: Int, expectedSheetValues: Array<Array<String>>) {
        val inputStream = FileInputStream(file)
        //Instantiate Excel workbook using existing file:
        var xlWb = WorkbookFactory.create(inputStream)

        for (x in expectedSheetValues.indices) {
            for (y in expectedSheetValues[x].indices) {
                val xlWs = xlWb.getSheetAt(sheetId)
                val expected = expectedSheetValues[x][y]
                val actual = xlWs.getRow(x)
                    .getCell(y, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                    .toString()
                assertEquals(expected, actual, "wrong value at [$x:$y]")
            }
        }
    }
}