package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.MailPerson;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import com.jaunt.Document;
import com.jaunt.UserAgent;
import com.jaunt.component.Form;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class NoaaDailyScrape {

    private static Config config = null;

    public static void main(String[] args) {

        // 0-> Configuration file
        if (null == args[0]) {
            ErrorLogger.logError("Config file location not specified", config);
            return;
        }

        List<NoaaRainStation> stations;
        try {
            config = new Config(args[0]);
            stations = retrieveNoaaRainStations();
        } catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
            return;
        }

        ErrorLogger.logError("Started Noaa Daily Scrape", config);

        LocalDate firstOfThisMonth = TimeUtils.firstOfMonth(new LocalDate());
        LocalDate startDate = firstOfThisMonth.minusMonths(1);
        LocalDate endDate = firstOfThisMonth.plusMonths(1).minusDays(1);

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
            e.printStackTrace();
        }
        finally {
            if (null != dbConnection) dbConnection.close();
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

        String NCDC_URL="http://cdo.ncdc.noaa.gov/qclcd/QCLCD?prior=N";

        try {

            UserAgent userAgent = new UserAgent();

            LocalDate date = startDate;

            while (!date.isAfter(endDate)) {
                userAgent.visit(NCDC_URL);

                // Select Illinois
                Document il = userAgent.doc;
                il.choose("Desired Station is Located", "Illinois");
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

                writeRainEventsToDb(rainEvents, config);

                date = date.plusMonths(1);

            }

        } catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getMessage(e), config);
            MailPerson.sendErrorEmail(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }

    }

    public static void writeRainEventsToDb(List<NoaaRainEvent> rainEvents, Config configArg) {

        try {
            DbConnection db = new DbConnection(configArg);

            // At this point, all rainEvents are for a given month.  We have either written
            // this whole month to the database before, or this is the first time.  If there
            // are already entries for this month, update.  If not, insert.
            boolean monthExists = false;
            List<List<String>> existingEvent = db.query(rainEvents.get(0).sqlSelectString(),1);
            if ((null != existingEvent) && (0 != existingEvent.size()))
                monthExists = true;

            List<String> sqlStatements = new ArrayList<String>(rainEvents.size());

            for (NoaaRainEvent rainEvent : rainEvents) {
                String sql;
                if (monthExists)
                    sql = rainEvent.sqlUpdateString();
                else
                    sql = rainEvent.sqlInsertString();

                System.out.println(sql);
                sqlStatements.add(sql);
            }

            db.batchUpdate(sqlStatements);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
