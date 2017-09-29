package knikolov.goldtracker;

/**
 * Created by Konik on 9/24/17.
 */

public class EntryTemplate {
    String name;
    String date;
    float sum;

    public EntryTemplate(String name, String date, float sum) {
        this.name = name;
        this.date = date;
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public float getSum() {
        return sum;
    }

}
