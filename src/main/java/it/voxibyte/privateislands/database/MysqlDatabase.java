package it.voxibyte.privateislands.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDatabase {
    private final HikariDataSource dataSource;

    public MysqlDatabase(final String url, final String username, final String password) {
        this.dataSource = createDataSource(url, username, password);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private HikariDataSource createDataSource(final String url, final String username, final String password) {
        HikariConfig datasourceConfig = new HikariConfig();
        datasourceConfig.setJdbcUrl(url);
        datasourceConfig.setUsername(username);
        datasourceConfig.setPassword(password);
        datasourceConfig.addDataSourceProperty("cachePrepStmts", "true");
        datasourceConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        datasourceConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(datasourceConfig);
    }
}
