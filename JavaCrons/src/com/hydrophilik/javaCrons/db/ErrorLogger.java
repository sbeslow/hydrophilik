package com.hydrophilik.javaCrons.db;

import com.hydrophilik.javaCrons.rainWarning.RetrieveForecastAndReact;
import com.hydrophilik.javaCrons.utils.Config;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class ErrorLogger {

    public static void logError(String errorDescription) {
        logError(errorDescription, Config.getConfiguration());
    }

    public static void logError(String errorDescription, Config config) {
        DbConnection db = null;

        if (null == config) {
            config = RetrieveForecastAndReact.config;
        }

        try {
            db = new DbConnection(config);
            DateTime now = new DateTime();
            Timestamp tStamp = new Timestamp(now.getMillis());
            String sql = "INSERT INTO errorlog (entrytime, description) VALUES ('"+
                    tStamp + "','" + errorDescription + "')";
            db.update(sql);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != db) db.close();
        }
    }

    public static void logError(Exception e) {
        logError(ExceptionUtils.getStackTrace(e));
    }
}
