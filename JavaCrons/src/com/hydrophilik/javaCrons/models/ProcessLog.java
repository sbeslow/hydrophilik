package com.hydrophilik.javaCrons.models;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.db.ErrorLogger;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class ProcessLog {

    public static void writeToLog(String message) {
        DbConnection dbCon;
        try {
            dbCon = new DbConnection();
        }
        catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getMessage(e));
            return;
        }

        DateTime now = new DateTime();
        Timestamp tStamp = new Timestamp(now.getMillis());
        String sql = "INSERT INTO ProcessLog (entryTime,message) VALUES ('" + tStamp +
                "','" + message + "')";

        dbCon.update(sql);


    }
}
