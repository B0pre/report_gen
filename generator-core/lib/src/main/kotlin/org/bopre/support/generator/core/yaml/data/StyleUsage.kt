package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class StyleUsage {
    abstract val behaviour: StyleBehaviour

    companion object {
        val DEFAULT_STYLE_BEHAVIOUR: StyleBehaviour = StyleBehaviour.EXTEND
    }

    enum class StyleBehaviour {
        /**
         * extend globally defined style
         */
        EXTEND,

        /**
         * extend globally defined style
         */
        OVERWRITE
    }

    @Serializable
    @SerialName("defined")
    data class DefinedStyle(
        val id: String,
        override val behaviour: StyleBehaviour = DEFAULT_STYLE_BEHAVIOUR
    ) : StyleUsage()

    @Serializable
    @SerialName("inline")
    data class InlineStyle(
        val definition: StyleDefinition,
        override val behaviour: StyleBehaviour = DEFAULT_STYLE_BEHAVIOUR
    ) : StyleUsage()

}