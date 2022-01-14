package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable

@Serializable
data class DocumentSheet(
    val id: String?,
    val name: String? = null,
    val headerStyle: StyleUsage? = null,
    val content: List<ContentDefinition> = emptyList()
)