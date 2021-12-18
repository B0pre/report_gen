package org.bopre.support.generator.core.processor.data

interface RenderProperties {
    fun getValue(key: String): String?

    companion object {
        fun empty(): RenderProperties = NoProperties()
        fun of(map: Map<String, String>) = PropertiesFromMap(map)
    }

    class NoProperties : RenderProperties {
        override fun getValue(key: String): String? = null
    }

    class PropertiesFromMap(private val map: Map<String, String>) : RenderProperties {
        override fun getValue(key: String): String? = map[key]
    }

}