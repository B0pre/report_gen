package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleSeparatorContent
import org.bopre.support.generator.core.processor.content.impl.SimpleTableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleTableContent

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
            return SimpleTableColumn(title = colTitle, id = colId)
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