package com.example;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.Connection;

public abstract class LiquibaseChangeLogLoader {
    public static void load(String changelog, DataSource dataSource) throws Exception {
        try(Connection conn = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
        }
    }

    public static void loadWithDropAllFirst(String changelog, DataSource dataSource) throws Exception {
        try(Connection conn = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
            liquibase.update("");
        }
    }

    public static void loadWithTruncateOnly(DataSource dataSource) throws Exception {
        try(Connection conn = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase((String) null, new ClassLoaderResourceAccessor(), database);
            liquibase.dropAll();
        }
    }
}
