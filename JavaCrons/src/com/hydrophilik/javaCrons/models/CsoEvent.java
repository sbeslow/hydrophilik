package com.hydrophilik.javaCrons.models;

import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;

import java.sql.Timestamp;

public class CsoEvent {

    private DateTime start;
    private DateTime end;
    private String outfallLocation;

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public String getOutfallLocation() {
        return outfallLocation;
    }

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

    public boolean overlapsWith(CsoEvent dbEvent) {
        if ((dbEvent.getStart().isBefore(getStart()) && (!dbEvent.getEnd().isBefore(getStart()))) ||
                dbEvent.getStart().isBefore(getEnd()) && (!dbEvent.getEnd().isBefore(getEnd()))) {
            return false;
        }
        return true;
    }

    public String deleteSql() {
        Timestamp startTimestamp = new Timestamp(start.getMillis());
        Timestamp endTimestamp = new Timestamp(end.getMillis());

        return "DELETE FROM csoevent where start = '" + startTimestamp + " AND end = '" + endTimestamp +
                "' AND outfalllocation = '" + outfallLocation + "'";

    }

    public String insertSql() {

        return "INSERT ";
    }
}
