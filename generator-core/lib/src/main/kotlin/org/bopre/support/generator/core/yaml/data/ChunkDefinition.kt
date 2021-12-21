package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bopre.support.generator.core.processor.content.*

@Serializable
sealed class ChunkDefinition {
    @Serializable
    @SerialName("table")
    data class TableDefinition(
        val id: String,
        val title: String?,
        val source: String,
        val cols: List<CellParameters> = emptyList()
    ) : ChunkDefinition() {
        override fun toContent(): Content =
            SimpleTableContent(
                sourceId = source,
                columns = cols
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
    data class Separator(val strength: Int = 1) : ChunkDefinition() {
        override fun toContent(): Content =
            SeparatorContent(strength)
    }

    abstract fun toContent(): Content
}