package com.hydrophilik.javaCrons.models;

import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class CsoEvent {

    private DateTime start;
    private DateTime end;
    private String outfallLocation;
    private String waterwaySegment;

    public CsoEvent(DateTime start, DateTime end, String outfallLocation, String waterwaySegment) {
        this.waterwaySegment = waterwaySegment;
        this.start = start;
        this.end = end;
        this.outfallLocation = outfallLocation;
    }

    public String print() {
        return TimeUtils.convertJodaToString(start) + " - " + TimeUtils.convertJodaToString(end) + "\n" +
                "Outfall location: " + outfallLocation;
    }
}
