package com.hydrophilik.javaCrons.db;

import com.hydrophilik.javaCrons.utils.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {

    private Connection connection = null;

    public DbConnection(String connectionStr, String user, String password) throws Exception {

        Class.forName("org.postgresql.Driver");

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

    public DbConnection() throws Exception  {
        this(Config.getConfiguration());
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

    public List<List<String>> query(String sql, int numColumns) {

        Statement statement = null;
        ResultSet rs = null;

        List<List<String>> rows = new ArrayList<List<String>>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                List<String> row = new ArrayList<String>(numColumns);
                for (int i=1; i <= numColumns; i++) {
                    row.add(rs.getString(i));
                }
                rows.add(row);
            }
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

        return rows;

    }

}