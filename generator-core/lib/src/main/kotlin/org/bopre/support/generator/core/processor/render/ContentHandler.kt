package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.data.RenderProperties

interface ContentHandler {
    fun handleContent(
        sheet: XSSFSheet, rowStart: Int, content: Content, settings: DocumentSettings, properties: RenderProperties
    ): Int

    fun supports(content: Content): Boolean
}