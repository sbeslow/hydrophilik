package controllers;

import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainStation;
import play.mvc.*;
import utils.UrlOptionParser;

import java.util.Map;

public class APIs extends Controller {

    public static Result index() {
        return ok(views.html.apis.apiMain.render());
    }

    public static Result rain() {
        Map<String, String> options = UrlOptionParser.parseOptions(optionsArg);
        String startDateStr = options.get("startDate");
        String endDateStr = options.get("endDate");
        String location = options.get("location");
        if ((null == startDateStr) || (null == endDateStr) || (null == location)) {
            return badRequest("Must include startDate, endDate, and location options in this call");
        }

        NoaaRainStation station = getRainStationByCallSign(location);
        if (null == station)
            return badRequest("Invalid location call sign: " + location);

    }
}
