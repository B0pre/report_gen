package org.bopre.support.generator.core.processor.render

import java.io.File

sealed class Generator {
    abstract fun renderToFile(file: File)
}