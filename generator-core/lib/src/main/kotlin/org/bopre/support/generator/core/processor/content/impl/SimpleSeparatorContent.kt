package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.SeparatorContent

class SimpleSeparatorContent(
    private val linesToSkip: Int
) : SeparatorContent {
    override fun getLinesToSkip(): Int = linesToSkip
}