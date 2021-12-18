package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.RenderProperties
import java.io.File

sealed class Generator {
    abstract fun renderToFile(file: File, properties: RenderProperties)
}