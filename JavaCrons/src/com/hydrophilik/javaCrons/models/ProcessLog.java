package com.hydrophilik.javaCrons.models;

import org.joda.time.DateTime;

public class ProcessLog {

    private DateTime timestamp;
    private String message;

    public ProcessLog(DateTime timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
        writeToLog();
    }

    private void writeToLog() {

    }
}
