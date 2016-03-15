package main.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The fields used to uniquely identify a Hockey? league game.
 * Constructed using a raw date string 'MMDDYY'
 */
public class GameIdentity {

    public String redTeamName, blueTeamName, date;

    public GameIdentity(String redTeamName, String blueTeamName, String rawDate) {
        this.redTeamName = redTeamName;
        this.blueTeamName = blueTeamName;
        if (rawDate == null) {
            this.date = null;
        } else {
            SimpleDateFormat input = new SimpleDateFormat("MMddyy");
            SimpleDateFormat output = new SimpleDateFormat("EEE MMMMM dd yyyy");
            try {
                this.date = output.format(input.parse(rawDate));
            } catch (ParseException e) {
                this.date = null;
            }
        }
    }
}
