package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable

@Serializable
data class GlobalDocumentSettings(
    val headerStyle: StyleUsage? = null,
    val style: StyleUsage? = null
)