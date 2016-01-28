package main.core;

public class GameIdentity {

    String redTeamName, blueTeamName;
    GameDate date;

    public GameIdentity(String redTeamName, String blueTeamName, String rawDate) {
        this.redTeamName = redTeamName;
        this.blueTeamName = blueTeamName;
        if (rawDate == null) {
            this.date = null;
        } else {
            this.date = new GameDate();
            this.date.setDateFromString(rawDate);
        }
    }
}
