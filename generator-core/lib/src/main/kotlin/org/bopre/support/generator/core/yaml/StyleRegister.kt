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
    private val register = HashMap<String, List<StyleDefinition>>()

    //registered styles (definition <-> styleId)
    private val registerInvert = HashMap<StyleDefinition, String>()

    //stack with default styles
    private val currentDefaultStyleStack = HashMap<StyleScope, Stack<String>>()

    /**
     * register style definition
     */
    fun register(styleId: String, definition: StyleDefinition) {
        register[styleId] = listOf(definition)
        registerInvert[definition] = styleId
    }

    /**
     * get all style definitions
     */
    fun getRegistered(): Map<String, List<StyleDefinition>> = HashMap(register)

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
    fun getCellStyleId(
        scope: StyleScope,
        cellStyle: StyleUsage?
    ): String? {
        if (cellStyle != null && cellStyle.behaviour == StyleUsage.StyleBehaviour.EXTEND)
            return extendStyle(scope, cellStyle)
        val ownStyle = getCellStyleId(cellStyle)
        if (ownStyle != null)
            return ownStyle
        return defaultsTop(scope)
    }

    private fun extendStyle(
        scope: StyleScope,
        cellStyle: StyleUsage?,
    ): String? {
        val styleId = UUID.randomUUID().toString()
        val defaultStyle = register[defaultsTop(scope)]
        val stylesSequence = mutableListOf<StyleDefinition>()

        defaultStyle?.let { stylesSequence.addAll(it) }
        val cellStyleId = getCellStyleId(cellStyle)
        if (cellStyleId != null) {
            val cellStyle = register[cellStyleId]
            if (cellStyle != null)
                stylesSequence.addAll(cellStyle)
        }

        register[styleId] = stylesSequence
        return styleId
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
            val styleId = getCellStyleId(scope, styleUsage)
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
            val styleId = getCellStyleId(scope, styleUsage)
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