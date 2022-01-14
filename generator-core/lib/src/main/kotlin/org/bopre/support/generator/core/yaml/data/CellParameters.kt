package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable

@Serializable
data class CellParameters(
    val id: String?,
    val title: String?,
    val headerStyle: StyleUsage? = null,
    val style: StyleUsage? = null
)