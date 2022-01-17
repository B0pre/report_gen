package org.bopre.support.generator.examples.jdbc;

import org.bopre.support.generator.core.processor.data.Line;
import org.bopre.support.generator.core.processor.data.LineSource;
import org.bopre.support.generator.core.processor.data.RenderProperties;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class JdbcLineSource implements LineSource {

    private final Supplier<DataSource> dataSourceSupplier;
    private final String nativeSql;

    public JdbcLineSource(Supplier<DataSource> dataSourceSupplier, String nativeSql) {
        this.dataSourceSupplier = dataSourceSupplier;
        this.nativeSql = nativeSql;
    }

    @Override
    public Iterable<Line> start(RenderProperties properties) {
        try {
            return new IterableLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final class IterableLine implements Iterable<Line> {

        @NotNull
        @Override
        public Iterator<Line> iterator() {
            try {
                Connection conn = dataSourceSupplier.get().getConnection();
                ResultSet rs = conn.createStatement().executeQuery(nativeSql);
                return new IteratorLine(rs);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static final class IteratorLine implements Iterator<Line> {

        private final ResultSet resultSet;
        private Line cachedLine;
        private Boolean hasNext = true;

        private IteratorLine(ResultSet resultSet) {
            this.resultSet = resultSet;
            next();
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Line next() {
            try {
                Line prevCache = cachedLine;
                hasNext = hasNext && resultSet.next();
                Map<String, Object> lineValues = hasNext ? from(resultSet) : new HashMap<>();
                cachedLine = Line.Companion.fromMap(lineValues);
                return prevCache;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private static Map<String, Object> from(ResultSet rs) throws SQLException {
            Map<String, Object> values = new HashMap<>();
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<Integer> columnTypes = new ArrayList<>();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                columnNames.add(rs.getMetaData().getColumnName(i + 1));
                columnTypes.add(rs.getMetaData().getColumnType(i + 1));
            }
            for (int i = 0; i < columnNames.size(); i++) {
                String columnName = columnNames.get(i).toLowerCase();
                Object value = getByType(columnTypes.get(i), i + 1, rs);
                values.put(columnName, value);
            }
            return values;
        }

        private static Object getByType(int code, int columnIndex, ResultSet rs) throws SQLException {
            switch (code) {
                case Types.BIT:
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIGINT:
                    return rs.getInt(columnIndex);

                case Types.FLOAT:
                case Types.REAL:
                case Types.DOUBLE:
                case Types.NUMERIC:
                case Types.DECIMAL:
                    return rs.getDouble(columnIndex);

                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                    return rs.getString(columnIndex);

                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                case Types.TIME_WITH_TIMEZONE:
                case Types.TIMESTAMP_WITH_TIMEZONE:
                    return rs.getTimestamp(columnIndex);

                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                case Types.NULL:
                case Types.OTHER:
                case Types.JAVA_OBJECT:
                case Types.DISTINCT:
                case Types.STRUCT:
                case Types.ARRAY:
                case Types.BLOB:
                case Types.CLOB:
                case Types.REF:
                case Types.DATALINK:
                case Types.BOOLEAN:
                case Types.ROWID:
                case Types.NCHAR:
                case Types.NVARCHAR:
                case Types.LONGNVARCHAR:
                case Types.NCLOB:
                case Types.SQLXML:
                case Types.REF_CURSOR:
                default:
                    return rs.getString(columnIndex);
            }
        }

    }

}
