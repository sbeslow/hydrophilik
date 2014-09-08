package com.hydrophilik.javaCrons.rainWarning;

import org.joda.time.DateTime;
import java.sql.Timestamp;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class IoHourlyForecast implements Comparable<IoHourlyForecast>{
    private Long id;
    private DateTime startTime;
    private DateTime collectionTime;
    private double precipIntensityInchesPerHour;

    public IoHourlyForecast(DateTime startTime, double precipIntensityMMPerHour, DateTime collectionTime) {
        this.startTime = startTime;
        this.collectionTime = collectionTime;

        this.precipIntensityInchesPerHour = precipIntensityMMPerHour * 0.0393701;
    }

    public String sqlInsertStatement() {

        Timestamp startTimestamp = new Timestamp(startTime.getMillis());
        Timestamp collectionTimestamp = new Timestamp(collectionTime.getMillis());

        return "INSERT INTO iohourlyforecast (starttime, precipintensity, forecastedat)" +
                "VALUES ('" + startTimestamp + "'," + precipIntensityInchesPerHour + ",'" + collectionTimestamp + "')";

    }

    @Override
    public int compareTo(IoHourlyForecast o) {
        if (getStartTime().isBefore(o.getStartTime()))
            return -1;
        else if (o.getStartTime().isBefore(getStartTime()))
            return 1;
        else
            return 0;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public double getPrecipIntensityInchesPerHour() {
        return precipIntensityInchesPerHour;
    }
}