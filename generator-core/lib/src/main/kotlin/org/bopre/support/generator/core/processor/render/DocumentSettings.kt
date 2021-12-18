package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.LineSource

interface DocumentSettings {
    fun getSource(sourceId: String): LineSource
}