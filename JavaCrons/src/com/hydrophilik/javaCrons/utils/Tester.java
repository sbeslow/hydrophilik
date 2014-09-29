package com.hydrophilik.javaCrons.utils;

import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaDatabaseCmds;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainEvent;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainStation;
import org.joda.time.LocalDate;

import java.util.List;

public class Tester {

    public static void main(String[] args) {

        Config config;
        // 0-> Configuration file
        if (null == args[0]) {
            System.out.println("No config file specified");
            return;
        }

        try {
            config = new Config(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        LocalDate endDate = new LocalDate();
        LocalDate startDate = endDate.minusDays(1);
        String locationId = "14819";

        List<NoaaRainEvent> rainEvents = NoaaDatabaseCmds.retrieveByDate(config, startDate, endDate, locationId);
        if (null == rainEvents) {
            System.out.println("Returned null");
            return;
        }

        for (NoaaRainEvent event : rainEvents) {
            System.out.println(TimeUtils.convertJodaToString(event.getStartTime()) + " => " + event.getPrecipitationInches());
        }

        List<NoaaRainStation> stations = NoaaDatabaseCmds.retrieveRainStations(config);
        for (NoaaRainStation station : stations) {
            System.out.println(station.getCallSign() + " " + station.getLocationId());
        }

    }
}
