package com.hydrophilik.javaCrons.rainWarning;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.rainEventCategorizer.AlertSearcher;
import com.hydrophilik.javaCrons.rainEventCategorizer.EventAlert;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import dme.forecastiolib.FIOHourly;
import dme.forecastiolib.ForecastIO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class RetrieveForecastAndReact {

    public static Config config = null;

    public static void main(String[] args) {


        // 0-> Configuration file
        if (null == args[0]) {
            ErrorLogger.logError("Config file location not specified", config);
            return;
        }

        try {
            config = new Config(args[0]);
        } catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
            return;
        }

        ErrorLogger.logError("Started retrieving ForecastIoWarning test", config);

        List<IoHourlyForecast> forecasts = grabForecastIoForecast();
        if ((null == forecasts) || (0 == forecasts.size())) {
            ErrorLogger.logError("ForecastIo Scrape came up with no entries", config);
        }

        writeForecastsToDb(forecasts);

        AlertSearcher alertSearcher = new AlertSearcher();
        List<EventAlert> alerts = alertSearcher.alertSearch(forecasts);
        if (0 == alerts.size()) {
            System.out.println("No alerts found");
        }
        else {
            writeAlertsToDb(alerts);
        }
    }

    private static List<IoHourlyForecast> grabForecastIoForecast() {

        List<IoHourlyForecast> forecasts = new ArrayList<IoHourlyForecast>(24);

        String forecastIoKey = config.getSetting("forecastIoKey");
        if (null == forecastIoKey) {
            System.out.println("Unable to determine forecastIoKey");
            return forecasts;
        }

        ForecastIO fio = new ForecastIO(forecastIoKey);
        fio.setUnits(ForecastIO.UNITS_SI);
        fio.getForecast("41.888570", "-87.635530");

        DateTime now = new DateTime();

        FIOHourly hourly = new FIOHourly(fio);

        for(int i = 0; i < 24; i++) {

            String timeStr = hourly.getHour(i).getByKey("time");
            DateTime time = TimeUtils.convertUtcStringToJoda(timeStr);

            String precipStr = hourly.getHour(i).getByKey("precipIntensity");
            double precip;
            try {
                precip = Double.parseDouble(precipStr);
            }
            catch (Exception e) {
                ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
                precip = 0.0;
            }

            IoHourlyForecast thisForcast = new IoHourlyForecast(time, precip, now);
            forecasts.add(thisForcast);

        }

        return forecasts;

    }

    private static void writeForecastsToDb(List<IoHourlyForecast> forecasts) {

        DbConnection db = null;
        List<String> sqlStatements = new ArrayList<String>(forecasts.size());

        for (IoHourlyForecast forecast : forecasts) {
            sqlStatements.add(forecast.sqlInsertStatement());
        }

        try {
            db = new DbConnection(config);
            db.batchUpdate(sqlStatements);

        }
        catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
        }
        finally {
            if (null != db) db.close();
        }
    }

    private static void writeAlertsToDb(List<EventAlert> alerts) {
        DbConnection db = null;
        List<String> sqlStatements = new ArrayList<String>(alerts.size());

        for (EventAlert alert : alerts) {
            sqlStatements.add(alert.sqlInsertStatement());
        }

        try {
            db = new DbConnection(config);
            db.batchUpdate(sqlStatements);
        }
        catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getStackTrace(e), config);
        }
        finally {
            if (null != db) db.close();
        }

    }
}