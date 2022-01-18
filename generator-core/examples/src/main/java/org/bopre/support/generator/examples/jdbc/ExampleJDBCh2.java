package org.bopre.support.generator.examples.jdbc;

import org.bopre.support.generator.core.processor.Generators;
import org.bopre.support.generator.core.processor.data.LineSource;
import org.bopre.support.generator.core.processor.exception.GeneratorTemplateException;
import org.bopre.support.generator.core.processor.render.Generator;
import org.bopre.support.generator.core.processor.render.GeneratorTemplate;
import org.h2.jdbcx.JdbcDataSource;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ExampleJDBCh2 {

    private static final String DATABASE_LOCATION = "test";
    private static final String SEARCH_QUERY = "SELECT * FROM employee;";
    private static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS employee";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS employee(" +
            "id integer, name text, salary double, added_date timestamp" +
            ");";
    private static final String FILL_DATA_QUERY = "INSERT INTO employee(id,name,salary,added_date) VALUES " +
            "(-1, 'test', 0.0, now())," +
            "(0, 'admin', 20000.0, now())," +
            "(1, 'name 0', 200.0, now())," +
            "(2, 'name 1', 200.2512885, now())," +
            "(3, 'name 2', 2002134.0, now())," +
            "(4, 'name 3', 200.1, now())," +
            "(5, 'name 4', 200, now())," +
            "(6, 'name 5', 200, now())" +
            ";";

    public static void jdbcH2Example() throws SQLException, NamingException, URISyntaxException, GeneratorTemplateException {
        final DataSource ds = prepareDatabase();
        final LineSource jdbcSource = getSource(() -> ds, SEARCH_QUERY);

        File outputFile = new File("jdbc_h2_example.xlsx");
        URL fileLocation = ClassLoader.getSystemResource("examples/jdbc_h2_example.yaml");
        File yamlDefinition = new File(fileLocation.toURI());
        Map<String, LineSource> externalSources = new HashMap<>();
        externalSources.put("jdbc_source", jdbcSource);

        GeneratorTemplate template = Generators.fromYaml(yamlDefinition, externalSources);
        Generator instance = Generators.processTemplate(template, new HashMap<>());
        instance.renderToFile(outputFile);
    }

    public static LineSource getSource(Supplier<DataSource> dataSourceSupplier, String query) {
        return new JdbcLineSource(dataSourceSupplier, query);
    }

    private static DataSource prepareDatabase() throws NamingException, SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:" + DATABASE_LOCATION);
        ds.setUser("sa");
        ds.setPassword("sa");

        ds.getConnection().createStatement().executeUpdate(DROP_TABLE_QUERY);
        ds.getConnection().createStatement().executeUpdate(CREATE_TABLE_QUERY);
        ds.getConnection().createStatement().executeUpdate(FILL_DATA_QUERY);

        return ds;
    }

}
