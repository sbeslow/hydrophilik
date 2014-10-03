package com.hydrophilik.javaCrons.utils;

import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaDatabaseCmds;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainEvent;
import com.hydrophilik.javaCrons.noaaHourlyRainQC.NoaaRainStation;
import org.joda.time.LocalDate;

import java.util.List;

public class Tester {

    public static void main(String[] args) {

        Config config;
        // 0-> Configuration file
        if (null == args[0]) {
            System.out.println("No config file specified");
            return;
        }

        try {
            config = new Config(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        MailPerson.sendErrorEmail(config, "This is an error test of the emergency broadcast system");

    }
}
