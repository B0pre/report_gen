package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.content.impl.SimpleSheet
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.data.RenderProperties
import org.bopre.support.generator.core.processor.render.ConfigurableTemplate
import org.bopre.support.generator.core.processor.render.Generator
import org.bopre.support.generator.core.processor.render.GeneratorTemplate
import org.bopre.support.generator.core.processor.render.PoiDocumentRendererBuilder

class YamlConfigurerImpl(val configReader: YamlConfigurationReader) : YamlConfigurer {

    val sourConfigurer: SourceConfigurer = SourceConfigurer()
    val contentConfigurer: ContentConfigurer = ContentConfigurer()

    override fun configure(yaml: String, externalSources: Map<String, LineSource>): GeneratorTemplate {
        val parsedDocument = configReader.readDocument(yaml)
        val builder = PoiDocumentRendererBuilder()
        parsedDocument.sheets.forEachIndexed { index, sheetDef ->
            val contents = sheetDef.content.map { contentConfigurer.configureContent(it) }.toList()
            val sheet = SimpleSheet(title = "$index", contents)
            builder.appendSheet(sheet)
        }
        for (sourceDef in parsedDocument.sources) {
            val source = sourConfigurer.configureSource(sourceDef, externalSources)
            if (source != null)
                builder.externalSource(sourceDef.id, source)
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
