package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleSeparatorContent
import org.bopre.support.generator.core.processor.content.impl.SimpleTableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleTableContent
import org.bopre.support.generator.core.processor.content.style.CellBorders
import org.bopre.support.generator.core.processor.content.style.CellSettings
import org.bopre.support.generator.core.yaml.data.CellParameters
import org.bopre.support.generator.core.yaml.data.ContentDefinition

class ContentConfigurer {

    fun configureContent(definition: ContentDefinition): Content {
        return when (definition) {
            is ContentDefinition.TableDefinition ->
                table(definition)
            is ContentDefinition.Separator ->
                separator(definition)
        }
    }

    private fun table(tableDefinition: ContentDefinition.TableDefinition): Content =
        SimpleTableContent(
            sourceId = tableDefinition.sourceId,
            columns = tableDefinition.columns
                .mapIndexed { index, cellParameters -> toColumn(cellParameters, index) }
                .toList()
        )

    private fun separator(separatorDefinition: ContentDefinition.Separator): Content =
        SimpleSeparatorContent(separatorDefinition.strength)

    private fun toColumn(cell: CellParameters, index: Int): TableColumn {
        val colTitle = cell.title ?: "$index"
        val colId = cell.id ?: "$index"
        var cellSettingsBuilder = CellSettings.builder()
        if (cell.style != null) {
            val bordersBuilder = CellBorders.builder()
            cell.style.borders?.left?.let { bordersBuilder.left(it) }
            cell.style.borders?.right?.let { bordersBuilder.right(it) }
            cell.style.borders?.top?.let { bordersBuilder.top(it) }
            cell.style.borders?.bottom?.let { bordersBuilder.bottom(it) }

            cell.style.fontSize?.let { cellSettingsBuilder.height(it) }

            cell.style.alignV?.let { cellSettingsBuilder.verticalAlignment(it) }
            cell.style.alignH?.let { cellSettingsBuilder.horizontalAlignment(it) }

            cell.style.bold?.let { cellSettingsBuilder.isBold(it) }
            cell.style.italic?.let { cellSettingsBuilder.isItalic(it) }
            cell.style.strikeout?.let { cellSettingsBuilder.isStrikeout(it) }

            cell.style.wrapped?.let { cellSettingsBuilder.isWrapped(it) }

            cell.style.font?.let { cellSettingsBuilder.font(it) }

            cell.style.format?.let { cellSettingsBuilder.dataFormat(it) }

            cellSettingsBuilder.borders(bordersBuilder.build())
        }
        return SimpleTableColumn(title = colTitle, id = colId, style = cellSettingsBuilder.build())
    }
}