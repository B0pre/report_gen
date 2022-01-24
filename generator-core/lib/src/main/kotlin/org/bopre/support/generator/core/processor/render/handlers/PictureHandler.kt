package org.bopre.support.generator.core.processor.render.handlers

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.util.Units.EMU_PER_PIXEL
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.bopre.support.generator.core.processor.content.Content
import org.bopre.support.generator.core.processor.content.PictureContent
import org.bopre.support.generator.core.processor.render.RenderContext

class PictureHandler : ContentHandler {
    override fun handleContent(sheet: XSSFSheet, content: Content, context: RenderContext): Int {
        val rowStart = context.currentRow()

        var currentRowNum = rowStart
        if (content is PictureContent) {
            val anchor = sheet.workbook.creationHelper.createClientAnchor()
            currentRowNum += content.getShift().getShiftTop()
            anchor.setCol1(content.getShift().getShiftLeft())
            anchor.setRow1(currentRowNum)

            currentRowNum += content.getHeight()
            //anchor.setCol2(content.getShift().getShiftLeft() + content.getWidthCells())
            //anchor.setRow2(currentRowNum)

            val picIdx = sheet.workbook.addPicture(context.getPictureResolver().byId(content.getRelationId()).invoke(),
                Workbook.PICTURE_TYPE_JPEG)

            val drawningPatriarch = sheet.createDrawingPatriarch()
            val pict = drawningPatriarch.createPicture(anchor, picIdx)

            pict.setLineWidth(256.0 * 100)
            pict.resize()

            currentRowNum += 1
            return currentRowNum
        } else return rowStart
    }

    override fun supports(content: Content): Boolean {
        return content is PictureContent
    }
}