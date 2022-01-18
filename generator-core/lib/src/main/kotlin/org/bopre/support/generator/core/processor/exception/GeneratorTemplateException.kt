package org.bopre.support.generator.core.processor.exception

import org.bopre.support.generator.core.processor.render.ConfigurableTemplate
import org.bopre.support.generator.core.processor.render.Generator

class GeneratorTemplateException(val fail: ConfigurableTemplate.Result.Fail<Generator>) : Exception()