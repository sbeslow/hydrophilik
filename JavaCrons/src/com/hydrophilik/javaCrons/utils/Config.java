package com.hydrophilik.javaCrons.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by scottbeslow on 9/5/14.
 */
public class Config {

    private static Config configuration = null;

    private Map<String, String> settings = null;

    public Config(String pathToConfigfile) throws Exception {
        List<String> configLines = FileManager.readFileLines(pathToConfigfile);

        if (null == configLines) {
            throw new Exception("Unable to read config file");
        }

        settings = new HashMap<String, String>(configLines.size());

        for (String configLine : configLines) {
            String [] pairing = configLine.split(";");
            if (2 != pairing.length) {
                throw new Exception("Unable to parse configuration line: " + configLine);
            }

            settings.put(pairing[0], pairing[1]);
        }
    }

    public String getSetting(String key) {
        return settings.get(key);
    }

    public static Config getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Config config) throws Exception {
        Config.configuration = config;
    }
}