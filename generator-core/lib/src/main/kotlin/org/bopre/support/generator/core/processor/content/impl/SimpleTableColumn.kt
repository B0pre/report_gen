package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.data.Line

class SimpleTableColumn(
    private val title: String,
    private val id: String,
    private val styleId: String = "",
    private val headerStyleId: String = ""
) : TableColumn {

    override fun getTitle(): String {
        return title
    }

    override fun getStyleId(): String = styleId

    override fun getHeaderStyleId(): String = headerStyleId

    override fun getValue(source: Line): Any =
        source.getCell(id)

}
