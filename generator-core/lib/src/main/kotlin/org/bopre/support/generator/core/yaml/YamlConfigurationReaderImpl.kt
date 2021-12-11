package org.bopre.support.generator.core.yaml

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import org.bopre.support.generator.core.yaml.data.Document

class YamlConfigurationReaderImpl : YamlConfigurationReader {

    override fun readDocument(input: String): Document =
        with(Yaml(configuration = YamlConfiguration(polymorphismStyle = PolymorphismStyle.Property))) {
            decodeFromString(Document.serializer(), input.trimIndent())
        }

}