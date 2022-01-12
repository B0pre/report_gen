package org.bopre.support.generator.core.testutils.xls

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import java.io.File
import java.io.FileInputStream
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

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

fun assertCellStyles(file: File, sheetId: Int, assertions: List<GenericCell<CellStyleAssertion>>) {
    val inputStream = FileInputStream(file)
    //Instantiate Excel workbook using existing file:
    val xlWb = WorkbookFactory.create(inputStream)

    val xlWs = xlWb.getSheetAt(sheetId)
    for (styleAssertion in assertions) {
        val actual = xlWs.getRow(styleAssertion.row)
            .getCell(styleAssertion.col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
            .cellStyle

        val cell = xlWs.getRow(styleAssertion.row)
            .getCell(styleAssertion.col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
            .toString()
        styleAssertion.value.assertCell(actual, "($cell) [${styleAssertion.row}:${styleAssertion.col}]")
    }
}

fun interface CellStyleAssertion {

    fun assertCell(cellStyle: CellStyle, message: String)

    abstract class CellStyleAssertionRouter : CellStyleAssertion {
        abstract fun assertXSSFCell(cellStyle: XSSFCellStyle, message: String)
        override fun assertCell(cellStyle: CellStyle, message: String) {
            when (cellStyle) {
                is XSSFCellStyle -> {
                    assertXSSFCell(cellStyle, message)
                }
                else -> {
                    fail("unknown cell style format $cellStyle")
                }
            }
        }
    }

    class CellFontHeightAssertion(val height: Short) : CellStyleAssertionRouter() {
        override fun assertXSSFCell(cellStyle: XSSFCellStyle, message: String) {
            assertNotNull(cellStyle.font, "font was null")
            assertEquals(height, cellStyle.font.fontHeightInPoints, "wrong font height: $message")
        }
    }

    class CellBordersAssertion(val type: BorderLocation, val expectedBorder: BorderStyle) : CellStyleAssertionRouter() {
        enum class BorderLocation {
            LEFT,
            RIGHT,
            TOP,
            BOTTOM
        }

        override fun assertXSSFCell(cellStyle: XSSFCellStyle, message: String) {
            assertNotNull(cellStyle.font, "font was null")
            val borderStyle: BorderStyle = when (type) {
                BorderLocation.LEFT -> cellStyle.borderLeft
                BorderLocation.RIGHT -> cellStyle.borderRight
                BorderLocation.TOP -> cellStyle.borderTop
                BorderLocation.BOTTOM -> cellStyle.borderBottom
            }
            assertEquals(expected = expectedBorder, actual = borderStyle, "wrong border for $type")
        }
    }

}

data class GenericCell<T>(
    val row: Int,
    val col: Int,
    val value: T
)