package com.hydrophilik.javaCrons.istheresewageCsoScraper;

import com.hydrophilik.javaCrons.db.DbConnection;
import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.models.CsoEvent;
import com.hydrophilik.javaCrons.utils.TimeUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CsoDbInterface {

    public static void addUpdateEvents(List<CsoEvent> newEvents) {

        List<String> sqlStatements = new ArrayList<String>();
        DbConnection dbCon = null;
        try {
            dbCon = new DbConnection();

        }
        catch(Exception e) {
            ErrorLogger.logError(e);
            return;
        }


        for (CsoEvent event : newEvents) {

            List<CsoEvent> dbEvents = retrieveEventsByDateAndLocation(event.getStart().toLocalDate(),
                    event.getEnd().toLocalDate(), event.getOutfallLocation());

            for (CsoEvent dbEvent : dbEvents) {
                if (event.overlapsWith(dbEvent)) {
                    sqlStatements.add(dbEvent.deleteSql());


                }
            }
            sqlStatements.add(event.insertSql());



        }

        dbCon.batchUpdate(sqlStatements);

    }

    public static List<CsoEvent> retrieveEventsByDateAndLocation(LocalDate startDate, LocalDate endDate, String outfallLocation) {

        DbConnection dbCon = null;

        try {
            dbCon = new DbConnection();
        }
        catch(Exception e) {

        }

        String startDateStr = TimeUtils.convertDateToString(startDate);

        String sql = "SELECT starttime, endtime, outfalllocation, waterwaysegment FROM csoevent " +
                "WHERE (starttime = '" + startDateStr + "' OR endtime = '" + startDateStr + "') OR " +
                "(starttime = '" + startDateStr + "' OR endtime = '" + startDateStr + "') AND " +
                "outfalllocation = " + outfallLocation;
        System.out.println(sql);

        List<List<String>> records = dbCon.query(sql, 4);
        return null;


    }

    public static void insertEvents(List<CsoEvent> events) {



    }
}
