package printables;

import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainEvent;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;

import java.io.Serializable;

public class PrintableRainEvent implements Serializable {

    public String start;
    public String end;
    public String locationId;
    public String precipitationInches;

    public PrintableRainEvent(DateTime startTime, DateTime endTime, String locationId, String precipitationInches) {
        this.start = TimeUtils.convertJodaToString(startTime);
        this.end = TimeUtils.convertJodaToString(endTime);
        this.locationId = locationId;
        this.precipitationInches = precipitationInches;
    }

    public PrintableRainEvent(NoaaRainEvent rainEvent) {
        this.start = TimeUtils.convertJodaToString(rainEvent.getStartTime());
        this.end = TimeUtils.convertJodaToString(rainEvent.getEndTime());
        this.locationId = rainEvent.getLocationId();
        this.precipitationInches = Double.toString(rainEvent.getPrecipitationInches());
    }
}
