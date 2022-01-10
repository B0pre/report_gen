package org.bopre.support.generator.core.testutils.xls

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileInputStream
import kotlin.test.assertEquals

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