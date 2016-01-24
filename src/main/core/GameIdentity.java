package main.core;

public class GameIdentity {

    String redTeamName, blueTeamName;
    GameDate date;

    public GameIdentity(String redTeamName, String blueTeamName, String rawDate) {
        this.redTeamName = redTeamName;
        this.blueTeamName = blueTeamName;
        this.date = new GameDate();
        this.date.setDateFromString(rawDate);
    }
}
