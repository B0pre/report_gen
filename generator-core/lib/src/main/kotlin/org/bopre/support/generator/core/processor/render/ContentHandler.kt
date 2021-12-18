package org.bopre.support.generator.core.processor.render

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content

interface ContentHandler {
    fun handleContent(
        sheet: XSSFSheet, rowStart: Int, content: Content, settings: DocumentSettings
    ): Int

    fun supports(content: Content): Boolean
}