package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable

@Serializable
data class ShiftDefinition(
    val left: Int,
    val top: Int
)