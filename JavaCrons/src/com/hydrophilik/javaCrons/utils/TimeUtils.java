package com.hydrophilik.javaCrons.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class TimeUtils {

    public final static DateTimeZone chiTimeZone = DateTimeZone.forID("America/Chicago");

    // The dateStr coming in is in UTC timezone.  It will be converted to CST
    public static DateTime convertUtcStringToJoda(String dtStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeZone tz = DateTimeZone.forID("America/Chicago");

        DateTime utcDateTime = formatter.withZoneUTC().parseDateTime(dtStr);
        DateTime dateTime = new DateTime(utcDateTime, tz);

        return (dateTime);
    }

    public static String convertJodaToString(DateTime dt) {
        //LocalDateTime dateTime = new LocalDateTime(dt.getMillis());
        //DateTimeZone tz = DateTimeZone.forID("America/Chicago");

        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        String str = fmt.print(dt);
        return str;
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
}
