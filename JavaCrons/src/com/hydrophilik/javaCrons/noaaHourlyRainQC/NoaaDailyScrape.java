package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import com.jaunt.Document;
import com.jaunt.UserAgent;
import com.jaunt.component.Form;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottbeslow on 9/2/14.
 */
public class NoaaDailyScrape {

    private static Config config = null;
    private static String NCDC_URL="http://cdo.ncdc.noaa.gov/qclcd/QCLCD?prior=N";

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

            scrapeTimespan(startDate, endDate, station.getCallSign(), station.getLocationId());

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

    private static void scrapeTimespan(LocalDate startDate, LocalDate endDate, String callSign, String stationId) {

        try {

            UserAgent userAgent = new UserAgent();

            LocalDate date = startDate.plusDays(30);

            while (false == date.isAfter(endDate)) {
                userAgent.visit(NCDC_URL);

                // Select Illinois
                Document il = userAgent.doc;
                il.select("Desired Station is Located", "Illinois");
                il.submit();

                Form form = userAgent.doc.getForm(0);
                form.setSelect("callsign", callSign);

                form.submit();
                form = userAgent.doc.getForm(0);

                Integer monthInt = Integer.parseInt(date.monthOfYear().getAsString());
                String monthStr = Integer.toString(monthInt);
                if (monthInt < 10) {
                    monthStr = "0" + monthStr;
                }

                String baseCode = stationId + date.year().getAsString() + monthStr;
                form.setSelect("VARVALUE", baseCode);

                System.out.println("Submitting basecode: " + baseCode);

                form.submit();
                form = userAgent.doc.getForm(0);
                form.setSelect("reqday", "E");
                form.setRadio("which", "ASCII Download (Hourly Precip.)");

                form.submit();

                List<NoaaRainEvent> rainEvents = NCDCPageParser.parseNCDCDataPage(userAgent.doc.innerHTML());

                writeRainEventsToDb(rainEvents);

                date = date.plusMonths(1);

            }


        } catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
            return;
        }

    }

    private static void writeRainEventsToDb(List<NoaaRainEvent> rainEvents) {

        try {
            DbConnection db = new DbConnection(config);

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
