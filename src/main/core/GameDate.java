package main.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameDate {

    Calendar c = Calendar.getInstance();

    public GameDate(int year, int month, int day, int hours, int minutes) {
        if (year < 1000) {
            year += 2000;
        }
        c.set(year, month - 1, day, hours, minutes, 0);
    }

    public String toString() {
        return new SimpleDateFormat("EEE MMMMM dd yyyy HH:mm:ss").format(c.getTime());
    }
    
}
