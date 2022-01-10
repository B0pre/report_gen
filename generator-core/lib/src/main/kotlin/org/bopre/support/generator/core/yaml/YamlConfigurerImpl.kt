package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.content.impl.SimpleSheet
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.ConfigurableTemplate
import org.bopre.support.generator.core.processor.render.Generator
import org.bopre.support.generator.core.processor.render.GeneratorTemplate
import org.bopre.support.generator.core.processor.render.PoiDocumentRendererBuilder
import org.bopre.support.generator.core.yaml.data.SourceDefinition

class YamlConfigurerImpl(val configReader: YamlConfigurationReader) : YamlConfigurer {

    override fun configure(yaml: String, externalSources: Map<String, LineSource>): GeneratorTemplate {
        val parsedDocument = configReader.readDocument(yaml)
        val builder = PoiDocumentRendererBuilder()
        parsedDocument.sheets.forEachIndexed { index, sheetDef ->
            val contents = sheetDef.content.map { it.toContent() }.toList()
            val sheet = SimpleSheet(title = "$index", contents)
            builder.appendSheet(sheet)
        }
        for (sourceDef in parsedDocument.sources) {
            if (sourceDef is SourceDefinition.StaticSourceDefinition) {
                val source = LineSource.static(
                    sourceDef.lines.map { Line.fromMap(it) }.toList()
                )
                builder.externalSource(sourceDef.id, source)
            }
            if (sourceDef is SourceDefinition.ExternalSourceDefinition) {
                externalSources[sourceDef.name]?.let { builder.externalSource(sourceDef.id, it) };
            }
        }
        return object : GeneratorTemplate {
            override fun instance(params: Map<String, Any>): ConfigurableTemplate.Result<Generator> {
                return ConfigurableTemplate.Result.Success(
                    builder.build(
                        RenderProperties.of(params)
                    )
                )
            }
        }
    }

}
