package controllers;

import com.hydrophilik.javaCrons.utils.TimeUtils;
import models.Beach;
import org.joda.time.LocalDate;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import utils.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Beaches extends Controller {

    private static Map<String, Beach> beaches = null;

    public static final int SortAlphabetical=0;
    public static final int SortDirtiest=1;

    public static Result list(Integer sortOrder) {


        if (null == beaches) {
            try {
                Beaches.fillDatastore();
            } catch(Exception e) {
                return badRequest("Unable to retrieve data");
            }
        }

        List<String> beachNames = new ArrayList<>(beaches.keySet());

        String title = "Unsorted Beach list";

        if (SortAlphabetical == sortOrder) {
            Collections.sort(beachNames);
            title = "Beaches Sorted Alphabetically";
        }
        else if (SortDirtiest == sortOrder) {
            List<Beach> sortedBeaches = new ArrayList<>(beaches.values());
            Collections.sort(sortedBeaches);
            beachNames = new ArrayList<>(beachNames.size());
            for (Beach beach : sortedBeaches) {
                beachNames.add(beach.getName());
            }
            title = "Beaches Sorted by Dirtiest";
        }

        List<Beach> sortedBeaches = new ArrayList<>(beachNames.size());
        for (String beachName : beachNames) {
            sortedBeaches.add(beaches.get(beachName));
        }

        return ok(views.html.beaches.listBeaches.render(sortedBeaches, title));
    }

    public static Result show(String beachName) {
        String printBeachName = StringUtils.capitalizeFirstLetter(beachName);
        Beach beach = beaches.get(printBeachName);
        if (null == beach) {
            return badRequest("Bad Request.  Beach named" + beachName + " is not in our database");
        }
        return ok(views.html.beaches.showBeach.render(beach));
    }

    public static void fillDatastore() throws Exception {
        if (null != beaches)
            return;

        beaches = new HashMap<String, Beach>(100); // TODO: Set with number of beaches

        File theFile = Play.application().getFile("conf/beachClosings.csv");

        BufferedReader br = null;

        try {



            br = new BufferedReader(new FileReader(theFile));

            String sCurrentLine = br.readLine();

            // Skip the first line

            while ((sCurrentLine = br.readLine()) != null) {
                String [] beachDataSplit = sCurrentLine.split(";");

                addClosing(beachDataSplit[0], beachDataSplit[1]);

            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static void addClosing(String beachName, String dateString) {

        LocalDate closingDate = TimeUtils.convertStringToDate(dateString);

        Beach thisBeach = beaches.get(beachName);
        if (null == thisBeach) {
            thisBeach = new Beach(beachName, closingDate);
            beaches.put(beachName, thisBeach);
        }
        else {
            thisBeach.addClosing(closingDate);
            beaches.put(thisBeach.getName(), thisBeach);
        }

    }
}
