package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.yaml.data.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class YamlConfigurationReaderImplTest {

    lateinit var reader: YamlConfigurationReader

    @Before
    fun beforeEach() {
        reader = YamlConfigurationReaderImpl()
    }

    @Test
    fun `yaml config static sources`() {
        val input = """
            docname: test
            sources:
                - type: static
                  id: source_01
                  lines:
                    - id: 01
                      name: user_01
                    - id: 02
                      name: user_02
                    - id: 03
                      name: user_03
            """

        val expected = Document(
            docname = "test",
            sources = listOf(
                SourceDefinition.static(
                    "source_01",
                    listOf(
                        mapOf("id" to "01", "name" to "user_01"),
                        mapOf("id" to "02", "name" to "user_02"),
                        mapOf("id" to "03", "name" to "user_03")
                    )
                )
            )
        )

        val actual = reader.readDocument(input)
        assertEquals(expected, actual, "wrong deserialization")
    }

    @Test
    fun `read sample yaml configuration`() {
        val input = """
            docname: sample document
            sheets:
                - id: report_0
                  name: report number 0
                  chunks:
                    - type: table
                      source: source_id
                      id: table1
                      title: table1 for report 0
                      cols:
                        - id: id
                          title: identifier
                        - id: name
                          title: username
                    - type: separator
                      strength: 23
            """

        val expected = Document(
            docname = "sample document",
            sheets = listOf(
                DocumentSheet(
                    id = "report_0",
                    name = "report number 0",
                    chunks = listOf(
                        ChunkDefinition.TableDefinition(
                            id = "table1",
                            title = "table1 for report 0",
                            source = "source_id",
                            cols = listOf(
                                CellParameters(id = "id", title = "identifier"),
                                CellParameters(id = "name", title = "username")
                            )
                        ),
                        ChunkDefinition.Separator(23)
                    )
                )
            ),
        )

        val actual = reader.readDocument(input)
        assertEquals(expected, actual, "wrong deserialization")
    }

}

