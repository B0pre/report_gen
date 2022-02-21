package org.bopre.support.generator.core.processor.render.support

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver

class XSSFWorkbookManager : WorkbookManager<XSSFWorkbook> {

    private val workbookStyleManager = DefaultWorkbookStyleManager()

    override fun create(): XSSFWorkbook {
        return XSSFWorkbook()
    }

    override fun clean(workbook: XSSFWorkbook) {
    }

    override fun prepareStyles(workbook: XSSFWorkbook, styles: Map<String, CellSettings>): StyleResolver =
        workbookStyleManager.prepareStyles(workbook, styles)
}