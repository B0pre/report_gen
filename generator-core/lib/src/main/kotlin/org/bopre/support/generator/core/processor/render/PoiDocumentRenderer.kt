package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.processor.content.Sheet
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.handlers.ContentHandler
import org.bopre.support.generator.core.processor.render.support.XSSFWorkbookManager
import java.io.File
import java.io.FileOutputStream

class PoiDocumentRenderer(
    val sheets: List<Sheet>,
    val handlers: List<ContentHandler<org.apache.poi.ss.usermodel.Sheet>>,
    val settings: DocumentSettings,
    private val styles: Map<String, CellSettings> = emptyMap(),
    private val properties: RenderProperties = RenderProperties.empty(),
) : Generator() {

    private val workbookManager = XSSFWorkbookManager()

    override fun renderToFile(file: File) {
        val workBook = renderWorkBook()

        //write work book content to file
        val fileOutputStream = FileOutputStream(file)
        workBook.write(fileOutputStream)
        fileOutputStream.close()

        workbookManager.clean(workBook)
    }

    private fun renderWorkBook(): XSSFWorkbook {
        val workBook = workbookManager.create()

        val styles = workbookManager.prepareStyles(workBook, styles)
        for (sheet in sheets) {
            val sheetPOI = workBook.createSheet(sheet.getTitle())
            var context = RenderContext.init(settings = settings, properties = properties, styleResolver = styles)
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
