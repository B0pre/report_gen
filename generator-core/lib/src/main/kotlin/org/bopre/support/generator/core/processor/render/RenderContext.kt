package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver

interface RenderContext {
    fun currentRow(): Int
    fun getSettings(): DocumentSettings
    fun getProperties(): RenderProperties
    fun getStyleResolver(): StyleResolver

    companion object {
        class RenderContextImpl(
            private val currentRow: Int,
            private val settings: DocumentSettings,
            private val properties: RenderProperties,
            private val styleResolver: StyleResolver
        ) : RenderContext {
            override fun currentRow(): Int = currentRow

            override fun getSettings(): DocumentSettings = settings

            override fun getProperties(): RenderProperties = properties
            override fun getStyleResolver(): StyleResolver = styleResolver
        }

        fun init(
            settings: DocumentSettings,
            properties: RenderProperties,
            startRow: Int = 0,
            styleResolver: StyleResolver = StyleResolver.empty()
        ): RenderContext =
            RenderContextImpl(
                settings = settings,
                properties = properties,
                currentRow = startRow,
                styleResolver = styleResolver
            )

        fun shiftRow(context: RenderContext, nextRowNum: Int): RenderContextImpl =
            RenderContextImpl(
                currentRow = nextRowNum,
                settings = context.getSettings(),
                properties = context.getProperties(),
                styleResolver = context.getStyleResolver()
            )
    }

}