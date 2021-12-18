package org.bopre.support.generator.core.processor.content

class SimpleTableColumn(
    private val title: String,
    private val id: String
) : TableColumn {

    override fun getTitle(): String {
        return title
    }

    override fun getColumnId(): String {
        return id;
    }

}
