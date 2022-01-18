package org.bopre.support.generator.core.processor

import org.bopre.support.generator.core.processor.exception.GeneratorTemplateException
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.render.ConfigurableTemplate
import org.bopre.support.generator.core.processor.render.Generator
import org.bopre.support.generator.core.processor.render.GeneratorTemplate
import org.bopre.support.generator.core.yaml.YamlConfigurer
import org.bopre.support.generator.core.yaml.YamlConfigurerImpl
import java.io.File

class Generators {


    companion object {
        private val yamlConfigurer: YamlConfigurer = YamlConfigurerImpl();

        /**
         * prepare generator template from yaml definition
         */
        @JvmStatic
        fun fromYaml(yamlDefinition: File, externalSources: Map<String, LineSource> = mapOf()): GeneratorTemplate {
            val yaml: String = yamlDefinition.useLines { it.joinToString("\n") }
            return yamlConfigurer.configure(yaml, externalSources)
        }

        /**
         * process generator instance with parameters
         */
        @Throws(GeneratorTemplateException::class)
        @JvmStatic
        fun processTemplate(
            template: GeneratorTemplate,
            params: Map<String, Any> = emptyMap()
        ): Generator {
            val generator = template.instance(params)
            if (generator is ConfigurableTemplate.Result.Success)
                return generator.value

            var fail = if (generator is ConfigurableTemplate.Result.Fail) generator
            else ConfigurableTemplate.Result.Fail("$generator")
            throw GeneratorTemplateException(fail)
        }

    }

}