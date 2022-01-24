package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.handlers.StyleResolver
import java.io.InputStream

interface RenderContext {
    fun currentRow(): Int
    fun getSettings(): DocumentSettings
    fun getProperties(): RenderProperties
    fun getStyleResolver(): StyleResolver
    fun getPictureResolver(): PictureResolver

    companion object {
        interface PictureResolver {
            fun byId(imageId: String): () -> InputStream

            companion object {
                fun fromMap(map: Map<String, () -> InputStream>): PictureResolver {
                    return object : PictureResolver {
                        override fun byId(imageId: String): () -> InputStream {
                            return map.getOrDefault(imageId, { InputStream.nullInputStream() })
                        }
                    }
                }

                fun empty(): PictureResolver {
                    return object : PictureResolver {
                        override fun byId(imageId: String): () -> InputStream =
                            { InputStream.nullInputStream() }
                    }
                }
            }
        }

        class RenderContextImpl(
            private val currentRow: Int,
            private val settings: DocumentSettings,
            private val properties: RenderProperties,
            private val styleResolver: StyleResolver,
            private val pictureResolver: PictureResolver,
        ) : RenderContext {
            override fun currentRow(): Int = currentRow

            override fun getSettings(): DocumentSettings = settings

            override fun getProperties(): RenderProperties = properties
            override fun getStyleResolver(): StyleResolver = styleResolver
            override fun getPictureResolver(): PictureResolver {
                return pictureResolver
            }
        }

        fun init(
            settings: DocumentSettings,
            properties: RenderProperties,
            startRow: Int = 0,
            styleResolver: StyleResolver = StyleResolver.empty(),
            pictureResolver: PictureResolver,
        ): RenderContext =
            RenderContextImpl(
                settings = settings,
                properties = properties,
                currentRow = startRow,
                styleResolver = styleResolver,
                pictureResolver = pictureResolver
            )

        fun shiftRow(context: RenderContext, nextRowNum: Int): RenderContextImpl =
            RenderContextImpl(
                currentRow = nextRowNum,
                settings = context.getSettings(),
                properties = context.getProperties(),
                styleResolver = context.getStyleResolver(),
                pictureResolver = context.getPictureResolver()
            )
    }

}