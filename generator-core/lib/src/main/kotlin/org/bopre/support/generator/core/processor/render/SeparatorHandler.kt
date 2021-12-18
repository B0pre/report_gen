package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.SeparatorContent
import org.bopre.support.generator.core.processor.data.RenderProperties

class SeparatorHandler : ContentHandler {
    override fun handleContent(
        sheet: XSSFSheet,
        rowStart: Int,
        content: Content,
        settings: DocumentSettings,
        properties: RenderProperties
    ): Int {
        var currentRowNum = rowStart
        if (content is SeparatorContent) {
            for (i: Int in 1..content.linesToSkip) {
                val row = sheet.createRow(currentRowNum++)
                val cell = row.createCell(0)
                cell.setCellValue("")
            }
            return currentRowNum
        } else return rowStart
    }

    override fun supports(content: Content): Boolean {
        return content is SeparatorContent
    }

}