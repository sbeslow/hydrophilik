package com.hydrophilik.javaCrons.rainWarning;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import dme.forecastiolib.FIOHourly;
import dme.forecastiolib.ForecastIO;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scottbeslow on 9/5/14.
 */
public class RetrieveForecastAndReact {

    public static Config config = null;

    public static void main(String[] args) {

        // 0-> Configuration file
        if (null == args[0]) {
            System.out.println("Config file location not specified");
            return;
        }

        try {
            config = new Config(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<IoHourlyForecast> forecasts = grabForecastIoForecast();
        if ((null == forecasts) || (0 == forecasts.size())) {
            return;
        }

        writeForecastsToDb(forecasts);
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
        if(hourly.hours()<0)
            System.out.println("No hourly data.");
        else
            System.out.println("\nHourly:\n");
        //Print hourly data

        for(int i = 0; i < 24; i++) {
            String [] h = hourly.getHour(i).getFieldsArray();
            System.out.println("Hour: " + Integer.toString(i+1));

            String timeStr = hourly.getHour(i).getByKey("time");
            DateTime time = TimeUtils.convertUtcStringToJoda(timeStr);

            String precipStr = hourly.getHour(i).getByKey("precipIntensity");
            double precip = 0.0;
            try {
                precip = Double.parseDouble(precipStr);
            }
            catch (Exception e) {
                // TODO: Write error here
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
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }
}