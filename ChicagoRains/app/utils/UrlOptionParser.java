package utils;

import java.util.HashMap;
import java.util.Map;

public class UrlOptionParser {

    public static Map<String, String> parseOptions(String optionString) {
        Map<String, String> retVals = new HashMap<>();

        String [] options = optionString.split("&");
        if ((null == options) || (0 == options.length)) {
            return retVals;
        }

        for (String option : options) {
            String [] pairing = option.split("=");
            if (2 != pairing.length)
                continue;

            retVals.put(pairing[0], pairing[1]);
        }

        return retVals;
    }
}
