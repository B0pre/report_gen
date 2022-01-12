package org.bopre.support.generator.core.processor.content.style

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment

interface CellSettings {

    companion object {
        fun create(
            height: Short? = null,
            borders: CellBorders? = null,
            isWrapped: Boolean? = null,
            verticalAlignment: VerticalAlignment? = null,
            horizontalAlignment: HorizontalAlignment? = null,
            isBold: Boolean? = null,
            isItalic: Boolean? = null,
            isStrikeout: Boolean? = null
        ): CellSettings {
            return object : CellSettings {
                override fun getHeightInPoints(): Short? = height
                override fun getBorders(): CellBorders? = borders
                override fun getIsWrapped(): Boolean? = isWrapped
                override fun getVerticalAlignment(): VerticalAlignment? = verticalAlignment
                override fun getHorizontalAlignment(): HorizontalAlignment? = horizontalAlignment
                override fun getBold(): Boolean? = isBold
                override fun getItalic(): Boolean? = isItalic
                override fun getStrikeout(): Boolean? = isStrikeout
            }
        }

        fun empty(): CellSettings {
            return object : CellSettings {
                override fun getHeightInPoints(): Short? = null
                override fun getBorders(): CellBorders? = null
                override fun getIsWrapped(): Boolean? = null
                override fun getVerticalAlignment(): VerticalAlignment? = null
                override fun getHorizontalAlignment(): HorizontalAlignment? = null
                override fun getBold(): Boolean? = null
                override fun getItalic(): Boolean? = null
                override fun getStrikeout(): Boolean? = null
            }
        }
    }


    fun getHeightInPoints(): Short?
    fun getBorders(): CellBorders?

    fun getIsWrapped(): Boolean?
    fun getVerticalAlignment(): VerticalAlignment?
    fun getHorizontalAlignment(): HorizontalAlignment?

    fun getBold(): Boolean?
    fun getItalic(): Boolean?
    fun getStrikeout(): Boolean?

}
