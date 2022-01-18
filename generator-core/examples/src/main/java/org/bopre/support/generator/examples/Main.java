package org.bopre.support.generator.examples;

import org.bopre.support.generator.core.processor.Generators;
import org.bopre.support.generator.core.processor.exception.GeneratorTemplateException;
import org.bopre.support.generator.core.processor.render.Generator;
import org.bopre.support.generator.core.processor.render.GeneratorTemplate;
import org.bopre.support.generator.examples.jdbc.ExampleJDBCh2;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        //simpleStaticExample();
        ExampleJDBCh2.jdbcH2Example();
    }

    //private static void simpleStaticExample() throws Exception {
    private static void simpleStaticExample() throws GeneratorTemplateException, URISyntaxException {
        File outputFile = new File("simpleStaticExample.xlsx");
        URL fileLocation = ClassLoader.getSystemResource("examples/simple_static_example.yaml");
        File yamlDefinition = new File(fileLocation.toURI());

        GeneratorTemplate template = Generators.fromYaml(yamlDefinition, new HashMap<>());
        Generator instance = Generators.processTemplate(template, new HashMap<>() {
                    {
                        this.put("admin.user.alias", "root");
                    }
                }
        );
        instance.renderToFile(outputFile);
    }

}