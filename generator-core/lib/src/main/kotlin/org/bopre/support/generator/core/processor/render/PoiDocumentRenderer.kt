package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.bopre.support.generator.core.processor.content.Sheet
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.handlers.ContentHandler
import org.bopre.support.generator.core.processor.render.support.SXSSFWorkbookManager
import org.bopre.support.generator.core.utils.CloseableWrap
import java.io.File
import java.io.FileOutputStream

class PoiDocumentRenderer(
    val sheets: List<Sheet>,
    val handlers: List<ContentHandler<org.apache.poi.ss.usermodel.Sheet>>,
    val settings: DocumentSettings,
    private val styles: Map<String, CellSettings> = emptyMap(),
    private val properties: RenderProperties = RenderProperties.empty(),
) : Generator() {

    private val workbookManager = SXSSFWorkbookManager()

    override fun renderToFile(file: File) {
        //create, render and close workbook
        createAutoCloseableWorkbook()
            .use {
                val workBook = it.value

                //render book
                renderWorkBook(workBook)

                //write workbook content to file
                FileOutputStream(file)
                    .use {
                        workBook.write(it)
                    }
            }
    }

    /**
     * create instance of closeable wrap for workbook
     */
    private fun createAutoCloseableWorkbook(): CloseableWrap<SXSSFWorkbook> =
        CloseableWrap(value = workbookManager.create(), closeAction = {
            workbookManager.clean(it)
        })

    private fun renderWorkBook(workBook: SXSSFWorkbook) {
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
    }

}
