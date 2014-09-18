package com.hydrophilik.javaCrons.db;

import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.FileManager;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CreateCSVFromDb {

    public static void main(String[] args) {
        Config config = null;
        // 0-> Configuration file
        if (null == args[0]) {
            System.out.println("ConfigFile not specified in commandline");
            return;
        }

        try {
            config = new Config(args[0]);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
            return;
        }

        createCsv(config);
    }

    // Creates CSV of NOAA Hourly Quality Controlled
    public static void createCsv(Config config) {

        // Setup database

        DbConnection dbConnection = null;

        List<List<String>> rows = null;

        try {

            dbConnection = new DbConnection(config);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        String workingDirectory = config.getSetting("workingDirectory");
        if (null == workingDirectory) {
            ErrorLogger.logError("Unable to retrieve workingDirectory from Config file", config);
            return;
        }

        String selectSql = "SELECT starttime,endtime,locationid,precipitationinches FROM noaahourlyprecip;";

        rows = dbConnection.query(selectSql, 4);
        if (null == rows) {
            ErrorLogger.logError("Retrieved null entries from the database as part of database command", config);
            return;
        }

        List<String> csvRows = new ArrayList<String>(rows.size() + 1);
        csvRows.add("startTime,endTime,locationId,precipInches");

        for (List<String> row : rows) {
            String startTime = row.get(0);
            String endTime = row.get(1);
            String locationId = row.get(2);
            String precipInches = row.get(3);
            Double precip = Double.parseDouble(precipInches);

            // Round the number
            DecimalFormat df = new DecimalFormat("#.##");
            precip = Double.valueOf(df.format(precip));

            String csvRow = startTime + "," + endTime + "," + locationId + "," + Double.toString(precip);
            csvRows.add(csvRow);

        }

        String retVal = FileManager.createAndWriteToFile(workingDirectory + "noaaRainEvent.csv", csvRows);
        if (null != retVal)
            ErrorLogger.logError(retVal, config);

    }
}