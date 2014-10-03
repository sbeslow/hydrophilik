package com.hydrophilik.javaCrons.csoScraper;

import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.models.CsoEvent;
import com.hydrophilik.javaCrons.utils.Config;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by scottbeslow on 9/30/14.
 */
public class DailyCsoScrape {

    public static void main(String[] args) {

        Config config;

        // 0-> Configuration file
        if (null == args[0]) {
            System.out.println("This script requires a config file to be specified as commandline argument");
            return;
        }

        try {
            config = new Config(args[0]);
        } catch (Exception e) {

            System.out.println("Failed to connect to config file");
            return;
        }

        LocalDate date = new LocalDate(2014, 6, 30);
        //List<CsoEvent> events = MwrdCsoScraper.scrapeDate(date);





    }
}
