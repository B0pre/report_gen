package org.bopre.support.generator.core.processor.content

import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.Line

interface TableColumn {
    fun getTitle(): String
    fun getSettings(): CellSettings
    fun getValue(source: Line): Any
}
