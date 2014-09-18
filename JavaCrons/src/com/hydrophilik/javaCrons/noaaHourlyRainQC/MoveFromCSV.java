package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.FileManager;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottbeslow on 9/16/14.
 */
public class MoveFromCSV {

    public static Config config = null;

    public static void main(String[] args) {
        String csvFileName = "/Users/scottbeslow/Downloads/rainEvents.csv";
        List<String> theLines = FileManager.readFileLines(csvFileName);

        // 0-> Configuration file
        if (null == args[0]) {
            ErrorLogger.logError("Config file location not specified", config);
            return;
        }

        try {
            config = new Config(args[0]);
         } catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
            return;
        }

        List<NoaaRainEvent> rainEvents = new ArrayList<NoaaRainEvent>();

        for (int i=1; i < theLines.size(); i++) {
            String [] columns = theLines.get(i).split(",");
            String year = columns[0];
            String month = columns[1];
            String day = columns[2];
            String startString = columns[3];
            String endString = columns[4];
            String location = columns[5];
            String precip = columns[6];

            int startHour = Integer.parseInt(startString.split(":")[0]);

            DateTime startTime = new DateTime(Integer.parseInt(year),
                    Integer.parseInt(month),
                    Integer.parseInt(day), startHour, 0, TimeUtils.chiTimeZone);

            DateTime endTime = startTime.plusHours(1);

            NoaaRainEvent rainEvent = new NoaaRainEvent(startTime, endTime, location, Double.parseDouble(precip));
            rainEvents.add(rainEvent);


        }

        NoaaDailyScrape.writeRainEventsToDb(rainEvents, config);

    }
}
