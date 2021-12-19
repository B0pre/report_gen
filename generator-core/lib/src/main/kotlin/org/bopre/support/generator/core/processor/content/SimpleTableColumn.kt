package org.bopre.support.generator.core.processor.content

import org.bopre.support.generator.core.processor.data.Line

class SimpleTableColumn(
    private val title: String,
    private val id: String
) : TableColumn {

    override fun getTitle(): String {
        return title
    }

    override fun getValue(source: Line): String =
        source.getCell(id)

}
