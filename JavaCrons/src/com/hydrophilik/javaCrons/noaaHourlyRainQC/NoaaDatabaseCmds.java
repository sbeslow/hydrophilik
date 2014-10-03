package com.hydrophilik.javaCrons.noaaHourlyRainQC;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.utils.Config;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class NoaaDatabaseCmds {

    public static List<NoaaRainEvent> retrieveByDate(Config config, LocalDate startDate, LocalDate endDate, String locationId) {

        DbConnection dbCon = null;
        List<NoaaRainEvent> events = new ArrayList<NoaaRainEvent>();

        try {
            dbCon = new DbConnection(config);

            // Add one day to endTime so that the query works
            endDate = endDate.plusDays(1);

            String startDateString = TimeUtils.convertDateToString(startDate);
            String endDateString = TimeUtils.convertDateToString(endDate);

            String sqlQuery = "Select starttime,endtime,locationid,precipitationinches from noaahourlyprecip " +
                                        "where (startTime >= '" + startDateString + "' AND " +
                                        "endTime <= '" + endDateString + "' and locationid='" + locationId + "')";
            List<List<String>> rawEventsFromDb = dbCon.query(sqlQuery, 4);
            if (null == rawEventsFromDb)
                return null;

            for (List<String> row: rawEventsFromDb) {
                DateTime startTime = TimeUtils.parseTimestampString(row.get(0));
                DateTime endTime = TimeUtils.parseTimestampString(row.get(1));

                String locationIdRet = row.get(2);
                Double precip = Double.parseDouble(row.get(3));
                NoaaRainEvent rainEvent = new NoaaRainEvent(startTime, endTime, locationIdRet, precip);
                events.add(rainEvent);
            }


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != dbCon) dbCon.close();
        }

        return events;
    }

    public static List<NoaaRainStation> retrieveRainStations(Config config) {

        DbConnection dbCon;
        List<NoaaRainStation> stations = new ArrayList<NoaaRainStation>();

        try {
            dbCon = new DbConnection(config);
        }
        catch (Exception e) {
            return stations;
        }


        String sqlQuery = "SELECT \"locationId\", \"callSign\" from \"noaaStation\"";
        List<List<String>> stationList = dbCon.query(sqlQuery, 2);
        if (null == stationList)
            return stations;

        for (List<String> stationInfo : stationList) {
            String locationId = stationInfo.get(0);
            String callSign = stationInfo.get(1);
            NoaaRainStation station = new NoaaRainStation(locationId, callSign);
            stations.add(station);
        }

        return stations;

    }

}
