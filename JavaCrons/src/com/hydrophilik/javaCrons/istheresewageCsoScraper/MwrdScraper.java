package com.hydrophilik.javaCrons.istheresewageCsoScraper;

import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.models.CsoEvent;
import com.hydrophilik.javaCrons.utils.Config;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class MwrdScraper {

    private Config config;

    public MwrdScraper(Config config) {
        this.config = config;
    }

    public List<CsoEvent> scapeDates(LocalDate startDate, LocalDate endDate) {
        List<CsoEvent> retVal = new ArrayList<CsoEvent>();
        LocalDate iteratingDate = new LocalDate(startDate);
        while (!iteratingDate.isAfter(endDate)) {
            List<CsoEvent> newEvents = scrapeDate(iteratingDate);
            retVal.addAll(newEvents);
            iteratingDate.plusDays(1);
        }
        return retVal;
    }

    public List<CsoEvent> scrapeDate(LocalDate date) {

        String mwrdWebPrefix = "http://apps.mwrd.org/CSO/CSOEventSynopsisReport.aspx?passdate=";

        String urlStr = mwrdWebPrefix + date.toString();
        Document document;

        try {
            document = Jsoup.connect(urlStr).get();
        }
        catch (Exception e) {
            ErrorLogger.logError(ExceptionUtils.getMessage(e), config);
            return null;
        }

        return MwrdCsoSynopsisParser.parseEvents(document, date);

    }
}