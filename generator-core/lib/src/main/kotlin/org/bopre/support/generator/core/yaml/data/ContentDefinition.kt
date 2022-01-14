package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ContentDefinition {
    @Serializable
    @SerialName("table")
    data class TableDefinition(
        val id: String,
        val title: String?,
        val sourceId: String,
        val style: StyleUsage? = null,
        val headerStyle: StyleUsage? = null,
        val shift: ShiftDefinition? = null,
        val showHeader: Boolean = true,
        val columns: List<CellParameters> = emptyList()
    ) : ContentDefinition()

    @Serializable
    @SerialName("separator")
    data class Separator(val strength: Int = 1) : ContentDefinition()

}