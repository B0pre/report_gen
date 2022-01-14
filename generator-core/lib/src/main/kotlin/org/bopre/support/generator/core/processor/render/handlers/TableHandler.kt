package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableContent
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
        val shifts = content.getShifts()

        var currentRowNum = rowStart

        //skip rows for top shift
        for (i in 0 until shifts.getShiftTop())
            sheet.createRow(currentRowNum++)

        //render header
        if (content.showHeader()) {
            val header = sheet.createRow(currentRowNum++)
            var columnNum = 0
            //skip cells for left shift(header)
            for (i in 0 until shifts.getShiftLeft())
                header.createCell(columnNum++)
            for (column in content.getColumns()) {
                val cell = header.createCell(columnNum++)
                cell.setCellValue(column.getTitle())
            }
        }

        val sourceId = content.getSourceId()
        var lineSource: LineSource = settings.getSource(sourceId)

        //render body
        for (line in lineSource.start(properties)) {
            val bodyRow = sheet.createRow(currentRowNum++)
            var bodyColumnNum = 0

            //skip cells for left shift (body)
            for (i in 0 until shifts.getShiftLeft())
                bodyRow.createCell(bodyColumnNum++)

            for (column in content.getColumns()) {
                val cell = bodyRow.createCell(bodyColumnNum++)
                val newStyle = context.getStyleResolver().resolve(column.getStyleId())
                if (newStyle != null)
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

}