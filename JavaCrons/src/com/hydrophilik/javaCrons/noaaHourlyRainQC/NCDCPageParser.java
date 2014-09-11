package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;


public abstract class NCDCPageParser {

    public static List<NoaaRainEvent> parseNCDCDataPage(String rawHtml) {
        String [] lines = rawHtml.split("\n");

        List<NoaaRainEvent> allEvents = new ArrayList<NoaaRainEvent>();
        for (String line : lines) {
            List<NoaaRainEvent> events = getRainEventsFromCsvLine(line);
            if (null == events)
                continue;

            for (NoaaRainEvent event : events) {
                allEvents.add(event);
            }
        }

        return allEvents;
    }

	private static List<NoaaRainEvent> getRainEventsFromCsvLine(String csvLine) {
		List<NoaaRainEvent> retVal = new ArrayList<NoaaRainEvent>();
		String [] columns = csvLine.split(",");
		
		if (50 != columns.length) {
			return null;
		}
		
		try {
			Integer.parseInt(columns[0]);
		}
		catch (Exception e) {
			return null;
		}
		
		String location = columns[0];
		String dateStr = columns[1];
		int year = Integer.parseInt(dateStr.substring(0,4));
		int month = Integer.parseInt(dateStr.substring(4, 6));
		int day = Integer.parseInt(dateStr.substring(6));

        if (9 == day) {
            System.out.println("Leap Day");
        }

		int placeHolder = 2;
		while (placeHolder < columns.length) {
			int hour = placeHolder / 2;


			DateTime endTime = TimeUtils.createDateTimeFromCsv(dateStr, hour);
			if (null == endTime) {
				placeHolder += 2;
				continue;
			}

			DateTime startTime = endTime.minusHours(1);

            // Is this a leap day
            if (startTime.getZone().nextTransition(startTime.getMillis()) < endTime.getMillis()) {
                System.out.println("Detected daylight savings now");
            }

			String inchesStr = columns[placeHolder];
			Double inches;
			try {
				inches = Double.parseDouble(inchesStr);
			}
			catch(Exception e) {
				inches = 0.0;
			}

			NoaaRainEvent event = new NoaaRainEvent(startTime, endTime, location, inches);
			retVal.add(event);
			
			placeHolder += 2;
		}

		if (24 != retVal.size()) {
			System.out.println("Incorrect number of days for: " + month + "/" + day + "/" + year);
		}

		return retVal;
	}
	

}
