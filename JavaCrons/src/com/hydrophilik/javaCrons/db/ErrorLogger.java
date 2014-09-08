package com.hydrophilik.javaCrons.db;

import com.hydrophilik.javaCrons.utils.Config;
import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class ErrorLogger {

    public static void logError(String errorDescription, Config config) {
        DbConnection db = null;

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
            db.close();
        }
    }
}
