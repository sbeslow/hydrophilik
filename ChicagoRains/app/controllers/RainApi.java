package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaDatabaseCmds;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainEvent;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainStation;
import org.joda.time.LocalDate;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;
import printables.PrintableRainEvent;
import utils.TimeUtilities;
import utils.UrlOptionParser;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RainApi extends Controller {

    private static List<NoaaRainStation> rainStations = null;

    public static Result hourlyPrecip(String optionsArg) {

        Map<String, String> options = UrlOptionParser.parseOptions(optionsArg);
        String startDateStr = options.get("startDate");
        String endDateStr = options.get("endDate");
        String location = options.get("location");
        if ((null == startDateStr) || (null == endDateStr) || (null == location)) {
            return badRequest("Must include startDate, endDate, and locationId options in this call");
        }

        NoaaRainStation station = getRainStationByCallSign(location);
        if (null == station)
            return badRequest("Invalid location call sign: " + location);


        return rainByDates(startDateStr, endDateStr, station.getLocationId());
    }

    // We are expecting the date in the format of MM-DD-YYYY
    public static Result rainByDates(String startDateStr, String endDateStr, String locationId) {

        LocalDate startDate;
        LocalDate endDate;

        try {

            startDate = TimeUtilities.parseDateInput(startDateStr);
            endDate = TimeUtilities.parseDateInput(endDateStr);
        }
        catch (Exception e) {
            return badRequest("Unable to translate dates.  They must be in the format MM-DD-YYYY");
        }

        List<NoaaRainEvent> rainEvents = NoaaDatabaseCmds.retrieveByDate(Application.config, startDate, endDate, locationId);
        List<PrintableRainEvent> printEvents = new ArrayList<PrintableRainEvent>();
        for (NoaaRainEvent event : rainEvents) {
            PrintableRainEvent pEvent = new PrintableRainEvent(event);
            printEvents.add(pEvent);
        }

        String eventString = Json.stringify(Json.toJson(printEvents));

        return ok(eventString);
    }

    private static NoaaRainStation getRainStationByCallSign(String callSign) {
        if (null == rainStations) {
            rainStations = NoaaDatabaseCmds.retrieveRainStations(Application.config);
        }

        for (NoaaRainStation station : rainStations) {
            if (callSign.equals(station.getCallSign())) {
                return station;
            }
        }

        return null;
    }
}
