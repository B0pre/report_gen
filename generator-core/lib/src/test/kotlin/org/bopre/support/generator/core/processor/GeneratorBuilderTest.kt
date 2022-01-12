package org.bopre.support.generator.core.processor

import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.impl.SimpleSeparatorContent
import org.bopre.support.generator.core.processor.content.impl.SimpleSheet
import org.bopre.support.generator.core.processor.content.impl.SimpleTableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleTableContent
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.PoiDocumentRenderer
import org.bopre.support.generator.core.processor.render.PoiDocumentRendererBuilder
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion
import org.bopre.support.generator.core.testutils.xls.GenericCell
import org.bopre.support.generator.core.testutils.xls.assertCellStyles
import org.bopre.support.generator.core.testutils.xls.assertSheetInFile
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class GeneratorBuilderTest {

    @Test
    fun `test style font height`() {

        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("column" to "value"))
            )
        )

        val columns = listOf(
            SimpleTableColumn(title = "column", id = "column", style = CellSettings.create(height = 18))
        )

        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)

        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource)
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

        assertTrue(file.exists(), "file was not created")
        assertCellStyles(
            file, 0, listOf(
                GenericCell(0, 0, CellStyleAssertion.CellFontHeightAssertion(11)),
                GenericCell(1, 0, CellStyleAssertion.CellFontHeightAssertion(18))
            )
        )
    }

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
            SimpleSeparatorContent(linesToSkip),
            SimpleTableContent(columns, sourceId)
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)

        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource)
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

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

}