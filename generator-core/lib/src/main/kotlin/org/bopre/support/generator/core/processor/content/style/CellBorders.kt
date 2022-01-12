package org.bopre.support.generator.core.processor.content.style

import org.apache.poi.ss.usermodel.BorderStyle

data class CellBorders(
    val left: BorderStyle,
    val right: BorderStyle?,
    val bottom: BorderStyle?,
    val top: BorderStyle?
) {
    companion object {
        fun none(): CellBorders {
            return CellBorders(
                left = BorderStyle.NONE,
                right = BorderStyle.NONE,
                bottom = BorderStyle.NONE,
                top = BorderStyle.NONE
            )
        }

        fun builder(): CellBorderBuilder = CellBorderBuilder()
    }

    class CellBorderBuilder {
        var left: BorderStyle = BorderStyle.NONE;
        var right: BorderStyle = BorderStyle.NONE;
        var bottom: BorderStyle = BorderStyle.NONE;
        var top: BorderStyle? = BorderStyle.NONE;

        fun left(style: BorderStyle): CellBorderBuilder {
            left = style
            return this
        }

        fun right(style: BorderStyle): CellBorderBuilder {
            right = style
            return this
        }

        fun bottom(style: BorderStyle): CellBorderBuilder {
            bottom = style
            return this
        }

        fun top(style: BorderStyle): CellBorderBuilder {
            top = style
            return this
        }

        fun build(): CellBorders = CellBorders(
            left = left,
            right = right,
            bottom = bottom,
            top = top
        )
    }
}