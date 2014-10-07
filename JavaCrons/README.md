JavaCrons

This Java application contains a few scripts that are run as part of periodic cron jobs to scrape data from various sites.  They are
written in Java v. 1.7.  As an argument, each script requires a configuration file with various attributes specific to the database and
user running the script.  A sample of this configuration can be found in this directory.

1. com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaDailyScrape
Performs a daily scrape of the NOAA Daily Quality Controlled hourly rainfall data for this month and the previous month.
Data source: http://cdo.ncdc.noaa.gov/qclcd/QCLCD?prior=N

2. com.hydrophilik.javaCrons.rainWarning
This script performs a scrape of Forecast.io of hourly rainfall for the next 24 hours.  It logs if the rain exceeds specific thresholds.
This requires a user key from Forecast.io, which must be set in the configuration file.
Forecast.io API: https://developer.forecast.io/