package org.bopre.support.generator.core.processor

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.bopre.support.generator.core.yaml.YamlConfigurationReaderImpl
import org.bopre.support.generator.core.yaml.data.ChunkDefinition
import org.bopre.support.generator.core.yaml.data.Document
import org.bopre.support.generator.core.yaml.data.SourceDefinition
import java.io.File
import java.io.FileOutputStream

class Generators {

    companion object {
        fun fromYaml(file: File): Generator {
            val yaml: String = file.useLines { it.joinToString("\n") }
            val parsedDocument = YamlConfigurationReaderImpl().readDocument(yaml)
            return PoiGenerator(parsedDocument)
        }
    }

    sealed class Generator {
        abstract fun generateToFile(file: File)
    }

    class PoiGenerator(val doc: Document) : Generator() {
        override fun generateToFile(file: File) {
            val workBook = XSSFWorkbook()
            for (sheet in doc.sheets) {
                val sheetPOI = workBook.createSheet()
                var currentRowNum = 0;
                for (chunk in sheet.chunks) {
                    if (chunk is ChunkDefinition.Separator) {
                        for (i: Int in 1..chunk.strength) {
                            val row = sheetPOI.createRow(currentRowNum++)
                            val cell = row.createCell(0)
                            cell.setCellValue("")
                        }
                        continue
                    }
                    if (chunk is ChunkDefinition.TableDefinition) {
                        //render header
                        val header = sheetPOI.createRow(currentRowNum++)
                        var columnNum = 0
                        for (column in chunk.cols) {
                            val cell = header.createCell(columnNum++)
                            cell.setCellValue(column.title)
                        }

                        val sourceId = chunk.source
                        var lineSource: LineSource? = null
                        for (sourceDefinition in doc.sources) {
                            if (sourceId == sourceDefinition.id)
                                lineSource = resolveSource(sourceDefinition)
                        }
                        if (lineSource == null)
                            throw RuntimeException("no source found: $sourceId")

                        //render body
                        for (line in lineSource.start()) {
                            val bodyRow = sheetPOI.createRow(currentRowNum++)
                            var bodyColumnNum = 0
                            for (column in chunk.cols) {
                                val cell = bodyRow.createCell(bodyColumnNum++)
                                val cellId = column.id ?: "$bodyColumnNum"
                                cell.setCellValue(line.getCell(cellId))
                            }
                        }
                        continue
                    }
                    continue
                }
            }
            val fileOutputStream = FileOutputStream(file)
            workBook.write(fileOutputStream)
            fileOutputStream.close()
        }

        private fun resolveSource(sourceDef: SourceDefinition): LineSource {
            if (sourceDef is SourceDefinition.StaticSourceDefinition)
                return LineSource.static(sourceDef.lines.map { Line.fromMap(it) })
            return LineSource.static(emptyList())
        }
    }

}