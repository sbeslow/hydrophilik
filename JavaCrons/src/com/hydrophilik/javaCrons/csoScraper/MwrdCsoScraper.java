package com.hydrophilik.javaCrons.csoScraper;

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

public class MwrdCsoScraper {

    public static List<CsoEvent> scrapeDates(LocalDate startDate, LocalDate endDate) {

        LocalDate date = startDate;
        List<CsoEvent> events = new ArrayList<CsoEvent>();
        while (!date.isAfter(endDate)) {

        }
        return null;
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

                List<CsoEvent> csoEvents = createCsoEvents(config, date, location, segment, startString, endString, durationStr);

                System.out.println(location + " , " + segment + " , " + startString + " , " + endString +
                    " , " + durationStr);

            }




        } catch (Exception e) {
            e.printStackTrace();

        }

        return retVal;

    }

    private static List<CsoEvent> createCsoEvents(Config config, LocalDate date, String location,
                String segment, String startString, String endString, String durationStr) {

        List<CsoEvent> retVal = new ArrayList<CsoEvent>();

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
        if ((null != durationFromPage) &&  (calculatedDurationMins != durationFromPage)) {
            DateTime siteBasedEndTime = start.plusMinutes(durationFromPage);
            if (siteBasedEndTime.toLocalTime().equals(end.toLocalTime())) {
                end = siteBasedEndTime;
            }
            else {

                ErrorLogger.logError(TimeUtils.convertJodaToString(start) + "has non-matching durations of: " +
                        durationFromPage + " and " + calculatedDurationMins, config);
            }
        }

        if (start.toLocalDate().equals(end.toLocalDate())) {
            retVal.add(new CsoEvent(start, end, location, segment));
            return retVal;
        }



        // TODO: If dates are not equal, this must span across several days




        return null;
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
