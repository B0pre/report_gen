package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment

@Serializable
data class StyleDefinition(
    val fontSize: Short? = null,
    val font: String? = null,

    val borders: CellBordersYaml? = null,

    val alignV: VerticalAlignment? = null,
    val alignH: HorizontalAlignment? = null,

    val bold: Boolean? = null,
    val italic: Boolean? = null,
    val strikeout: Boolean? = null,

    val wrapped: Boolean? = null

)