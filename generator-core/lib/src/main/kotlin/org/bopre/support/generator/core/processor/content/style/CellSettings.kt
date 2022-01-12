package org.bopre.support.generator.core.processor.content.style

interface CellSettings {

    companion object {
        fun create(height: Short): CellSettings {
            return object : CellSettings {
                override fun getHeightInPoints(): Short {
                    return height
                }
            }
        }

        fun empty(): CellSettings {
            return object : CellSettings {
                override fun getHeightInPoints(): Short? {
                    return null
                }
            }
        }
    }

    fun getHeightInPoints(): Short?

}
