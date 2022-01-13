package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class StyleUsage {

    @Serializable
    @SerialName("defined")
    data class DefinedStyle(val id: String) : StyleUsage()

    @Serializable
    @SerialName("inline")
    data class InlineStyle(val definition: StyleDefinition) : StyleUsage()

}