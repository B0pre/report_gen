package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.yaml.YamlConfigurationReaderImpl
import org.bopre.support.generator.core.yaml.YamlConfigurer
import org.bopre.support.generator.core.yaml.YamlConfigurerImpl
import java.io.File

class Generators {


    companion object {
        private val yamlConfigurer: YamlConfigurer = YamlConfigurerImpl(YamlConfigurationReaderImpl());

        fun fromYaml(file: File, externalSources: Map<String, LineSource> = mapOf()): GeneratorTemplate {
            val yaml: String = file.useLines { it.joinToString("\n") }
            return yamlConfigurer.configure(yaml)
        }
    }

}