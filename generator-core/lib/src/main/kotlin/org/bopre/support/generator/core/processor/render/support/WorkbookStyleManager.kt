package org.bopre.support.generator.core.processor.render.support

import org.apache.poi.ss.usermodel.Workbook
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver

interface WorkbookStyleManager<WB : Workbook> {

    fun prepareStyles(workbook: WB, styles: Map<String, CellSettings>): StyleResolver

}