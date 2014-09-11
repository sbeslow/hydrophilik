package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottbeslow on 9/2/14.
 */
public class NoaaDailyScrape {

    private static Config config = null;

    public static void main(String[] args) {

        // 0-> Configuration file
        if (null == args[0]) {
            ErrorLogger.logError("Config file location not specified", config);
            return;
        }

        List<NoaaRainStation> stations = null;
        try {
            config = new Config(args[0]);
            stations = retrieveNoaaRainStations();
        } catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
            return;
        }

        LocalDate today = new LocalDate();
        LocalDate endDate = today.plusMonths(1);
        endDate = TimeUtils.firstOfMonth(endDate);

        LocalDate startDate = today.minusMonths(1);
        startDate = TimeUtils.firstOfMonth(startDate);

        System.out.println("Running scraper with start " + startDate.toString() +
            " and end " + endDate.toString());

        for (NoaaRainStation station : stations) {

            System.out.println("Station " + station.getCallSign() + " " + station.getLocationId());

            //NCDCSiteScrape.scrapeTimespan(startDate, endDate, station.getCallSign(), station.getLocationId());

        }

    }

    private static List<NoaaRainStation> retrieveNoaaRainStations() {
        DbConnection dbConnection = null;

        List<NoaaRainStation> noaaStations = new ArrayList<NoaaRainStation>();
        List<List<String>> rows = null;

        try {
            dbConnection = new DbConnection(config);
            String selectSql = "SELECT \"locationId\", \"callSign\" from \"noaaStation\"";
            System.out.println(selectSql);

            rows = dbConnection.query(selectSql, 2);


        }
        catch (Exception e) {
        }
        finally {
            dbConnection.close();
        }

        if (null == rows) {
            return noaaStations;
        }

        for (List<String> row : rows) {
            String locationId = row.get(0);
            String callSign = row.get(1);
            NoaaRainStation noaaRainStation = new NoaaRainStation(locationId, callSign);
            noaaStations.add(noaaRainStation);
        }

        return noaaStations;
    }
}
