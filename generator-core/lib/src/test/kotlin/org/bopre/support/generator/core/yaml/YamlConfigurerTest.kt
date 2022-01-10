package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.render.ConfigurableTemplate
import org.bopre.support.generator.core.processor.render.Generator
import org.bopre.support.generator.core.testutils.xls.assertSheetInFile
import org.bopre.support.generator.core.yaml.data.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertTrue

class YamlConfigurerTest {

    lateinit var configurer: YamlConfigurer
    lateinit var configurationReader: YamlConfigurationReader

    @BeforeEach
    fun beforeEach() {
        configurationReader = Mockito.mock(YamlConfigurationReader::class.java)
        configurer = YamlConfigurerImpl(configurationReader)
    }

    @Test
    fun `yaml config external sources`() {
        val document = Document(
            docname = "sample",
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
                                CellParameters(id = "id", title = "identifier"),
                                CellParameters(id = "name", title = "username")
                            )
                        )
                    )
                )
            ),
            sources = listOf(
                SourceDefinition.ExternalSourceDefinition(
                    "source_01",
                    name = "external_source"
                )
            )
        )

        val source: LineSource = LineSource.static(
            listOf(
                Line.fromMap(mapOf("id" to "00", "name" to "u_00")),
                Line.fromMap(mapOf("id" to "11", "name" to "u_11"))
            )
        )

        val yamlContentStub = "some yaml content";
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        Mockito.`when`(configurationReader.readDocument(yamlContentStub))
            .thenReturn(document)

        val generator: Generator = (
                configurer.configure(
                    yaml = yamlContentStub,
                    externalSources = mapOf(
                        "external_source" to source
                    )
                )
                    .instance() as ConfigurableTemplate.Result.Success
                ).value
        generator.renderToFile(file);
        assertTrue(file.exists(), "file was not created")
        assertSheetInFile(
            file, 0, arrayOf(
                arrayOf("identifier", "username"),
                arrayOf("00", "u_00"),
                arrayOf("11", "u_11"),
            )
        )
    }

    @Test
    fun `yaml config static sources`() {

        val document = Document(
            docname = "sample",
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
                                CellParameters(id = "id", title = "identifier"),
                                CellParameters(id = "name", title = "username")
                            )
                        )
                    )
                )
            ),
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

        val yamlContentStub = "some yaml content";
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        Mockito.`when`(configurationReader.readDocument(yamlContentStub))
            .thenReturn(document)

        val generator: Generator = (
                configurer.configure(yamlContentStub)
                    .instance() as ConfigurableTemplate.Result.Success
                ).value;
        generator.renderToFile(file);
        assertTrue(file.exists(), "file was not created")
        assertSheetInFile(
            file, 0, arrayOf(
                arrayOf("identifier", "username"),
                arrayOf("01", "user_01"),
                arrayOf("02", "user_02"),
                arrayOf("03", "user_03")
            )
        )
    }

}