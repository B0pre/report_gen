package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.ss.usermodel.CellStyle

interface StyleResolver {
    /**
     * resolve style by id
     */
    fun resolve(styleId: String): CellStyle?

    companion object {
        fun empty(): StyleResolver = object : StyleResolver {
            override fun resolve(styleId: String): CellStyle? = null
        }

        fun of(stylesMap: Map<String, CellStyle>): StyleResolver = MapBasedStyleResolver(stylesMap)

        class MapBasedStyleResolver(val stylesMap: Map<String, CellStyle>) : StyleResolver {
            override fun resolve(styleId: String): CellStyle? = stylesMap[styleId]
        }
    }

}