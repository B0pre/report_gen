package org.bopre.support.generator.core.processor.content

import org.bopre.support.generator.core.processor.data.Line

interface TableColumn {
    fun getTitle(): String
    fun getValue(source: Line): String
}
