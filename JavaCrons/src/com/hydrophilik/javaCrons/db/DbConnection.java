package com.hydrophilik.javaCrons.db;

import com.hydrophilik.javaCrons.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class DbConnection {

    private Connection connection = null;

    public DbConnection(String connectionStr, String user, String password) throws Exception {

        Class.forName("org.postgresql.Driver");
        //"jdbc:postgresql://127.0.0.1:5432/testdb", "mkyong",
        //"123456");

        connection = DriverManager.getConnection(connectionStr, user, password);

        if (null == connection) {
            throw (new Exception("Unable to make connection"));
        }
    }

    public DbConnection(Config config) throws Exception {
        Class.forName("org.postgresql.Driver");
        String dbConnectionString = config.getSetting("dbConnectionString");
        String dbUser = config.getSetting("dbUser");
        String dbPassword = config.getSetting("dbPassword");

        if ((null == dbConnectionString) || (null == dbUser) || (null == dbPassword)) {
            throw (new Exception("Unable to find database parameters in the config file"));
        }
        connection = DriverManager.getConnection(dbConnectionString, dbUser, dbPassword);

        if (null == connection) {
            throw (new Exception("Unable to make connection"));
        }

    }

    public void update(String sqlString) {

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sqlString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != statement) statement.close();
            }
            catch (Exception e) {}
        }
    }

    public void batchUpdate(List<String> sqlStatements) {
        Statement statement = null;
        try {

            statement = connection.createStatement();

            connection.setAutoCommit(false);

            for (String sqlStatement : sqlStatements) {
                statement.addBatch(sqlStatement);
            }

            statement.executeBatch();

            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (null != statement) statement.close();
            }
            catch (Exception e) {}
        }
    }

    public void close() {
        if (null != connection) {
            try {
                connection.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


