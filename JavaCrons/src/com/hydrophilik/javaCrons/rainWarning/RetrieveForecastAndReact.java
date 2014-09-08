package com.hydrophilik.javaCrons.rainWarning;

import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import dme.forecastiolib.FIOHourly;
import dme.forecastiolib.ForecastIO;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String forecastIoKey = config.getSetting("forecastIoKey");
        if (null == forecastIoKey) {
            System.out.println("Unable to determine forecastIoKey");
            return;
        }

        ForecastIO fio = new ForecastIO(forecastIoKey);
        fio.setUnits(ForecastIO.UNITS_SI);
        fio.getForecast("41.888570", "-87.635530");
        System.out.println("Timezone: " + fio.getTimezone());

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
            DateTime time = TimeUtils.convertStringToJoda(timeStr);

            System.out.println("Timestamp: " + TimeUtils.convertJodaToString(time));
            System.out.println("Precipitation Intensity: " + hourly.getHour(i).getByKey("precipIntensity"));
            System.out.println("");

            

        }

    }
}
