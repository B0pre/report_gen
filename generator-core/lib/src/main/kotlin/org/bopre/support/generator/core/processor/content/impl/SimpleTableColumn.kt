package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.processor.data.Line

class SimpleTableColumn(
    private val title: String,
    private val id: String,
    private val style: CellSettings = CellSettings.empty()
) : TableColumn {

    override fun getTitle(): String {
        return title
    }

    override fun getSettings(): CellSettings = style

    override fun getValue(source: Line): Any =
        source.getCell(id)

}
