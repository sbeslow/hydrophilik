package com.hydrophilik.javaCrons.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {

    public final static DateTimeZone chiTimeZone = DateTimeZone.forID("America/Chicago");

    // The dateStr coming in is in UTC timezone.  It will be converted to CST
    public static DateTime convertUtcStringToJoda(String dtStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeZone tz = DateTimeZone.forID("America/Chicago");

        DateTime utcDateTime = formatter.withZoneUTC().parseDateTime(dtStr);
        return new DateTime(utcDateTime, tz);
    }

    public static String convertJodaToString(DateTime dt) {
        //LocalDateTime dateTime = new LocalDateTime(dt.getMillis());
        //DateTimeZone tz = DateTimeZone.forID("America/Chicago");

        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        return fmt.print(dt);

    }

    public static LocalDate firstOfMonth(LocalDate date) {
        return new LocalDate(date.getYear(), date.getMonthOfYear(), 1);
    }

    public static DateTime createDateTimeFromCsv(String dateStr, int hour) {
        Integer year = Integer.parseInt(dateStr.substring(0,4));
        Integer month = Integer.parseInt(dateStr.substring(4, 6));
        Integer day = Integer.parseInt(dateStr.substring(6));

        boolean advanceDay = false;
        if (24 == hour) {
            hour = 0;
            advanceDay = true;
        }

        DateTime retVal = null;

        try {
            retVal = new DateTime(year, month, day, hour, 0, chiTimeZone);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (advanceDay)
            retVal = retVal.plusDays(1);
        return retVal;
    }

    // Should be arriving in the format of YYYY-MM-DD HH:MM:SS
    public static DateTime parseTimestampString(String timestampString) {
        String [] dateTimeSplit = timestampString.split(" ");
        if (2 != dateTimeSplit.length)
            return null;

        String [] dateSplit = dateTimeSplit[0].split("-");
        if (3 != dateSplit.length) {
            return null;
        }

        String [] timeSplit = dateTimeSplit[1].split(":");
        if (3 != timeSplit.length)
            return null;

        DateTime retVal = null;
        try {
            retVal = new DateTime(Integer.parseInt(dateSplit[0]),
                    Integer.parseInt(dateSplit[1]),
                    Integer.parseInt(dateSplit[2]),
                    Integer.parseInt(timeSplit[0]),
                    Integer.parseInt(timeSplit[1]),
                    Integer.parseInt(timeSplit[2]));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return retVal;

    }

    public static String convertDateToString(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        return fmt.print(date);
    }
}
