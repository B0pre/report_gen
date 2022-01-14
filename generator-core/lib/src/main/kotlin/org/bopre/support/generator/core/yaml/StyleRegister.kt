package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.yaml.data.StyleDefinition

class StyleRegister {

    private val register = HashMap<String, StyleDefinition>()

    fun register(styleId: String, definition: StyleDefinition) {
        register[styleId] = definition
    }

    fun getRegistered(): Map<String, StyleDefinition> = HashMap(register)

}