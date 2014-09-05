package com.hydrophilik.javaCrons.rainWarning;

import com.hydrophilik.javaCrons.utils.Config;
import dme.forecastiolib.ForecastIO;

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
    }
}
