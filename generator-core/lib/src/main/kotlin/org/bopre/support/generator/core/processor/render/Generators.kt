package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.content.impl.SimpleSheet
import org.bopre.support.generator.core.yaml.YamlConfigurationReaderImpl
import org.bopre.support.generator.core.yaml.data.SourceDefinition
import java.io.File

class Generators {

    companion object {
        fun fromYaml(file: File): Generator {
            val yaml: String = file.useLines { it.joinToString("\n") }
            val parsedDocument = YamlConfigurationReaderImpl().readDocument(yaml)
            val builder = PoiDocumentRendererBuilder()
            parsedDocument.sheets.forEachIndexed { index, sheetDef ->
                val contents = sheetDef.chunks.map { it.toContent() }.toList()
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
            }
            return builder.build()
        }
    }

}