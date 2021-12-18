package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.LineSource

interface DocumentSettings {
    fun getSource(sourceId: String): LineSource
}