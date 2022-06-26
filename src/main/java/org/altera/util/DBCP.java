package org.altera.util;

import com.typesafe.config.Config;
import org.altera.common.Env;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * LazyHolder DBCP
 */
public class DBCP {

    public static Connection getConnection() throws SQLException {
        return LazyHolder.instance.dbcp.getConnection();
    }

    // =========================================

    private final BasicDataSource dbcp;

    private DBCP() {
        dbcp = new BasicDataSource();
        Config conf = Env.getConfig("application.conf");

        dbcp.setDriverClassName(conf.getString("db.driver"));
        dbcp.setUrl(conf.getString("db.url"));
        dbcp.setUsername(conf.getString("db.username"));
        dbcp.setPassword(conf.getString("db.password"));
    }

    private static class LazyHolder {
        private static final DBCP instance = new DBCP();
    }
}
