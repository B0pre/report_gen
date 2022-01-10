package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SourceDefinition {
    abstract val id: String

    companion object {
        fun static(id: String, mapOf: List<Map<String, String>>): SourceDefinition = StaticSourceDefinition(id, mapOf)
    }

    @Serializable
    @SerialName("static")
    data class StaticSourceDefinition(
        override val id: String, val lines: List<Map<String, String>>
    ) : SourceDefinition()

    @Serializable
    @SerialName("external")
    data class ExternalSourceDefinition(
        override val id: String, val name: String
    ) : SourceDefinition()

}
