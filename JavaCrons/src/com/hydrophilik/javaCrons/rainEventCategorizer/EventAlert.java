package com.hydrophilik.javaCrons.rainEventCategorizer;

import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class EventAlert {

    public enum AlertType {FREQUENCY, RAIN_PER_INTERVAL}

    private DateTime startTime;
    private AlertType alertType;
    private Double value = null;
    private int interval;

    public EventAlert(DateTime startTime, AlertType alertType, Double value, int interval) {
        this.startTime = startTime;
        this.alertType = alertType;
        this.value = value;
        this.interval = interval;
    }

    public String sqlInsertStatement() {
        Timestamp startTimestamp = new Timestamp(startTime.getMillis());

        return "INSERT INTO eventalert (starttime, alertType, value, interval)" +
                "VALUES ('" + startTimestamp + "'," + alertType.ordinal() + "," + value + "," +
                interval + ")";
    }
}
