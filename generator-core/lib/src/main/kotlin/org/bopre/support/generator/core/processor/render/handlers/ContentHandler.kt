package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.ss.usermodel.Sheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.render.RenderContext

interface ContentHandler<T : Sheet> {
    fun handleContent(
        sheet: T, content: Content, context: RenderContext,
    ): Int

    fun supports(content: Content): Boolean
}