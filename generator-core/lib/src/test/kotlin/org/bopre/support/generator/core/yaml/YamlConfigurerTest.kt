package org.bopre.support.generator.core.yaml

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.bopre.support.generator.core.processor.data.Line
import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.render.ConfigurableTemplate
import org.bopre.support.generator.core.processor.render.Generator
import org.bopre.support.generator.core.testutils.xls.*
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.*
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.CellBordersAssertion.BorderLocation
import org.bopre.support.generator.core.testutils.xls.CellStyleAssertion.CellFontSettingsAssertion.AssertFontType
import org.bopre.support.generator.core.yaml.data.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Named
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import java.io.File
import java.util.stream.Stream
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

    @Test
    fun `yaml config cell with font size`() {
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
                                CellParameters(
                                    id = "id",
                                    title = "identifier",
                                    style = StyleUsage.InlineStyle(StyleDefinition(fontSize = 20))
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
                        mapOf("id" to "01"),
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
        assertCellStyles(
            file, 0, listOf(
                GenericCell(1, 0, CellStyleAssertion.CellFontHeightAssertion(20))
            )
        )
    }

    @Test
    fun `yaml config with dynamic source fields`() {
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
                                CellParameters(
                                    id = "id", title = "identifier"
                                ),
                                CellParameters(
                                    id = "username", title = "username"
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
                        mapOf("id" to "01", "username" to "\${users.user01}"),
                        mapOf("id" to "02", "username" to "\${users.user02}"),
                        mapOf("id" to "admin", "username" to "root")
                    )
                )
            )
        )

        val yamlContentStub = "some yaml content";
        val file = kotlin.io.path.createTempFile(suffix = ".xlsx").toFile()
        Mockito.`when`(configurationReader.readDocument(yamlContentStub))
            .thenReturn(document)

        val props = mapOf(
            "users.user01" to "name of user 01",
            "users.user02" to "name of user 02"
        )
        val generator: Generator = (
                configurer.configure(yamlContentStub)
                    .instance(props) as ConfigurableTemplate.Result.Success
                ).value;
        generator.renderToFile(file);

        assertTrue(file.exists(), "file was not created")
        assertSheetInFile(
            file, 0, arrayOf(
                arrayOf("identifier", "username"),
                arrayOf("01", "name of user 01"),
                arrayOf("02", "name of user 02"),
                arrayOf("admin", "root")
            )
        )
    }

    @Test
    fun `yaml config cell with borders`() {
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
                                CellParameters(
                                    id = "id", title = "identifier", style = StyleUsage.InlineStyle(
                                        StyleDefinition(
                                            fontSize = 20,
                                            font = "Times New Roman",
                                            borders = CellBordersYaml(
                                                left = BorderStyle.HAIR,
                                                right = BorderStyle.MEDIUM,
                                                top = BorderStyle.MEDIUM_DASHED,
                                                bottom = BorderStyle.THIN
                                            ),
                                            alignV = VerticalAlignment.CENTER,
                                            alignH = HorizontalAlignment.CENTER,
                                            wrapped = true,
                                            bold = true,
                                            italic = true,
                                            strikeout = true,
                                            format = "0.00"
                                        )
                                    )
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
                        mapOf("id" to "01"),
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
        assertCellStyles(
            file, 0, listOf(
                GenericCell(0, 0, CellBordersAssertion(BorderLocation.LEFT, BorderStyle.NONE)),
                GenericCell(0, 0, CellBordersAssertion(BorderLocation.RIGHT, BorderStyle.NONE)),
                GenericCell(0, 0, CellBordersAssertion(BorderLocation.TOP, BorderStyle.NONE)),
                GenericCell(0, 0, CellBordersAssertion(BorderLocation.BOTTOM, BorderStyle.NONE)),

                GenericCell(1, 0, CellBordersAssertion(BorderLocation.LEFT, BorderStyle.HAIR)),
                GenericCell(1, 0, CellBordersAssertion(BorderLocation.RIGHT, BorderStyle.MEDIUM)),
                GenericCell(1, 0, CellBordersAssertion(BorderLocation.TOP, BorderStyle.MEDIUM_DASHED)),
                GenericCell(1, 0, CellBordersAssertion(BorderLocation.BOTTOM, BorderStyle.THIN)),

                GenericCell(1, 0, CellVerticalAlignmentAssertion(VerticalAlignment.CENTER)),
                GenericCell(1, 0, CellHorizontalAlignmentAssertion(HorizontalAlignment.CENTER)),
                GenericCell(1, 0, CellIsWrappedAssertion(true)),
                GenericCell(1, 0, CellFontSettingsAssertion(AssertFontType.BOLD, true)),
                GenericCell(1, 0, CellFontSettingsAssertion(AssertFontType.ITALIC, true)),
                GenericCell(1, 0, CellFontSettingsAssertion(AssertFontType.STRIKEOUT, true)),

                GenericCell(1, 0, CellFontNameAlignmentAssertion("Times New Roman")),

                GenericCell(1, 0, CellDataFormatAssertion("0.00")),
            )
        )
    }

    @Test
    fun `yaml config cell defining styles`() {
        val document = Document(
            docname = "sample",
            styles = listOf(
                StyleDefinition(
                    id = "additional_style",
                    fontSize = 20,
                    font = "Times New Roman",
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
                                    id = "id", title = "identifier", style = StyleUsage.DefinedStyle(
                                        id = "additional_style"
                                    )
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
                        mapOf("id" to "01"),
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
        assertCellStyles(
            file, 0, listOf(
                GenericCell(1, 0, CellFontNameAlignmentAssertion("Times New Roman")),
                GenericCell(1, 0, CellFontHeightAssertion(20)),
            )
        )
    }

    @Test
    fun `yaml config cell with shifts`() {
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
                            shift = ShiftDefinition(2, 1),
                            columns = listOf(
                                CellParameters(
                                    id = "id", title = "id"
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
                        mapOf("id" to "01"),
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
            file, 0,
            arrayOf(
                arrayOf("", "", ""),
                arrayOf("", "", "id"),
                arrayOf("", "", "01")
            )
        )
    }

    @Test
    fun `yaml config test sheet names`() {
        val document = Document(
            docname = "sample",
            sheets = listOf(
                DocumentSheet(
                    id = "report_0",
                    name = "report 0",
                    content = emptyList()
                ),
                DocumentSheet(
                    id = "report_1",
                    name = "report 1",
                    content = emptyList()
                ),
                DocumentSheet(
                    id = "report_2",
                    content = emptyList()
                )
            ),
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
        assertSheetNameInFile(file, 0, "report 0")
        assertSheetNameInFile(file, 1, "report 1")
        assertSheetNameInFile(file, 2, "sheet#2")
    }

    @Test
    fun `yaml config cell hide headers`() {
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
                            showHeader = false,
                            columns = listOf(
                                CellParameters(
                                    id = "id", title = "id"
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
                        mapOf("id" to "01"),
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
            file, 0,
            arrayOf(
                arrayOf("01")
            )
        )
    }

    @MethodSource(value = ["stylesTestCases"])
    @ParameterizedTest(name = "{index}: {0}")
    fun `yaml congig general style test`(
        document: Document,
        message: String,
        executables: List<(file: File) -> () -> Unit>
    ) {
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
        assertAll(
            message,
            executables.map { it.invoke(file) }.toList()
        );
    }

    companion object {
        @JvmStatic
        fun stylesTestCases(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    Named.of("documentWithGlobalHeaderStyle", documentWithGlobalHeaderStyle()),
                    "documentWithGlobalHeaderStyle",
                    listOf { file: File ->
                        {
                            assertCellStyles(
                                file,
                                0,
                                GenericCell(0, 0, CellFontHeightAssertion(9)), //document default
                                GenericCell(0, 1, CellFontHeightAssertion(20)) //column own style
                            )
                        }
                    }
                ),
                //====
                Arguments.of(
                    Named.of("documentWithDefinedSheetStyle", documentWithDefinedSheetStyle()),
                    "documentWithDefinedSheetStyle",
                    listOf { file: File ->
                        {
                            assertCellStyles(
                                file,
                                0,
                                GenericCell(0, 0, CellFontHeightAssertion(13)), //sheet default
                                GenericCell(0, 1, CellFontHeightAssertion(20)) //column own style
                            )
                        }
                    }
                ),
                //====
                Arguments.of(
                    Named.of("documentWithDefinedTableStyle", documentWithDefinedTableStyle()),
                    "documentWithDefinedTableStyle",
                    listOf { file: File ->
                        {
                            assertCellStyles(
                                file,
                                0,
                                GenericCell(0, 0, CellFontHeightAssertion(15)), //table default
                                GenericCell(0, 1, CellFontHeightAssertion(20)) //column own style
                            )
                        }
                    }
                ),
            )
        }

    }

}
