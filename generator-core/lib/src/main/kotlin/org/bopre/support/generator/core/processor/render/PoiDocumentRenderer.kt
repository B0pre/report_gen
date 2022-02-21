package org.bopre.support.generator.core.processor.render

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.processor.content.Sheet
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.handlers.ContentHandler
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver
import java.io.File
import java.io.FileOutputStream

class PoiDocumentRenderer(
    val sheets: List<Sheet>,
    val handlers: List<ContentHandler<org.apache.poi.ss.usermodel.Sheet>>,
    val settings: DocumentSettings,
    private val styles: Map<String, CellSettings> = emptyMap(),
    private val properties: RenderProperties = RenderProperties.empty(),
) : Generator() {
    override fun renderToFile(file: File) {
        val workBook = renderWorkBook()

        //write work book content to file
        val fileOutputStream = FileOutputStream(file)
        workBook.write(fileOutputStream)
        fileOutputStream.close()
    }

    private fun renderWorkBook(): XSSFWorkbook {
        val workBook = XSSFWorkbook()

        val styles = prepareStyles(workBook)
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

    private fun prepareStyles(workbook: XSSFWorkbook): StyleResolver {
        val stylesMap = styles
            .map { it.key to createStyle(workbook, it.value) }
            .toMap()
        return StyleResolver.of(stylesMap)
    }

    private fun createStyle(workbook: XSSFWorkbook, settings: CellSettings): CellStyle {
        val newStyle = workbook.createCellStyle()
        val newFont = workbook.createFont()
        settings.getHeightInPoints()?.let { newFont.fontHeightInPoints = it }
        settings.getBorders()?.let {
            newStyle.borderLeft = it.left
            newStyle.borderRight = it.right
            newStyle.borderTop = it.top
            newStyle.borderBottom = it.bottom
        }
        settings.getIsWrapped()?.let {
            newStyle.wrapText = it
        }
        settings.getHorizontalAlignment()?.let {
            newStyle.alignment = it
        }
        settings.getVerticalAlignment()?.let {
            newStyle.verticalAlignment = it
        }
        settings.getBold()?.let {
            newFont.bold = it
        }
        settings.getItalic()?.let {
            newFont.italic = it
        }
        settings.getStrikeout()?.let {
            newFont.strikeout = it
        }
        settings.getFontName()?.let {
            newFont.fontName = it
        }
        settings.getDataFormat()?.let {
            val format = workbook.createDataFormat()
            newStyle.dataFormat = format.getFormat(it)
        }
        newStyle.setFont(newFont)
        return newStyle
    }

}
