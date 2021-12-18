package org.bopre.support.generator.core.processor.data

interface LineSource {
    companion object {
        fun static(values: Collection<Line>): LineSource = object : LineSource {
            override fun start(properties: RenderProperties): Iterable<Line> = values
        }
    }

    fun start(properties: RenderProperties): Iterable<Line>
}