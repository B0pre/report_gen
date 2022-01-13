package org.bopre.support.generator.core.processor

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFFont
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.impl.SimpleSeparatorContent
import org.bopre.support.generator.core.processor.content.impl.SimpleSheet
import org.bopre.support.generator.core.processor.content.impl.SimpleTableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleTableContent
import org.bopre.support.generator.core.processor.content.style.CellBorders
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.PoiDocumentRenderer
import org.bopre.support.generator.core.processor.render.PoiDocumentRendererBuilder
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.*
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.CellBordersAssertion.BorderLocation
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.CellFontSettingsAssertion.AssertFontType
import org.bopre.support.generator.core.testutils.xls.GenericCell
import org.bopre.support.generator.core.testutils.xls.assertCellStyles
import org.bopre.support.generator.core.testutils.xls.assertSheetInFile
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
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
            SimpleTableColumn(title = "column#$index", id = "column#$index", style = elem)
        }.toList()

        val contentsForSheet0: List<Content> = listOf(
            SimpleTableContent(columns, sourceId),
        )

        val sheet0 = SimpleSheet("sheet0", contentsForSheet0)

        val renderer: PoiDocumentRenderer = PoiDocumentRendererBuilder()
            .appendSheet(sheet0)
            .externalSource(sourceId, someSource)
            .build(RenderProperties.empty())

        renderer.renderToFile(file)
        println("rendered $file")

        assertTrue(file.exists(), "file was not created")
        assertCellStyles(file, 0, assertions)
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