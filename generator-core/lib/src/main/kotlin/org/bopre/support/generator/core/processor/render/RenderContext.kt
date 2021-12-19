package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.RenderProperties

interface RenderContext {
    fun currentRow(): Int
    fun getSettings(): DocumentSettings
    fun getProperties(): RenderProperties

    companion object {
        class RenderContextImpl(
            private val currentRow: Int,
            private val settings: DocumentSettings,
            private val properties: RenderProperties
        ) : RenderContext {
            override fun currentRow(): Int = currentRow

            override fun getSettings(): DocumentSettings = settings

            override fun getProperties(): RenderProperties = properties
        }

        fun init(
            settings: DocumentSettings,
            properties: RenderProperties,
            startRow: Int = 0
        ): RenderContext =
            RenderContextImpl(
                settings = settings,
                properties = properties,
                currentRow = startRow
            )

        fun shiftRow(context: RenderContext, nextRowNum: Int): RenderContextImpl =
            RenderContextImpl(
                currentRow = nextRowNum,
                settings = context.getSettings(),
                properties = context.getProperties()
            )
    }

}