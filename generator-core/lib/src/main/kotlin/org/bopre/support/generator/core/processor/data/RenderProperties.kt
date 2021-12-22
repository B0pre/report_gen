package org.bopre.support.generator.core.processor.data

interface RenderProperties {
    fun getValue(key: String): Any?

    companion object {
        fun empty(): RenderProperties = NoProperties()
        fun of(map: Map<String, Any>) = PropertiesFromMap(map)
    }

    class NoProperties : RenderProperties {
        override fun getValue(key: String): Any? = null
    }

    class PropertiesFromMap(private val map: Map<String, Any>) : RenderProperties {
        override fun getValue(key: String): Any? = map[key]
    }

}