package org.bopre.support.generator.core.processor.render.support

import org.apache.poi.ss.usermodel.Workbook
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver

interface WorkbookManager<WB : Workbook> {

    fun create(): WB

    fun clean(workbook: WB)

    fun prepareStyles(workbook: WB, styles: Map<String, CellSettings>): StyleResolver

}