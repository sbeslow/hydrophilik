package com.hydrophilik.javaCrons.noaaHourlyRainQC;

/**
 * Created by scottbeslow on 9/11/14.
 */
public class NoaaRainStation {

    private String locationId;
    private String callSign;
    private Double lat = null;
    private Double lon = null;

    public NoaaRainStation(String locationId, String callSign) {
        this.locationId = locationId;
        this.callSign = callSign;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getLocationId() {
        return locationId;
    }
}
