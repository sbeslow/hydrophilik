package com.hydrophilik.javaCrons.rainEventCategorizer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by scottbeslow on 8/18/14.
 */
public class ThresholdTable {

    public static Map<String, Double> build() {
        // Key is hours:year
        Map<String, Double> retVal = new HashMap<String, Double>();

        retVal.put("24:1", 2.43);
        retVal.put("24:2", 2.95);
        retVal.put("24:5",3.77);
        retVal.put("24:10", 4.45);
        retVal.put("24:25", 5.44);
        retVal.put("24:50", 6.28);
        retVal.put("24:100", 7.21);
        retVal.put("12:1", 2.06);
        retVal.put("12:2", 2.51);
        retVal.put("12:5", 3.2);
        retVal.put("12:10", 3.78);
        retVal.put("12:25", 4.61);
        retVal.put("12:50", 5.33);
        retVal.put("12:100", 6.1);
        retVal.put("6:1", 1.78);
        retVal.put("6:2", 2.17);
        retVal.put("6:5", 2.8);
        retVal.put("6:10", 3.32);
        retVal.put("6:25", 4.08);
        retVal.put("6:50", 4.73);
        retVal.put("6:100", 5.43);
        retVal.put("3:1", 1.49);
        retVal.put("3:2", 1.82);
        retVal.put("3:5", 2.31);
        retVal.put("3:10", 2.7);
        retVal.put("3:25", 3.23);
        retVal.put("3:50", 3.66);
        retVal.put("3:100", 4.11);
        retVal.put("2:1", 1.39);
        retVal.put("2:2", 1.7);
        retVal.put("2:5", 2.14);
        retVal.put("2:10", 2.49);
        retVal.put("2:25", 2.97);
        retVal.put("2:50", 3.35);
        retVal.put("2:100", 3.76);
        retVal.put("1:1", 1.18);
        retVal.put("1:2", 1.44);
        retVal.put("1:5", 1.8);
        retVal.put("1:10", 2.08);
        retVal.put("1:25", 2.46);
        retVal.put("1:50", 2.76);
        retVal.put("1:100", 3.08);

        return retVal;
    }
}
