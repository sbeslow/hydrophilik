package com.hydrophilik.javaCrons.istheresewageCsoScraper;

import java.util.ArrayList;
import java.util.List;

import com.hydrophilik.javaCrons.models.CsoEvent;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MwrdCsoSynopsisParser {

	public static List<CsoEvent> parseEvents(Document doc, LocalDate date) {

        List<CsoEvent> events = new ArrayList<CsoEvent>();
		
		// If there are no events, then nothing to do here, move on.
		Element eventTable = doc.getElementById("DG1");
		if (null == eventTable)
			return events;

		// Parse through the table
		Elements eventsTags = eventTable.getElementsByTag("tr");		
		for (Element event : eventsTags) {
			Elements theRow = event.getElementsByTag("td");

			// Each row represents one event.  The event has five rows in it.
			// Create an array of strings, and place the values along the column
			// into it.
			int index = 0;
			String [] aTable = new String[5];			
			for (Element field : theRow) {
				aTable[index] = field.text();
				index++;
			}
			
			// If the value of the first column of this row is "Outfall Location", it
			// is the title row.  Skip it.
			if (!aTable[0].equals("Outfall Location")) {
				
				// A given synopsis page could cover multiple days.
				// Therefore, the build method could create multiple events
				// from just one row.
				// Example: Check out the MWRD Synopsis page for 4/18, which actually
				// covers 4/18-4/22.
				CsoEvent readEvent = parseSynopsisRow(aTable, date);

                if (null != readEvent)
                    events.add(readEvent);
				
			}
		}

		return events;

	}
	
	private static CsoEvent parseSynopsisRow(String [] row, LocalDate date) {

        // The expected format of the incoming row.
        // row[0] -> Outfall Location
        // row[1] -> Waterway Segment
        // row[2] -> Start Time (HH:MM)
        // row[3] -> Stop Time (HH:MM)
        // row[4] -> Duration (HH:MM)

		String outfallLocation = row[0];
		String waterwaySegment = row[1];
		
		// Use the duration field to determine if the event goes into the next day(s)
		String [] durations = row[4].split(":");
		
		Integer durationFromPageMins;
		try {
            durationFromPageMins = (Integer.parseInt(durations[0]) * 60) + Integer.parseInt(durations[1]);
		}
		catch (Exception e) {
			System.out.println("Duration is not an int for outfall " + outfallLocation + " on " + TimeUtils.convertDateToString(date));
			durationFromPageMins = null;
		}

        DateTime start = TimeUtils.constructDateTime(date, row[2]);
        DateTime end = TimeUtils.constructDateTime(date, row[3]);

        int calculatedDurationMins = (int) ((end.getMillis() - start.getMillis()) / 60000);

        // If the calculated duration and the duration from the MWRD page match, then just create
        // the event and return.
        if ((null == durationFromPageMins) || (calculatedDurationMins == durationFromPageMins)) {

            return new CsoEvent(start, end, outfallLocation, waterwaySegment);
        }

        // If we've reached this point, then the calculated duration does not match the duration
        // read from MWRD.
        // The first possibility is that the CSO went into a future day.
        // Otherwise, I'll just assume that the MWRD end time is crap.
        DateTime mwrdPossibleEndDate = start.plusMinutes(durationFromPageMins);
        if (mwrdPossibleEndDate.toLocalTime().equals(end.toLocalTime()))
            end = mwrdPossibleEndDate;
        else
            end = start.plusMinutes(calculatedDurationMins);

        return new CsoEvent(start, end, outfallLocation, waterwaySegment);

	}
}
