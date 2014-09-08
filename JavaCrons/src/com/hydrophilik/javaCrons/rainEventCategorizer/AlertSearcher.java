package com.hydrophilik.javaCrons.rainEventCategorizer;

import com.hydrophilik.javaCrons.db.ErrorLogger;
import com.hydrophilik.javaCrons.rainWarning.IoHourlyForecast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by scottbeslow on 9/8/14.
 */
public class AlertSearcher {

    private Map<String, Double> thresholdTable = null;

    public AlertSearcher() {
        thresholdTable = ThresholdTable.build();

    }

    public List<EventAlert> alertSearch(List<IoHourlyForecast> forecasts) {
        List<EventAlert> alerts = new ArrayList<EventAlert>();

        List<EventAlert> frequencyAlerts = searchForFreqAlerts(forecasts);
        if ((null != frequencyAlerts) && (0 != frequencyAlerts.size())) {
            for (EventAlert freqAlert : frequencyAlerts) {
                alerts.add(freqAlert);
            }
        }

        return alerts;
    }

    private List<EventAlert> searchForFreqAlerts(List<IoHourlyForecast> forecasts) {
        List<EventAlert> alerts = new ArrayList<EventAlert>();

        List<Integer> yearList = Arrays.asList(100, 50, 25, 10, 5, 2, 1);
        List<Integer> hourList = Arrays.asList(24, 12,6,3,2,1);

        for (Integer yearInt : yearList) {
            for (Integer hour : hourList) {
                List<EventAlert> freqEvents = findFreqEvent(forecasts, hour, yearInt);
                if ((null != freqEvents) && (0 != freqEvents.size())) {
                    for (EventAlert freqEvent : freqEvents) {
                        alerts.add(freqEvent);
                    }
                }
            }
        }

        EventAlert cumulativeAlert = findCumulative(forecasts);
        if (null != cumulativeAlert)
            alerts.add(cumulativeAlert);

        return alerts;
    }

    private List<EventAlert> findFreqEvent(List<IoHourlyForecast> forecasts, int hours, int yearStorm) {

        Double threshold = thresholdTable.get(hours + ":" + yearStorm);

        if ((null == threshold) || (0 == threshold)) {
            ErrorLogger.logError("Unable to retrieve threshold for " + hours + " " + yearStorm, null);
            return new ArrayList<EventAlert>(1);
        }

        List<EventAlert> alerts = new ArrayList<EventAlert>();

        for (IoHourlyForecast forecast : forecasts) {
            if (0 == forecast.getPrecipIntensityInchesPerHour()) {
                continue;
            }

            int place = forecasts.indexOf(forecast);
            int endPlace = place + hours;

            if (endPlace > forecasts.size()) {
                break;
            }

            double totalPrecip = 0;
            for (int i = 0; i < hours; i++) {
                totalPrecip += forecasts.get(place + i).getPrecipIntensityInchesPerHour();
            }

            if (totalPrecip >= threshold) {
                EventAlert alert = new EventAlert(forecast.getStartTime(), EventAlert.AlertType.FREQUENCY,
                        null, yearStorm);
                alerts.add(alert);
            }
        }

        return alerts;
    }

    private EventAlert findCumulative(List<IoHourlyForecast> forecasts) {;

        double totalPrecip = 0;
        for (IoHourlyForecast forecast : forecasts) {
            totalPrecip += forecast.getPrecipIntensityInchesPerHour();
        }

        List<Double> milestones = Arrays.asList(4.0, 3.0, 2.0, 1.5, 1.0, 0.5, 0.1);
        for (Double milestone : milestones) {
            if (totalPrecip >= milestone)
                return new EventAlert(forecasts.get(0).getStartTime(), EventAlert.AlertType.RAIN_PER_INTERVAL,
                        totalPrecip, 24);
        }

        return null;
    }


}
