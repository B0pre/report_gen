package org.bopre.support.generator.core.processor.content

import org.bopre.support.generator.core.processor.content.Content

interface SeparatorContent : Content {
    fun getLinesToSkip(): Int
}