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

    public CsoEvent(LocalDate date, String startString, String endString, String outfallLocation,
                    String waterwaySegment, String duration) throws Exception {

        this.start = TimeUtils.constructDateTime(date, startString);
        this.end = TimeUtils.constructDateTime(date, endString);
        this.outfallLocation = outfallLocation;
        this.waterwaySegment = waterwaySegment;



    }

}
