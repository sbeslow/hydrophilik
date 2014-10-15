package models;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import play.db.ebean.Model;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

public class Beach extends Model implements Comparable<Beach> {

    @Id
    private String name;

    private List<LocalDate> closings;

    public Beach(String beachName, LocalDate closingDate) {
        this.name = beachName;
        if (null == closings)
            closings = new ArrayList<>();

        closings.add(closingDate);


    }

    public void addClosing(LocalDate closing) {
        if (null == closings)
            closings = new ArrayList<>();

        closings.add(closing);
    }

    public String getName() {
        return name;
    }

    public List<LocalDate> getClosings() {
        return closings;
    }

    @Override
    public int compareTo(Beach o) {
        if (getClosings().size() > o.getClosings().size())
            return -1;
        else if (getClosings().size() < o.getClosings().size())
            return 1;

        return 0;

    }
}
