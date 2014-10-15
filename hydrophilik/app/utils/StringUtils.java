package utils;

/**
 * Created by scottbeslow on 10/15/14.
 */
public class StringUtils {
    public static String capitalizeFirstLetter(String origString) {
        return origString.substring(0, 1).toUpperCase() + origString.substring(1);
    }
}
