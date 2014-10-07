package com.hydrophilik.javaCrons.istheresewageCsoScraper;


import com.hydrophilik.javaCrons.utils.Config;
import org.joda.time.LocalDate;

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
        } catch (Exception e) {

            System.out.println("Failed to connect to config file");
            return;
        }

        // Scrape ends with todays date, and starts with one month ago
        LocalDate endDate = new LocalDate();
        LocalDate startDate = endDate.minusMonths(1);

        MwrdScraper.scapeDates(startDate, endDate);

    }

}
