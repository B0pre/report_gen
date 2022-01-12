package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable

@Serializable
data class StyleDefinition(
    val fontSize: Short? = null,
    val borders: CellBordersYaml? = null
)