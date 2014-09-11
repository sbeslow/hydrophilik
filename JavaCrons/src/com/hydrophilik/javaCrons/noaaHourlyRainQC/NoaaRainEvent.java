package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import org.joda.time.DateTime;

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

}
