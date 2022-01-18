package org.bopre.support.generator.examples;

import org.bopre.support.generator.core.processor.render.ConfigurableTemplate;
import org.bopre.support.generator.core.processor.render.Generator;
import org.bopre.support.generator.core.processor.Generators;
import org.bopre.support.generator.examples.jdbc.ExampleJDBCh2;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws Exception {
        //simpleStaticExample();
        ExampleJDBCh2.jdbcH2Example();
    }

    private static void simpleStaticExample() throws Exception {
        File outputFile = new File("simpleStaticExample.xlsx");
        URL fileLocation = ClassLoader.getSystemResource("examples/simple_static_example.yaml");
        File dir = new File(fileLocation.toURI());
        ConfigurableTemplate.Result<Generator> generatorTemplate = Generators.Companion.fromYaml(dir, new HashMap<>()).instance(
                new HashMap<>() {{
                    this.put("admin.user.alias", "root");
                }}
        );
        if (generatorTemplate instanceof ConfigurableTemplate.Result.Success) {
            ConfigurableTemplate.Result.Success<Generator> generator = ((ConfigurableTemplate.Result.Success) generatorTemplate);
            generator.getValue().renderToFile(outputFile);
        } else {
            throw new RuntimeException("failed prepare report generator: " + generatorTemplate);
        }
    }

}