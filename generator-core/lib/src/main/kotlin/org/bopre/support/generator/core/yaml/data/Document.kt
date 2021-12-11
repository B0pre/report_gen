package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val docname: String,
    val sheets: List<DocumentSheet> = emptyList(),
    val sources: List<SourceDefinition> = emptyList()
)