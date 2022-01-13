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
import org.bopre.support.generator.core.yaml.data.StyleDefinition
import org.bopre.support.generator.core.yaml.data.StyleUsage

class ContentConfigurer {

    fun configureContent(definition: ContentDefinition, styles: Map<String, StyleDefinition>): Content {
        return when (definition) {
            is ContentDefinition.TableDefinition ->
                table(definition, styles)
            is ContentDefinition.Separator ->
                separator(definition)
        }
    }

    private fun table(
        tableDefinition: ContentDefinition.TableDefinition,
        styles: Map<String, StyleDefinition>
    ): Content =
        SimpleTableContent(
            sourceId = tableDefinition.sourceId,
            columns = tableDefinition.columns
                .mapIndexed { index, cellParameters -> toColumn(cellParameters, index, styles) }
                .toList()
        )

    private fun separator(separatorDefinition: ContentDefinition.Separator): Content =
        SimpleSeparatorContent(separatorDefinition.strength)

    private fun toColumn(
        cell: CellParameters, index: Int,
        styles: Map<String, StyleDefinition>
    ): TableColumn {
        val colTitle = cell.title ?: "$index"
        val colId = cell.id ?: "$index"
        var cellSettingsBuilder = CellSettings.builder()
        val cellStyle = getCellStyle(cell, styles)
        if (cellStyle != null) {
            val bordersBuilder = CellBorders.builder()
            cellStyle.borders?.left?.let { bordersBuilder.left(it) }
            cellStyle.borders?.right?.let { bordersBuilder.right(it) }
            cellStyle.borders?.top?.let { bordersBuilder.top(it) }
            cellStyle.borders?.bottom?.let { bordersBuilder.bottom(it) }

            cellStyle.fontSize?.let { cellSettingsBuilder.height(it) }

            cellStyle.alignV?.let { cellSettingsBuilder.verticalAlignment(it) }
            cellStyle.alignH?.let { cellSettingsBuilder.horizontalAlignment(it) }

            cellStyle.bold?.let { cellSettingsBuilder.isBold(it) }
            cellStyle.italic?.let { cellSettingsBuilder.isItalic(it) }
            cellStyle.strikeout?.let { cellSettingsBuilder.isStrikeout(it) }

            cellStyle.wrapped?.let { cellSettingsBuilder.isWrapped(it) }

            cellStyle.font?.let { cellSettingsBuilder.font(it) }

            cellStyle.format?.let { cellSettingsBuilder.dataFormat(it) }

            cellSettingsBuilder.borders(bordersBuilder.build())
        }
        return SimpleTableColumn(title = colTitle, id = colId, style = cellSettingsBuilder.build())
    }

    fun getCellStyle(cell: CellParameters, styles: Map<String, StyleDefinition>): StyleDefinition? {
        val cellStyle = cell.style
        return when (cellStyle) {
            is StyleUsage.InlineStyle -> cellStyle.definition
            is StyleUsage.DefinedStyle -> styles.get(cellStyle.id)
            else -> null
        }
    }
}