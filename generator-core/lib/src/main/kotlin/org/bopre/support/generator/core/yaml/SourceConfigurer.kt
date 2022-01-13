package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.data.ConfigurableStaticLineSource
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.yaml.data.SourceDefinition

class SourceConfigurer {

    /**
     * configure source using yaml definition
     */
    fun configureSource(sourceDef: SourceDefinition, externalSources: Map<String, LineSource>): LineSource? {
        if (sourceDef is SourceDefinition.StaticSourceDefinition) {
            if (isDynamicSource(sourceDef))
                return createDynamicSource(sourceDef)
            return createSimpleStaticSource(sourceDef)
        }
        if (sourceDef is SourceDefinition.ExternalSourceDefinition) {
            return externalSources[sourceDef.name]
        }
        return null
    }

    private fun isDynamicSource(sourceDef: SourceDefinition.StaticSourceDefinition): Boolean {
        return sourceDef.lines
            .flatMap { it.values }
            .any { isVariableParameter(it) }
    }

    private fun createSimpleStaticSource(sourceDef: SourceDefinition.StaticSourceDefinition): LineSource {
        return LineSource.static(
            sourceDef.lines.map { Line.fromMap(it) }.toList()
        )
    }

    private fun createDynamicSource(sourceDef: SourceDefinition.StaticSourceDefinition): LineSource {
        val lineTemplates = sourceDef.lines.map {
            it.entries.associate { entry ->
                entry.key to createCellTemplate(entry.value)
            }
        }.toList()
        return ConfigurableStaticLineSource.fromLines(lineTemplates)
    }

    private fun createCellTemplate(value: String): ConfigurableStaticLineSource.CellTemplate =
        if (isVariableParameter(value))
            ConfigurableStaticLineSource.dynamicField(removeSymbols(value))
        else
            ConfigurableStaticLineSource.constField(value)

}