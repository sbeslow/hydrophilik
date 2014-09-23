package utils;

import org.joda.time.LocalDate;

public class TimeUtilities {
    public static LocalDate parseDateInput(String dateString) {

        String [] dateSplit = dateString.split("-");
        if (3 != dateSplit.length)
            return null;

        try {
            LocalDate retVal = new LocalDate(Integer.parseInt(dateSplit[2]),
                Integer.parseInt(dateSplit[0]),  Integer.parseInt(dateSplit[1]));
            return retVal;

        }
        catch (Exception e) {
            return null;
        }

    }
}
