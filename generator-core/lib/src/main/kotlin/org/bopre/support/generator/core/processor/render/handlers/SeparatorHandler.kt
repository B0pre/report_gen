package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.ss.usermodel.Sheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.SeparatorContent
import org.bopre.support.generator.core.processor.render.RenderContext

class SeparatorHandler : ContentHandler<Sheet> {
    override fun handleContent(
        sheet: Sheet,
        content: Content,
        context: RenderContext,
    ): Int {
        val rowStart = context.currentRow()

        var currentRowNum = rowStart
        if (content is SeparatorContent) {
            for (i: Int in 1..content.getLinesToSkip()) {
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