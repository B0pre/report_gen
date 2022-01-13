package org.bopre.support.generator.core.processor.data

class ConfigurableStaticLineSource(
    private val lineTemplates: Collection<LineRender>
) : LineSource {

    companion object {
        private const val DEFAULT_CELL_VALUE: String = ""

        /**
         * construct configurable line source using cell templates
         */
        fun fromLines(
            lines: Collection<Map<String, CellTemplate>>
        ): ConfigurableStaticLineSource {
            val lineTemplates = lines.map {
                LineRenderImpl.fromCellTemplates(it)
            }.toList()
            return ConfigurableStaticLineSource(lineTemplates)
        }

        fun dynamicField(paramName: String): CellTemplate {
            return CellTemplate.Dynamic(paramName, DEFAULT_CELL_VALUE)
        }

        fun constField(value: String): CellTemplate {
            return CellTemplate.Static(value)
        }
    }

    override fun start(properties: RenderProperties): Iterable<Line> {
        return lineTemplates.map { it.createLine(properties) }.toList()
    }

    interface LineRender {
        fun createLine(props: RenderProperties): Line
    }

    class LineRenderImpl(
        private val lineDefinition: Map<String, ValueResolver>
    ) : LineRender {
        companion object {
            fun fromCellTemplates(cells: Map<String, CellTemplate>): LineRender {
                val a = cells.map {
                    it.key to it.value.cellPrepare()
                }
                    .toMap()
                return LineRenderImpl(a)
            }
        }

        override fun createLine(props: RenderProperties): Line {
            return object : Line {
                override fun getCell(name: String): String {
                    val resolver = lineDefinition.getOrDefault(name, ValueResolver.ConstValue(DEFAULT_CELL_VALUE))
                    return resolver.get(props)
                }

                override fun iterator(): Iterator<String> {
                    return lineDefinition.map { it.value }
                        .map { it.get(props) }
                        .iterator()
                }
            }
        }

    }

    fun interface ValueResolver {
        fun get(properties: RenderProperties): String

        class ConstValue(private val value: String) : ValueResolver {
            override fun get(properties: RenderProperties): String {
                return value
            }
        }

        class DynamicResolver(private val propertyName: String, private val defaultValue: String) : ValueResolver {
            override fun get(properties: RenderProperties): String {
                return propsOrDefault(properties, defaultValue)
            }

            private fun propsOrDefault(props: RenderProperties, default: String): String {
                val value = props.getValue(propertyName) ?: return default
                return value.toString()
            }
        }
    }

    sealed interface CellTemplate {

        fun cellPrepare(): ValueResolver

        class Dynamic(
            private val paramName: String,
            private val defaultValue: String
        ) : CellTemplate {
            override fun cellPrepare(): ValueResolver {
                return ValueResolver.DynamicResolver(paramName, defaultValue)
            }
        }

        class Static(
            private val value: String
        ) : CellTemplate {
            override fun cellPrepare(): ValueResolver {
                return ValueResolver.ConstValue(value)
            }
        }
    }

}