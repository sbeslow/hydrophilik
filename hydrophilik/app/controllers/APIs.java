package controllers;

import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaDatabaseCmds;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainEvent;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainStation;
import org.joda.time.LocalDate;
import play.libs.Json;
import play.mvc.*;
import printables.PrintableRainEvent;
import utils.TimeUtilities;
import utils.UrlOptionParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class APIs extends Controller {

    private static List<NoaaRainStation> rainStations = null;

    public static Result index() {
        return ok(views.html.apis.apiMain.render());
    }

    public static Result rain(String optionsArg) {
        Map<String, String> options = UrlOptionParser.parseOptions(optionsArg);
        String startDateStr = options.get("startDate");
        String endDateStr = options.get("endDate");
        String location = options.get("location");
        if ((null == startDateStr) || (null == endDateStr) || (null == location)) {
            return badRequest("Must include startDate, endDate, and location options in this call");
        }

        location = location.toUpperCase();
        NoaaRainStation station = NoaaRainStation.getRainStationByCallSign(location);
        if (null == station)
            return badRequest("Invalid location call sign: " + location);

        LocalDate startDate;
        LocalDate endDate;

        try {

            startDate = TimeUtilities.parseDateInput(startDateStr);
            endDate = TimeUtilities.parseDateInput(endDateStr);
        }
        catch (Exception e) {
            return badRequest("Unable to translate dates.  They must be in the format MM-DD-YYYY");
        }

        List<NoaaRainEvent> rainEvents = NoaaDatabaseCmds.retrieveByDate(Application.config, startDate, endDate, station.getLocationId());
        List<PrintableRainEvent> printEvents = new ArrayList<PrintableRainEvent>();
        for (NoaaRainEvent event : rainEvents) {
            PrintableRainEvent pEvent = new PrintableRainEvent(event);
            printEvents.add(pEvent);
        }

        String eventString = Json.stringify(Json.toJson(printEvents));

        return ok(eventString);


    }

    public static Result beach(String optionsArg) {
        Map<String, String> options = UrlOptionParser.parseOptions(optionsArg);
        String name = options.get("name");

        return TODO;
    }
}