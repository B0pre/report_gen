package org.bopre.support.generator.core.processor

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFFont
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.impl.*
import org.bopre.support.generator.core.processor.content.style.CellBorders
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.PoiDocumentRenderer
import org.bopre.support.generator.core.processor.render.PoiDocumentRendererBuilder
import org.bopre.support.generator.core.testutils.xls.*
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.*
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.CellBordersAssertion.BorderLocation
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.CellFontSettingsAssertion.AssertFontType
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertTrue

class GeneratorBuilderTest {

    companion object {
        @JvmStatic
        fun styleTestCases(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    Named.of(
                        "data format text",
                        listOf(
                            CellSettings.create(
                                dataFormat = "# ##0.00"
                            )
                        )
                    ),
                    listOf(
                        GenericCell(0, 0, CellDataFormatAssertion("General")),
                        GenericCell(1, 0, CellDataFormatAssertion("# ##0.00"))
                    )
                ),
                Arguments.of(
                    Named.of(
                        "wrap text",
                        listOf(
                            CellSettings.create(
                                isWrapped = true
                            )
                        )
                    ),
                    listOf(
                        GenericCell(0, 0, CellStyleAssertion.CellIsWrappedAssertion(false)),
                        GenericCell(1, 0, CellStyleAssertion.CellIsWrappedAssertion(true))
                    )
                ),
                Arguments.of(
                    Named.of(
                        "alignment text",
                        listOf(
                            CellSettings.create(
                                verticalAlignment = VerticalAlignment.CENTER
                            ),
                            CellSettings.create(
                                horizontalAlignment = HorizontalAlignment.CENTER
                            )
                        )
                    ),
                    listOf(
                        GenericCell(0, 0, CellVerticalAlignmentAssertion(VerticalAlignment.BOTTOM)),
                        GenericCell(0, 0, CellHorizontalAlignmentAssertion(HorizontalAlignment.GENERAL)),
                        GenericCell(1, 0, CellVerticalAlignmentAssertion(VerticalAlignment.CENTER)),
                        GenericCell(1, 1, CellHorizontalAlignmentAssertion(HorizontalAlignment.CENTER))
                    )
                ),
                Arguments.of(
                    Named.of(
                        "text styles",
                        listOf(
                            CellSettings.create(
                                isBold = true,
                                isItalic = true,
                                isStrikeout = true
                            )
                        )
                    ),
                    listOf(
                        GenericCell(0, 0, CellFontSettingsAssertion(AssertFontType.BOLD, false)),
                        GenericCell(0, 0, CellFontSettingsAssertion(AssertFontType.ITALIC, false)),
                        GenericCell(0, 0, CellFontSettingsAssertion(AssertFontType.STRIKEOUT, false)),
                        GenericCell(1, 0, CellFontSettingsAssertion(AssertFontType.BOLD, true)),
                        GenericCell(1, 0, CellFontSettingsAssertion(AssertFontType.ITALIC, true)),
                        GenericCell(1, 0, CellFontSettingsAssertion(AssertFontType.STRIKEOUT, true)),
                    )
                ),
                Arguments.of(
                    Named.of(
                        "font size test case", listOf(
                            CellSettings.create(
                                height = 18
                            )
                        )
                    ),
                    listOf(
                        GenericCell(0, 0, CellStyleAssertion.CellFontHeightAssertion(XSSFFont.DEFAULT_FONT_SIZE)),
                        GenericCell(1, 0, CellStyleAssertion.CellFontHeightAssertion(18))
                    )
                ),
                Arguments.of(
                    Named.of(
                        "cell borders", listOf(
                            CellSettings.create(
                                height = 18,
                                borders = CellBorders(
                                    left = BorderStyle.THIN,
                                    right = BorderStyle.DASHED,
                                    bottom = BorderStyle.HAIR,
                                    top = BorderStyle.MEDIUM
                                )
                            )
                        )
                    ),
                    listOf(
                        GenericCell(
                            0, 0, CellStyleAssertion.CellBordersAssertion(BorderLocation.TOP, BorderStyle.NONE)
                        ),
                        GenericCell(
                            1, 0, CellStyleAssertion.CellBordersAssertion(BorderLocation.LEFT, BorderStyle.THIN)
                        ),
                        GenericCell(
                            1, 0, CellStyleAssertion.CellBordersAssertion(BorderLocation.RIGHT, BorderStyle.DASHED)
                        ),
                        GenericCell(
                            1, 0, CellStyleAssertion.CellBordersAssertion(BorderLocation.BOTTOM, BorderStyle.HAIR)
                        ),
                        GenericCell(
                            1, 0, CellStyleAssertion.CellBordersAssertion(BorderLocation.TOP, BorderStyle.MEDIUM)
                        )
                    )
                ),
                Arguments.of(
                    Named.of(
                        "font name test case", listOf(
                            CellSettings.create(
                                font = "Arial"
                            )
                        )
                    ),
                    listOf(
                        GenericCell(
                            0,
                            0,
                            CellStyleAssertion.CellFontNameAlignmentAssertion(XSSFFont.DEFAULT_FONT_NAME)
                        ),
                        GenericCell(1, 0, CellStyleAssertion.CellFontNameAlignmentAssertion("Arial"))
                    )
                )
            )
        }

        @JvmStatic
        fun typesTestCases(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(Named.of("int number", 0), CellType.NUMERIC),
                Arguments.of(Named.of("bigdecimal number", BigDecimal.valueOf(0)), CellType.NUMERIC),
                Arguments.of(Named.of("biginteger number", BigInteger.valueOf(0)), CellType.NUMERIC),
                Arguments.of(Named.of("double number", 0.0), CellType.NUMERIC),
                Arguments.of(Named.of("float number", 0.0f), CellType.NUMERIC),
                Arguments.of(Named.of("date", Date()), CellType.NUMERIC),
                Arguments.of(Named.of("timestamp", Timestamp.from(Instant.now())), CellType.NUMERIC),
                Arguments.of(Named.of("localdate", LocalDate.now()), CellType.NUMERIC),
                Arguments.of(Named.of("string number", "0"), CellType.STRING),
                Arguments.of(Named.of("string number", "sample text"), CellType.STRING)
            )
        }
    }

    @MethodSource(value = ["styleTestCases"])
    @ParameterizedTest(name = "{argumentsWithNames}")
    fun `test cell style`(
        cellSettings: List<CellSettings>,
        assertions: List<GenericCell<CellStyleAssertion>>
    ) {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val someSource = LineSource.static(
            cellSettings.mapIndexed { index, elem ->
                Line.fromMap(mapOf("column#$index" to "column value $index"))
            }.toList()
        )

        val columns = cellSettings.mapIndexed { index, elem ->
            SimpleTableColumn(title = "column#$index", id = "column#$index", styleId = "style#$index")
        }.toList()

        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)

        val renderBuilder = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource);

        cellSettings.forEachIndexed() { index, elem ->
            renderBuilder.appendStyle("style#$index", elem)
        }

        val renderer: PoiDocumentRenderer = renderBuilder
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

        assertTrue(file.exists(), "file was not created")
        assertCellStyles(file, 0, assertions)
    }

    @MethodSource(value = ["typesTestCases"])
    @ParameterizedTest(name = "{argumentsWithNames}")
    fun `generic cell test`(value: Any, cellType: CellType) {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("column" to value))
            )
        )

        val columns = listOf(
            SimpleTableColumn(title = "column", id = "column")
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
        assertCells(
            file, 0, listOf(
                GenericCell(0, 0, CellAssertion.CellTypeAssertion(CellType.STRING)),
                GenericCell(1, 0, CellAssertion.CellTypeAssertion(cellType))
            )
        )
    }

    @Test
    fun `test table shifts`() {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("col0" to "1", "col1" to "2")),
                Line.fromMap(mapOf("col0" to "3", "col1" to "4")),
                Line.fromMap(mapOf("col0" to "5", "col1" to "6"))
            )
        )

        val columns = listOf(
            SimpleTableColumn(title = "col0", id = "col0"),
            SimpleTableColumn(title = "col1", id = "col1")
        )

        val shifts = SimpleContentShifts(left = 1, top = 1)

        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId, shifts),
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
                arrayOf("", "", ""),
                arrayOf("", "col0", "col1"),
                arrayOf("", "1", "2"),
                arrayOf("", "3", "4"),
                arrayOf("", "5", "6"),
            )
        )
    }

    @Test
    fun rightSheetName() {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val emptySheetContent: List<Content> = listOf(
            SimpleTableContent(emptyList(), sourceId)
        )

        val sheet0 = SimpleSheet("first sheet", emptySheetContent)
        val sheet1 = SimpleSheet("second sheet", emptySheetContent)
        val sheet2 = SimpleSheet("third sheet", emptySheetContent)

        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .appendSheet(sheet1)
            .appendSheet(sheet2)
            .externalSource(sourceId, LineSource.static(emptyList()))
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

        assertTrue(file.exists(), "file was not created")
        assertSheetNameInFile(file, 0, "first sheet")
        assertSheetNameInFile(file, 1, "second sheet")
        assertSheetNameInFile(file, 2, "third sheet")
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

    @Test
    fun `test table hide header`() {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("col0" to "1", "col1" to "2")),
                Line.fromMap(mapOf("col0" to "3", "col1" to "4")),
                Line.fromMap(mapOf("col0" to "5", "col1" to "6"))
            )
        )

        val columns = listOf(
            SimpleTableColumn(title = "col0", id = "col0"),
            SimpleTableColumn(title = "col1", id = "col1")
        )

        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId, showHeader = false),
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
                arrayOf("1", "2"),
                arrayOf("3", "4"),
                arrayOf("5", "6"),
            )
        )
    }

    @Test
    fun `test table header style`() {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val headerStyleId = "header style"
        val headerStyle = CellSettings.create(
            font = "Arial"
        )
        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("col0" to "1", "col1" to "2")),
            )
        )
        val columns = listOf(
            SimpleTableColumn(title = "col0", id = "col0", headerStyleId = headerStyleId),
            SimpleTableColumn(title = "col1", id = "col1")
        )
        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)
        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource)
            .appendStyle(headerStyleId, headerStyle)
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

        assertTrue(file.exists(), "file was not created")
        assertCellStyles(
            file, 0, listOf(
                GenericCell(0, 0, CellFontNameAlignmentAssertion("Arial")),
                GenericCell(0, 1, CellFontNameAlignmentAssertion(XSSFFont.DEFAULT_FONT_NAME)),
                GenericCell(1, 0, CellFontNameAlignmentAssertion(XSSFFont.DEFAULT_FONT_NAME)),
                GenericCell(1, 1, CellFontNameAlignmentAssertion(XSSFFont.DEFAULT_FONT_NAME))
            )
        )
    }

    @Test
    fun `test cell size`() {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val sourceId = "source_id_01"

        val headerStyleId = "header style"
        val headerStyle = CellSettings.create(
        )
        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("col0" to "1", "col1" to "2")),
            )
        )
        val columns = listOf(
            SimpleTableColumn(title = "col0", id = "col0", headerStyleId = headerStyleId, height = 400),
            SimpleTableColumn(title = "col1", id = "col1", width = 4000)
        )
        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)
        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource)
            .appendStyle(headerStyleId, headerStyle)
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

        assertTrue(file.exists(), "file was not created")
        assertRowHeight(file, sheetId = 0, rowIdx = 1, 400)
        assertColumnWidth(file, sheetId = 0, columnIdx = 1, 4000)
    }

    @Test
    fun `test cell picture`() {
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        val inputStreamProvider = { FileInputStream(getResourceAsFile("/test.jpg")) }
        val relationId = "test-image-relation"

        val sourceId = "source_id_01"

        val someSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("col0" to "1", "col1" to "2")),
            )
        )
        val columns = listOf(
            SimpleTableColumn(title = "col0", id = "col0"),
            SimpleTableColumn(title = "col1", id = "col1")
        )
        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
            SimplePictureContent(
                relationId = relationId,
                shift = SimpleContentShifts(left = 2, top = 2),
                widthCells = 2,
                heightCells = 2,
            )
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)
        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .appendPicture(relationId, inputStreamProvider)
            .externalSource(sourceId, someSource)
            .build(RenderProperties.empty())

        renderer.renderToFile(file)

        /**
        1: | col0 | col1 |     |     |
        2: | 1    |    2 |     |     |
        3: |      |      |     |     |
        4: |      |      |     |     |
        5: |      |      | pic | pic |
        6: |      |      | pic | pic |
         */

        assertTrue(file.exists(), "file was not created")
        assertPicture(
            file,
            0,
            relationId,
            TwoCellAnchor(from = Anchor(col = 3, row = 5), to = Anchor(col = 4, row = 6))
        )
    }

    fun getResourceAsFile(path: String): File {
        return File(object {}.javaClass.getResource(path).file)
    }

}