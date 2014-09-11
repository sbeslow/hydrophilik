package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * Created by scottbeslow on 9/11/14.
 */
public class NoaaRainEvent implements Comparable<NoaaRainEvent> {

    private DateTime startTime;
    private DateTime endTime;
    private String locationId;
    private Double precipitationInches;

    public NoaaRainEvent(DateTime startTime, DateTime endTime, String locationId, Double inches) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.locationId = locationId;
        this.precipitationInches = inches;
    }

    @Override
    public int compareTo(NoaaRainEvent o) {

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

    public Double getPrecipitationInches() {
        return precipitationInches;
    }

    public String sqlInsertString() {

        Timestamp startTimestamp = new Timestamp(startTime.getMillis());
        Timestamp endTimestamp = new Timestamp(endTime.getMillis());

        String sql = "UPDATE noaaHourlyPrecip SET precipitationInches=" + this.precipitationInches +
                " WHERE (locationId='" + this.locationId + "' AND startTime='" + startTimestamp + "');";

        sql += "INSERT INTO noaaHourlyPrecip (locationId, startTime, endTime, precipitationInches) " +
                "VALUES ('" + this.locationId + "','" + startTimestamp + "','" + endTimestamp +
                "'," + this.precipitationInches + ");";

        return sql;

    }
}
