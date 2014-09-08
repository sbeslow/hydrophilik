package com.hydrophilik.javaCrons.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class TimeUtils {

    // The dateStr coming in is in UTC timezone.  It will be converted to CST
    public static DateTime convertUtcStringToJoda(String dtStr) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm:ss");
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
}
