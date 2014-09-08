package com.hydrophilik.javaCrons.rainWarning;

import org.joda.time.DateTime;
import java.sql.Timestamp;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class IoHourlyForecast {
    private Long id;
    private DateTime startTime;
    private DateTime collectionTime;
    private double precipIntensity;

    public IoHourlyForecast(DateTime startTime, double precipIntensity, DateTime collectionTime) {
        this.startTime = startTime;
        this.collectionTime = collectionTime;
        this.precipIntensity = precipIntensity;
    }

    public String sqlInsertStatement() {

        Timestamp startTimestamp = new Timestamp(startTime.getMillis());
        Timestamp collectionTimestamp = new Timestamp(collectionTime.getMillis());

        return "INSERT INTO iohourlyforecast (starttime, precipintensity, forecastedat)" +
                "VALUES ('" + startTimestamp + "'," + precipIntensity + ",'" + collectionTimestamp + "')";

    }
}