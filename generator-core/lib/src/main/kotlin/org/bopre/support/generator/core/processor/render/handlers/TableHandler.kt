package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableContent
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.render.RenderContext
import java.time.LocalDate
import java.util.*

class TableHandler : ContentHandler {

    companion object {
        private const val DEFAULT_DATE_FORMAT: String = "d/m/yyyy"
    }

    override fun handleContent(
        sheet: XSSFSheet,
        content: Content,
        context: RenderContext
    ): Int {
        if (content !is TableContent) return context.currentRow()
        val rowStart = context.currentRow()
        val settings = context.getSettings()
        val properties = context.getProperties()

        var currentRowNum = rowStart
        //render header
        val header = sheet.createRow(currentRowNum++)
        var columnNum = 0
        for (column in content.getColumns()) {
            val cell = header.createCell(columnNum++)
            cell.setCellValue(column.getTitle())
        }
        val sourceId = content.getSourceId()
        var lineSource: LineSource = settings.getSource(sourceId)

        //render body
        for (line in lineSource.start(properties)) {
            val bodyRow = sheet.createRow(currentRowNum++)
            var bodyColumnNum = 0
            for (column in content.getColumns()) {
                val cell = bodyRow.createCell(bodyColumnNum++)
                val newStyle = createStyle(sheet.workbook, column.getSettings())
                cell.setCellStyle(newStyle)
                cell.setCellValueGeneric(column.getValue(line))
            }
        }
        return currentRowNum
    }

    private fun Cell.setCellValueGeneric(value: Any) {
        when (value) {
            is String -> this.setCellValue(value)
            is Number -> this.setCellValue(value.toDouble())
            is Date -> {
                val helper = this.sheet.workbook.creationHelper
                this.cellStyle.dataFormat = helper.createDataFormat().getFormat(DEFAULT_DATE_FORMAT)
                this.setCellValue(value)
            }
            is LocalDate -> {
                val helper = this.sheet.workbook.creationHelper
                this.cellStyle.dataFormat = helper.createDataFormat().getFormat(DEFAULT_DATE_FORMAT)
                this.setCellValue(value)
            }
            else -> {
                this.setCellValue(value.toString())
            }
        }
    }

    override fun supports(content: Content): Boolean {
        return content is TableContent
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