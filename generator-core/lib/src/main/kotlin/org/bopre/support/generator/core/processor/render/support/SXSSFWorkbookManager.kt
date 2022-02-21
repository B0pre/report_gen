package org.bopre.support.generator.core.processor.render.support

import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver

class SXSSFWorkbookManager : WorkbookManager<SXSSFWorkbook> {

    private val workbookStyleManager = DefaultWorkbookStyleManager()

    override fun create(): SXSSFWorkbook {
        return SXSSFWorkbook()
    }

    override fun clean(workbook: SXSSFWorkbook) {
        workbook.dispose()
    }

    override fun prepareStyles(workbook: SXSSFWorkbook, styles: Map<String, CellSettings>): StyleResolver =
        workbookStyleManager.prepareStyles(workbook, styles)

}