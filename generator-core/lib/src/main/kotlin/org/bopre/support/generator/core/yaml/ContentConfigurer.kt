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

class ContentConfigurer {

    companion object {
        private const val CELL_WIDTH_MULTIPLIER: Int = 256
        private const val CELL_HEIGHT_MULTIPLIER: Int = 20
    }

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
    ): Content {

        //push table`s style to stack
        styleRegister.pushIfNotNull(StyleRegister.StyleScope.HEADER, tableDefinition.headerStyle)
        styleRegister.pushIfNotNull(StyleRegister.StyleScope.BODY, tableDefinition.style)
        val content = SimpleTableContent(
            sourceId = tableDefinition.sourceId,
            shifts = prepareShifts(tableDefinition.shift),
            showHeader = tableDefinition.showHeader,
            columns = tableDefinition.columns
                .mapIndexed { index, cellParameters -> toColumn(cellParameters, index, styleRegister) }
                .toList()
        )

        //remove table`s style from stack
        styleRegister.popIfNotNull(StyleRegister.StyleScope.HEADER, tableDefinition.headerStyle)
        styleRegister.popIfNotNull(StyleRegister.StyleScope.BODY, tableDefinition.style)
        return content
    }

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

        val columnBuilder = SimpleTableColumn.SimpleTableColumnBuilder(
            title = colTitle,
            id = colId
        )

        //define cell`s style
        val styleId = styleRegister.getCellStyleId(StyleRegister.StyleScope.BODY, cell.style)
        if (styleId != null) {
            columnBuilder.styleId(styleId)
            styleRegister.getRegistered()[styleId]?.let {
                val height = it.height;
                val width = it.width
                if (height != null) {
                    columnBuilder.height(height = (height * CELL_HEIGHT_MULTIPLIER).toInt())
                }
                if (width != null) {
                    columnBuilder.width(width = (width * CELL_WIDTH_MULTIPLIER).toInt())
                }
            }
        }

        //define header`s style
        val headerStyleId = styleRegister.getCellStyleId(StyleRegister.StyleScope.HEADER, cell.headerStyle)
        if (headerStyleId != null)
            columnBuilder.headerStyleId(headerStyleId)

        return columnBuilder.build()
    }

}