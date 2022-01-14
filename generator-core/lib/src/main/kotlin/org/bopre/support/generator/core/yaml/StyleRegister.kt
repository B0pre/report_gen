package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.yaml.data.StyleDefinition
import org.bopre.support.generator.core.yaml.data.StyleUsage
import java.util.*

/**
 * supporting service for styles
 * provides style registration and defaults stack feature (for nested defaults settings)
 */
class StyleRegister {

    enum class StyleScope {
        HEADER,
        BODY
    }

    //registered styles (styleId <-> definition)
    private val register = HashMap<String, StyleDefinition>()

    //registered styles (definition <-> styleId)
    private val registerInvert = HashMap<StyleDefinition, String>()

    //stack with default styles
    private val currentDefaultStyleStack = HashMap<StyleScope, Stack<String>>()

    /**
     * register style definition
     */
    fun register(styleId: String, definition: StyleDefinition) {
        register[styleId] = definition
        registerInvert[definition] = styleId
    }

    /**
     * get all style definitions
     */
    fun getRegistered(): Map<String, StyleDefinition> = HashMap(register)

    /**
     * get styleId by definition if was registered yet
     */
    fun getCellStyleId(cellStyle: StyleUsage?): String? {
        return when (cellStyle) {
            is StyleUsage.InlineStyle -> {
                if (registerInvert.containsKey(cellStyle.definition))
                    return registerInvert[cellStyle.definition]
                val randomStyleId = UUID.randomUUID().toString()
                register(randomStyleId, cellStyle.definition)
                randomStyleId
            }
            is StyleUsage.DefinedStyle -> cellStyle.id
            else -> null
        }
    }

    /**
     * get styleId by definition if was registered yet
     * or try return scope default if exists
     */
    fun getCellStyleId(scope: StyleScope, cellStyle: StyleUsage?): String? {
        val ownStyle = getCellStyleId(cellStyle)
        if (ownStyle != null)
            return ownStyle
        return defaultsTop(scope)
    }

    //stack functions

    /**
     * push current default style for scope
     */
    fun defaultsPush(scope: StyleScope, styleId: String) {
        setUpScope(scope)
        currentDefaultStyleStack[scope]?.push(styleId)
    }

    /**
     * get current default style for scope
     */
    fun defaultsTop(scope: StyleScope): String? {
        setUpScope(scope)
        if (currentDefaultStyleStack[scope].isNullOrEmpty())
            return null
        return currentDefaultStyleStack[scope]?.peek()
    }

    /**
     * retrieve current default style for scope
     */
    fun defaultsPop(scope: StyleScope): String? {
        setUpScope(scope)
        if (currentDefaultStyleStack[scope].isNullOrEmpty())
            return null
        return currentDefaultStyleStack[scope]?.pop()
    }

    /**
     * Push style to stack only if it is not null
     */
    fun pushIfNotNull(scope: StyleScope, styleUsage: StyleUsage?) {
        if (styleUsage != null) {
            val styleId = getCellStyleId(styleUsage)
            if (styleId != null)
                defaultsPush(
                    scope,
                    styleId
                )
        }
    }

    /**
     * Remove stack element if style is not null
     */
    fun popIfNotNull(scope: StyleScope, styleUsage: StyleUsage?) {
        if (styleUsage != null) {
            val styleId = getCellStyleId(styleUsage)
            if (styleId != null)
                defaultsPop(scope)
        }
    }

    /**
     * create stack for required scope if not exists
     */
    private fun setUpScope(scope: StyleScope) {
        if (!currentDefaultStyleStack.containsKey(scope)) {
            currentDefaultStyleStack[scope] = Stack()
        }
    }

}