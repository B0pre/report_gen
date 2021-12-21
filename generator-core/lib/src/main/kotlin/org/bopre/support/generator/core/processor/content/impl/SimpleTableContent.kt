package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.content.TableContent

class SimpleTableContent(
    private val columns: List<TableColumn>,
    private val sourceId: String
) : TableContent {
    override fun getColumns(): List<TableColumn> = columns
    override fun getSourceId(): String = sourceId
}