package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.ContentShifts
import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleContentShifts
import org.bopre.support.generator.core.processor.content.impl.SimpleSeparatorContent
import org.bopre.support.generator.core.processor.content.impl.SimpleTableColumn
import org.bopre.support.generator.core.processor.content.impl.SimpleTableContent
import org.bopre.support.generator.core.yaml.data.CellParameters
import org.bopre.support.generator.core.yaml.data.ContentDefinition
import org.bopre.support.generator.core.yaml.data.ShiftDefinition
import org.bopre.support.generator.core.yaml.data.StyleUsage
import java.util.*

class ContentConfigurer {

    fun configureContent(
        definition: ContentDefinition,
        styleRegister: StyleRegister
    ): Content {
        return when (definition) {
            is ContentDefinition.TableDefinition ->
                table(definition, styleRegister)
            is ContentDefinition.Separator ->
                separator(definition)
        }
    }

    private fun table(
        tableDefinition: ContentDefinition.TableDefinition,
        styleRegister: StyleRegister
    ): Content =
        SimpleTableContent(
            sourceId = tableDefinition.sourceId,
            shifts = prepareShifts(tableDefinition.shift),
            columns = tableDefinition.columns
                .mapIndexed { index, cellParameters -> toColumn(cellParameters, index, styleRegister) }
                .toList()
        )

    private fun prepareShifts(shiftDefinition: ShiftDefinition?): ContentShifts {
        if (shiftDefinition == null)
            return SimpleContentShifts.empty()
        return SimpleContentShifts(left = shiftDefinition.left, top = shiftDefinition.top)
    }

    private fun separator(separatorDefinition: ContentDefinition.Separator): Content =
        SimpleSeparatorContent(separatorDefinition.strength)

    private fun toColumn(
        cell: CellParameters, index: Int,
        styleRegister: StyleRegister
    ): TableColumn {
        val colTitle = cell.title ?: "$index"
        val colId = cell.id ?: "$index"
        val styleId = getCellStyleId(cell, styleRegister)
        if (styleId != null)
            return SimpleTableColumn(title = colTitle, id = colId, styleId = styleId)
        return SimpleTableColumn(title = colTitle, id = colId)
    }

    private fun getCellStyleId(cell: CellParameters, register: StyleRegister): String? {
        val cellStyle = cell.style
        return when (cellStyle) {
            is StyleUsage.InlineStyle -> {
                val randomStyleId = UUID.randomUUID().toString()
                register.register(randomStyleId, cellStyle.definition)
                randomStyleId
            }
            is StyleUsage.DefinedStyle -> cellStyle.id
            else -> null
        }
    }

}