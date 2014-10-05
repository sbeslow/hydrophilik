package com.hydrophilik.javaCrons.csoScraperJaunt;

import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.models.CsoEvent;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public abstract class MwrdCsoScraper {

    public static List<CsoEvent> scrapeDates(Config config, LocalDate startDate, LocalDate endDate) {

        LocalDate date = startDate;
        List<CsoEvent> events = new ArrayList<CsoEvent>();
        while (!date.isAfter(endDate)) {
            List<CsoEvent> newEvents = scrapeDate(config, date);
            for (CsoEvent event : newEvents)
                events.add(event);
            date.plusDays(1);
        }
        return events;
    }

    public static List<CsoEvent> scrapeDate(Config config, LocalDate date) {
        String MWRD_URL = "http://apps.mwrd.org/CSO/CSOEventSynopsisReport.aspx?passdate=";
        // Date needs to be in this format: 9/21/2014

        List<CsoEvent> retVal = new ArrayList<CsoEvent>();

        try {

            UserAgent userAgent = new UserAgent();
            String dateString = TimeUtils.convertDateToStringWithSlashes(date);
            userAgent.visit(MWRD_URL + dateString);
            Element closureTable = userAgent.doc.findFirst("<table id=DG1>");
            Elements ols = closureTable.findEach("<tr>");
            for (Element element : ols) {
                Elements columns = element.findEach("<td>");
                Element firstColumn = columns.getElement(0);
                String location = firstColumn.innerHTML();
                if (location.contains("Location"))
                    continue;

                String segment = columns.getElement(1).innerHTML();
                String startString = columns.getElement(2).innerHTML();
                String endString = columns.getElement(3).innerHTML();
                String durationStr = columns.getElement(4).innerHTML();

                List<CsoEvent> csoEvents = createCsoEventsMwrdPageRow(config, date, location, segment, startString, endString, durationStr);

                System.out.println(location + " , " + segment + " , " + startString + " , " + endString +
                    " , " + durationStr);

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return retVal;

    }

    private static List<CsoEvent> createCsoEventsMwrdPageRow(Config config, LocalDate date, String outfallLocation,
                String waterwaySegment, String startString, String endString, String durationStr) {

        DateTime start = TimeUtils.constructDateTime(date, startString);
        DateTime end = TimeUtils.constructDateTime(date, endString);
        Integer durationFromPage = null;
        try {
            durationFromPage = translateDurationString(durationStr);
        }
        catch (Exception e) {
            ErrorLogger.logError("Date: " + TimeUtils.convertDateToString(date) + "\n" + ExceptionUtils.getStackTrace(e), config);
        }

        int calculatedDurationMins = (int) ((end.getMillis() - start.getMillis()) / 60000);

        List<CsoEvent> csoEventList = new ArrayList<CsoEvent>();

        // If the calculated duration and the duration from the MWRD page match, then just create
        // the event and return.
        if ((null == durationFromPage) || (calculatedDurationMins == durationFromPage)) {

            csoEventList.add(new CsoEvent(start, end, outfallLocation, waterwaySegment));
            return csoEventList;
        }

        // If we've reached this point, then the calculated duration does not match the duration
        // read from MWRD.
        // The first possibility is that the CSO went into a future day.
        // Otherwise, I'll just assume that the MWRD end time is crap.
        DateTime mwrdPossibleEndDate = start.plusMinutes(durationFromPage);
        if (mwrdPossibleEndDate.toLocalTime().equals(end.toLocalTime()))
            end = mwrdPossibleEndDate;
        else
            end = start.plusMinutes(calculatedDurationMins);

        DateTime dateTime = new DateTime(start.getMillis());
        while (dateTime.isAfter(end)) {
            DateTime thisEnd;
            if (end.toLocalDate().equals(dateTime.toLocalDate())) {
                thisEnd = end;
            }
            else {
                thisEnd = TimeUtils.constructDateTime(dateTime.toLocalDate(), "23:59").plusMinutes(1);
            }
            csoEventList.add(new CsoEvent(dateTime, thisEnd, outfallLocation, waterwaySegment));
            dateTime = TimeUtils.constructDateTime(dateTime.toLocalDate().plusDays(1), "0:00");
        }

        return csoEventList;
    }

    private static int translateDurationString(String durationString) throws Exception {
        String [] minHrs = durationString.split(":");
        if (2 != minHrs.length) {
            throw new Exception("Unable to translate duration: " + durationString);
        }

        int duration = Integer.parseInt(minHrs[0]) * 60;
        return duration + Integer.parseInt(minHrs[1]);



    }
}
