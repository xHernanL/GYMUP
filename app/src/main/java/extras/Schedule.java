package extras;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String date;
    private String hour;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Schedule(String date, String hour) {
        this.date = date;
        this.hour = hour;
    }
}
