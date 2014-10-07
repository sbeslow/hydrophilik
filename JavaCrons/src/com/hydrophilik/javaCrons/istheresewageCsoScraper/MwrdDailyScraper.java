package com.hydrophilik.javaCrons.istheresewageCsoScraper;


import com.hydrophilik.javaCrons.models.CsoEvent;
import com.hydrophilik.javaCrons.utils.Config;
import org.joda.time.LocalDate;

import java.util.List;

public class MwrdDailyScraper {

    public static void main(String[] args) {
        Config config;

        // 0-> Configuration file
        if (null == args[0]) {
            System.out.println("This script requires a config file to be specified as commandline argument");
            return;
        }

        try {
            config = new Config(args[0]);
            Config.setConfiguration(config);
        } catch (Exception e) {

            System.out.println("Failed to connect to config file");
            return;
        }

        // Scrape ends with todays date, and starts with one month ago
//        LocalDate endDate = new LocalDate();
//        LocalDate startDate = endDate.minusMonths(1);
//        List<CsoEvent> events = scraper.scapeDates(startDate, endDate);
        LocalDate testDate = new LocalDate(2014, 6, 30);
        List<CsoEvent> events = MwrdScraper.scrapeDate(testDate);
        for (CsoEvent event : events) {
            System.out.println(event.print());
        }

    }

}
