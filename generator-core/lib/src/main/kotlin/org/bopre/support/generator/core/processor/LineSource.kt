package org.bopre.support.generator.core.processor

interface LineSource : Iterable<Line> {
    companion object {
        fun static(values: Collection<Line>): LineSource = object : LineSource {
            override fun iterator(): Iterator<Line> =
                values.iterator()
        }
    }
}