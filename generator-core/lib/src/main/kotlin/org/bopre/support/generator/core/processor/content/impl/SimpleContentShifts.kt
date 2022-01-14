package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.ContentShifts

class SimpleContentShifts(
    private val left: Int,
    private val top: Int
) : ContentShifts {

    companion object {
        fun empty(): ContentShifts {
            return SimpleContentShifts(0, 0)
        }
    }

    override fun getShiftLeft(): Int = left
    override fun getShiftTop(): Int = top
}
