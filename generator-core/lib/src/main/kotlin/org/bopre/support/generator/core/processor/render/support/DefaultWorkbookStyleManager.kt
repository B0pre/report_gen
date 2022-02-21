package org.bopre.support.generator.core.processor.render.support

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Workbook
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver

class DefaultWorkbookStyleManager : WorkbookStyleManager<Workbook> {

    override fun prepareStyles(workbook: Workbook, styles: Map<String, CellSettings>): StyleResolver {
        val stylesMap = styles
            .map { it.key to createStyle(workbook, it.value) }
            .toMap()
        return StyleResolver.of(stylesMap)
    }

    private fun createStyle(workbook: Workbook, settings: CellSettings): CellStyle {
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