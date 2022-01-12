package org.bopre.support.generator.core.processor.content.style

interface CellSettings {

    companion object {
        fun create(height: Short?, borders: CellBorders? = null): CellSettings {
            return object : CellSettings {
                override fun getHeightInPoints(): Short? = height
                override fun getBorders(): CellBorders? = borders

            }
        }

        fun empty(): CellSettings {
            return object : CellSettings {
                override fun getHeightInPoints(): Short? = null
                override fun getBorders(): CellBorders? = null
            }
        }
    }

    fun getHeightInPoints(): Short?
    fun getBorders(): CellBorders?

}
