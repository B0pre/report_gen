package org.bopre.support.generator.core.yaml

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

fun documentWithDefinedSheetStyle(): Document {
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

fun documentWithDefinedTableStyle(): Document {
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
