package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.render.GeneratorTemplate

interface YamlConfigurer {
    fun configure(yaml: String): GeneratorTemplate
}