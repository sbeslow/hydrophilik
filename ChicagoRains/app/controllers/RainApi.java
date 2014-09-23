package controllers;

import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainEvent;
import org.joda.time.LocalDate;
import play.mvc.Result;
import play.mvc.Controller;
import utils.TimeUtilities;

import java.util.List;

public class RainApi extends Controller {

    // We are expecting the date in the format of MM-DD-YYYY
    public static Result rainByDates(String startDateStr, String endDateStr) {

        LocalDate startDate = TimeUtilities.parseDateInput(startDateStr);
        LocalDate endDate = TimeUtilities.parseDateInput(startDateStr);

        if ((null == startDate) || (null == endDate))
            return badRequest("Unable to parse start or end date.  Must be in format MM-DD-YYYY");

        List<NoaaRainEvent> rainEvents = null;
        return TODO;
    }
}
