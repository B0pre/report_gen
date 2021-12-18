package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableContent
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties

class TableHandler : ContentHandler {
    override fun handleContent(
        sheet: XSSFSheet,
        rowStart: Int,
        content: Content,
        settings: DocumentSettings,
        properties: RenderProperties
    ): Int {
        if (content !is TableContent) return rowStart
        var currentRowNum = rowStart
        //render header
        val header = sheet.createRow(currentRowNum++)
        var columnNum = 0
        for (column in content.columns) {
            val cell = header.createCell(columnNum++)
            cell.setCellValue(column.getTitle())
        }
        val sourceId = content.sourceId
        var lineSource: LineSource = settings.getSource(sourceId)

        //render body
        for (line in lineSource.start(properties)) {
            val bodyRow = sheet.createRow(currentRowNum++)
            var bodyColumnNum = 0
            for (column in content.columns) {
                val cell = bodyRow.createCell(bodyColumnNum++)
                val cellId = column.getColumnId() ?: "$bodyColumnNum"
                cell.setCellValue(line.getCell(cellId))
            }
        }
        return currentRowNum
    }

    override fun supports(content: Content): Boolean {
        return content is TableContent
    }

}