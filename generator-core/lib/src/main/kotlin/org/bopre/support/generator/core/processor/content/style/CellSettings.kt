package org.bopre.support.generator.core.processor.content.style

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment

interface CellSettings {

    class CellSettingsBuilder {
        private var height: Short? = null
        private var borders: CellBorders? = null
        private var isWrapped: Boolean? = null
        private var verticalAlignment: VerticalAlignment? = null
        private var horizontalAlignment: HorizontalAlignment? = null
        private var isBold: Boolean? = null
        private var isItalic: Boolean? = null
        private var isStrikeout: Boolean? = null
        private var font: String? = null

        fun height(height: Short): CellSettingsBuilder {
            this.height = height
            return this
        }

        fun borders(borders: CellBorders): CellSettingsBuilder {
            this.borders = borders
            return this
        }

        fun isWrapped(isWrapped: Boolean): CellSettingsBuilder {
            this.isWrapped = isWrapped
            return this
        }

        fun verticalAlignment(verticalAlignment: VerticalAlignment): CellSettingsBuilder {
            this.verticalAlignment = verticalAlignment
            return this
        }

        fun horizontalAlignment(horizontalAlignment: HorizontalAlignment): CellSettingsBuilder {
            this.horizontalAlignment = horizontalAlignment
            return this
        }

        fun isBold(isBold: Boolean): CellSettingsBuilder {
            this.isBold = isBold
            return this
        }

        fun isItalic(isItalic: Boolean): CellSettingsBuilder {
            this.isItalic = isItalic
            return this
        }

        fun isStrikeout(isStrikeout: Boolean): CellSettingsBuilder {
            this.isStrikeout = isStrikeout
            return this
        }

        fun build(): CellSettings =
            object : CellSettings {
                override fun getHeightInPoints(): Short? = height
                override fun getBorders(): CellBorders? = borders
                override fun getIsWrapped(): Boolean? = isWrapped
                override fun getVerticalAlignment(): VerticalAlignment? = verticalAlignment
                override fun getHorizontalAlignment(): HorizontalAlignment? = horizontalAlignment
                override fun getBold(): Boolean? = isBold
                override fun getItalic(): Boolean? = isItalic
                override fun getStrikeout(): Boolean? = isStrikeout
                override fun getFontName(): String? = font
            }
    }

    companion object {
        fun create(
            height: Short? = null,
            borders: CellBorders? = null,
            isWrapped: Boolean? = null,
            verticalAlignment: VerticalAlignment? = null,
            horizontalAlignment: HorizontalAlignment? = null,
            isBold: Boolean? = null,
            isItalic: Boolean? = null,
            isStrikeout: Boolean? = null,
            font: String? = null
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
                override fun getFontName(): String? = font
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
                override fun getFontName(): String? = null
            }
        }

        fun builder(): CellSettingsBuilder = CellSettingsBuilder()
    }


    fun getHeightInPoints(): Short?
    fun getBorders(): CellBorders?

    fun getIsWrapped(): Boolean?
    fun getVerticalAlignment(): VerticalAlignment?
    fun getHorizontalAlignment(): HorizontalAlignment?

    fun getBold(): Boolean?
    fun getItalic(): Boolean?
    fun getStrikeout(): Boolean?

    fun getFontName(): String?
}
