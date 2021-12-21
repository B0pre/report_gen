package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.processor.content.Sheet
import org.bopre.support.generator.core.processor.data.RenderProperties
import java.io.File
import java.io.FileOutputStream

class PoiDocumentRenderer(
    val sheets: List<Sheet>,
    val handlers: List<ContentHandler>,
    val settings: DocumentSettings
) : Generator() {
    override fun renderToFile(file: File, properties: RenderProperties) {
        val workBook = renderWorkBook(properties)

        //write work book content to file
        val fileOutputStream = FileOutputStream(file)
        workBook.write(fileOutputStream)
        fileOutputStream.close()
    }

    private fun renderWorkBook(properties: RenderProperties): XSSFWorkbook {
        val workBook = XSSFWorkbook()
        for (sheet in sheets) {
            val sheetPOI = workBook.createSheet(sheet.getTitle())
            var context = RenderContext.init(settings = settings, properties = properties)
            for (content in sheet.getContents()) {
                for (handler in handlers) {
                    if (!handler.supports(content)) continue
                    val rowShift = handler.handleContent(sheetPOI, content, context)
                    context = RenderContext.shiftRow(context, rowShift)
                    break
                }
            }
        }
        return workBook
    }

}
