package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableContent
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.render.RenderContext

class TableHandler : ContentHandler {
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
                cell.setCellValue(column.getValue(line))
            }
        }
        return currentRowNum
    }

    override fun supports(content: Content): Boolean {
        return content is TableContent
    }

}