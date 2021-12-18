package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.processor.content.Sheet
import java.io.File
import java.io.FileOutputStream

class PoiDocumentRenderer(
    val sheets: List<Sheet>,
    val handlers: List<ContentHandler>,
    val settings: DocumentSettings
) {
    fun renderToFile(file: File) {
        val workBook = renderWorkBook()

        //write work book content to file
        val fileOutputStream = FileOutputStream(file)
        workBook.write(fileOutputStream)
        fileOutputStream.close()
    }

    private fun renderWorkBook(): XSSFWorkbook {
        val workBook = XSSFWorkbook()
        for (sheet in sheets) {
            val sheetPOI = workBook.createSheet(sheet.title)
            var currentRowNum = 0;
            for (content in sheet.contents) {
                for (handler in handlers) {
                    if (!handler.supports(content)) continue
                    currentRowNum = handler.handleContent(sheetPOI, currentRowNum, content, settings)
                    break
                }
            }
        }
        return workBook
    }

}
