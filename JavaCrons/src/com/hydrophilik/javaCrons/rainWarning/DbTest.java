package com.hydrophilik.javaCrons.rainWarning;

import com.hydrophilik.javaCrons.db.DbConnection;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class DbTest {

    public static void main(String[] args) throws Exception {

        DbConnection db = new DbConnection("jdbc:postgresql://127.0.0.1:5432/testDb", "scottbeslow", "");
        //"jdbc:postgresql://127.0.0.1:5432/testdb", "mkyong",
        //"123456");

        String insertTableSQL = "INSERT INTO testTable"
                + "(name) " + "VALUES ('Scott')";

        System.out.println(insertTableSQL);

        db.update(insertTableSQL);

        db.close();
    }
}
