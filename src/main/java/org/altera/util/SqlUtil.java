package org.altera.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SqlUtil {
    private static final Logger logger = LoggerFactory.getLogger(SqlUtil.class);

    public static <T> List<T> select(String sql, Function<ResultSet, Optional<T>> bind, Pair<String, Object>... pairs) throws SQLException {
        return selectInner(buildSql(sql, pairs), bind);
    }

    public static <T> List<T> selectFile(String filepath, Function<ResultSet, Optional<T>> bind, Pair<String, Object>... pairs) throws IOException, SQLException {
        String sql = buildSql(FileUtil.readSql(filepath), pairs);
        return selectInner(sql, bind);
    }

    public static <T> int insertInto(String dstTable, String[] cols, List<T> rows, Consumer<T> func) throws SQLException {
        String columns = String.join(", ", cols);
        String values = Arrays.stream(cols).map(r -> "?").collect(Collectors.joining(", "));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", dstTable, columns, values);
        logger.info("query: {}", sql);

        Connection conn = null;
        int affectedRows = 0;
        try {
            conn = DBCP.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (T row : rows) {
                func.accept(row);
                pstmt.executeUpdate();
                pstmt.clearParameters();
                affectedRows += 1;
            }
        } finally {
            if (conn != null) conn.close();
        }
        return affectedRows;
    }

    public static <T> Function<ResultSet, Optional<T>> uncheck(UncheckFunction<ResultSet, T, SQLException> callable) {
        return rs -> {
            T row = null;
            try {
                row = callable.call(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.ofNullable(row);
        };
    }

    public static <T> BiConsumer<PreparedStatement, T> uncheck(UncheckBiConsumer<PreparedStatement, T, SQLException> callable) {
        return (pstmt, row) -> {
            try {
                callable.call(pstmt, row);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    }

    // ====================================================================

    private static <T> List<T> selectInner(String sql, Function<ResultSet, Optional<T>> bind) throws SQLException {
        Connection conn = DBCP.getConnection();
        List<T> rows = new ArrayList<>();

        try {
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                T row = bind.apply(rs).orElseThrow(SQLException::new);
                rows.add(row);
            }
        } finally {
            if (conn != null) conn.close();
        }
        return rows;
    }

    private static String buildSql(String sql, Pair<String, Object>[] pairs) {
        for (Pair<String, Object> e : pairs) {
            String value = e.getValue().toString();
            if (e.getValue() instanceof String) {
                value = String.format("'%s'", value);
            }
            sql = sql.replaceAll(String.format("[$]\\{%s\\}", e.getKey()), value);
        }
        sql = sql.replace(";", StringUtils.EMPTY);
        logger.info("query: {}", sql);
        return sql;
    }
}
