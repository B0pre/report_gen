package org.bopre.support.generator.core.processor.content

class SimpleTableContent(
    private val columns: List<TableColumn>,
    private val sourceId: String
) : TableContent {
    override fun getColumns(): List<TableColumn> = columns
    override fun getSourceId(): String = sourceId
}