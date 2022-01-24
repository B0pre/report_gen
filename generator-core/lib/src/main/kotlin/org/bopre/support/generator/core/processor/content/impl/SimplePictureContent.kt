package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.ContentShifts
import org.bopre.support.generator.core.processor.content.PictureContent

class SimplePictureContent(
    private val relationId: String,
    private val shift: ContentShifts,
    private val widthCells: Int,
    private val heightCells: Int
) : PictureContent {
    override fun getRelationId(): String = relationId
    override fun getShift(): ContentShifts = shift
    override fun getWidthCells(): Int = widthCells
    override fun getHeight(): Int = heightCells
}