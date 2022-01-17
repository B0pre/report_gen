package org.bopre.support.generator.core.yaml

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.bopre.support.generator.core.yaml.data.*

fun documentWithGlobalHeaderStyle(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            headerStyle = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                fontSize = 9
            ),
            StyleDefinition(
                id = "local_header_style",
                fontSize = 20
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            ),
                            CellParameters(
                                id = "col1", title = "col1", headerStyle = StyleUsage.DefinedStyle("local_header_style")
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01"),
                    mapOf("col1" to "02")
                )
            )
        )
    )
}

fun documentWithDefinedSheetHeaderStyle(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            headerStyle = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                fontSize = 9
            ),
            StyleDefinition(
                id = "sheet_header_style",
                fontSize = 13
            ),
            StyleDefinition(
                id = "local_header_style",
                fontSize = 20
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                headerStyle = StyleUsage.DefinedStyle("sheet_header_style"),
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            ),
                            CellParameters(
                                id = "col1", title = "col1", headerStyle = StyleUsage.DefinedStyle("local_header_style")
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01"),
                    mapOf("col1" to "02")
                )
            )
        )
    )
}

fun documentWithDefinedTableHeaderStyle(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            headerStyle = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                fontSize = 9
            ),
            StyleDefinition(
                id = "sheet_header_style",
                fontSize = 13
            ),
            StyleDefinition(
                id = "table_header_style",
                fontSize = 15
            ),
            StyleDefinition(
                id = "local_header_style",
                fontSize = 20
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                headerStyle = StyleUsage.DefinedStyle("sheet_header_style"),
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        headerStyle = StyleUsage.DefinedStyle("table_header_style"),
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            ),
                            CellParameters(
                                id = "col1", title = "col1", headerStyle = StyleUsage.DefinedStyle("local_header_style")
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01"),
                    mapOf("col1" to "02")
                )
            )
        )
    )
}

fun documentWithGlobalCellStyle(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            style = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                fontSize = 9
            ),
            StyleDefinition(
                id = "local_cell_style",
                fontSize = 20
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            ),
                            CellParameters(
                                id = "col1", title = "col1", style = StyleUsage.DefinedStyle("local_cell_style")
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01", "col1" to "02")
                )
            )
        )
    )
}

fun documentWithDefinedSheetCellStyle(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            style = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                fontSize = 9
            ),
            StyleDefinition(
                id = "sheet_cell_style",
                fontSize = 13
            ),
            StyleDefinition(
                id = "local_cell_style",
                fontSize = 20
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                style = StyleUsage.DefinedStyle("sheet_cell_style"),
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            ),
                            CellParameters(
                                id = "col1", title = "col1", style = StyleUsage.DefinedStyle("local_cell_style")
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01", "col1" to "02")
                )
            )
        )
    )
}

fun documentWithDefinedTableCellStyle(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            style = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                fontSize = 9
            ),
            StyleDefinition(
                id = "sheet_cell_style",
                fontSize = 13
            ),
            StyleDefinition(
                id = "table_cell_style",
                fontSize = 15
            ),
            StyleDefinition(
                id = "local_cell_style",
                fontSize = 20
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                style = StyleUsage.DefinedStyle("sheet_cell_style"),
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        style = StyleUsage.DefinedStyle("table_cell_style"),
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            ),
                            CellParameters(
                                id = "col1", title = "col1", style = StyleUsage.DefinedStyle("local_cell_style")
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01", "col1" to "02")
                )
            )
        )
    )

}

fun documentWithChangedCellSize(): Document {
    return Document(
        docname = "sample",
        globalSettings = GlobalDocumentSettings(
            style = StyleUsage.DefinedStyle("global_document")
        ),
        styles = listOf(
            StyleDefinition(
                id = "global_document",
                width = 32.0,
                height = 150.0,
                alignV = VerticalAlignment.CENTER,
                alignH = HorizontalAlignment.CENTER
            )
        ),
        sheets = listOf(
            DocumentSheet(
                id = "report_0",
                name = "report number 0",
                content = listOf(
                    ContentDefinition.TableDefinition(
                        id = "table1",
                        title = "table1 for report 0",
                        sourceId = "source_01",
                        columns = listOf(
                            CellParameters(
                                id = "col0", title = "col0"
                            )
                        )
                    )
                )
            )
        ),
        sources = listOf(
            SourceDefinition.static(
                "source_01",
                listOf(
                    mapOf("col0" to "01")
                )
            )
        )
    )

}
