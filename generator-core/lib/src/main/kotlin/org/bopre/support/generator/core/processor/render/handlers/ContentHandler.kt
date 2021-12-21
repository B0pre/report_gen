package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.render.RenderContext

interface ContentHandler {
    fun handleContent(
        sheet: XSSFSheet, content: Content, context: RenderContext
    ): Int

    fun supports(content: Content): Boolean
}