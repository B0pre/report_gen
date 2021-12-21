package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.Sheet

class SimpleSheet(
    private val title: String,
    private val contents: List<Content>
) : Sheet {
    override fun getTitle(): String = title
    override fun getContents(): List<Content> = contents
}
