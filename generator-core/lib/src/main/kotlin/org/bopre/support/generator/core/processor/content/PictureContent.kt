package org.bopre.support.generator.core.processor.content

interface PictureContent : Content {
    fun getRelationId(): String
    fun getShift(): ContentShifts

    fun getWidthCells(): Int
    fun getHeight(): Int

}