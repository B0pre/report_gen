package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleSeparatorContent
import org.bopre.support.generator.core.processor.content.impl.SimpleTableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleTableContent
import org.bopre.support.generator.core.processor.content.style.CellBorders
import org.bopre.support.generator.core.processor.content.style.CellSettings

@Serializable
sealed class ContentDefinition {
    @Serializable
    @SerialName("table")
    data class TableDefinition(
        val id: String,
        val title: String?,
        val sourceId: String,
        val columns: List<CellParameters> = emptyList()
    ) : ContentDefinition() {
        override fun toContent(): Content =
            SimpleTableContent(
                sourceId = sourceId,
                columns = columns
                    .mapIndexed { index, cellParameters -> toColumn(cellParameters, index) }
                    .toList()
            )

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

    @Serializable
    @SerialName("separator")
    data class Separator(val strength: Int = 1) : ContentDefinition() {
        override fun toContent(): Content =
            SimpleSeparatorContent(strength)
    }

    abstract fun toContent(): Content
}