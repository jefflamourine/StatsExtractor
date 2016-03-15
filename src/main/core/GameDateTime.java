package main.core;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameDateTime {

    Calendar c = Calendar.getInstance();

    public GameDateTime() {

    }

    public void setDateFromString(String date) {
        int month = Integer.parseInt(date.substring(0, 2));
        int day = Integer.parseInt(date.substring(2, 4));
        int year = Integer.parseInt(date.substring(4, 6));
        int game = Integer.parseInt(date.substring(6, 7));

        if (year < 1000) {
            year += 2000;
        }

        int hours, minutes;

        switch (game) {
            case 1:
                hours = 19;
                minutes = 30;
                break;
            case 2:
                hours = 19;
                minutes = 50;
                break;
            case 3:
                hours = 20;
                minutes = 10;
                break;
            default:
                hours = 0;
                minutes = 0;
                break;
        }

        c.set(year, month - 1, day, hours, minutes, 0);
    }

    public String toString() {
        return new SimpleDateFormat("EEE MMMMM dd yyyy HH:mm:ss").format(c.getTime());
    }

}
