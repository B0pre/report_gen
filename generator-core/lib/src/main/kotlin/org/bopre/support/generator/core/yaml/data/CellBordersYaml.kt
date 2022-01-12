package org.bopre.support.generator.core.yaml.data

import kotlinx.serialization.Serializable
import org.apache.poi.ss.usermodel.BorderStyle

@Serializable
data class CellBordersYaml(
    val left: BorderStyle = BorderStyle.NONE,
    val right: BorderStyle = BorderStyle.NONE,
    val top: BorderStyle = BorderStyle.NONE,
    val bottom: BorderStyle = BorderStyle.NONE
)