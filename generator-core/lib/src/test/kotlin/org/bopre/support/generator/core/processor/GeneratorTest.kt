package org.bopre.support.generator.core.processor

import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.bopre.support.generator.core.processor.render.Generators
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorTest {

    @Test
    fun `file is created after report gen`() {
        val fileYaml = getResourceAsFile("/examples/definition.yaml")
        val generator = Generators.fromYaml(fileYaml)
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()

        generator.renderToFile(
            file
        )

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

    fun getResourceAsFile(path: String): File {
        return File(object {}.javaClass.getResource(path).file)
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