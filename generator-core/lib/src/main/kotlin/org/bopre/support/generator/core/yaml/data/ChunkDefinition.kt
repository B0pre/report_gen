package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ChunkDefinition {
    @Serializable
    @SerialName("table")
    data class TableDefinition(
        val id: String,
        val title: String?,
        val source: String,
        val cols: List<CellParameters> = emptyList()
    ) : ChunkDefinition()

    @Serializable
    @SerialName("separator")
    data class Separator(val strength: Int = 1) : ChunkDefinition()

}