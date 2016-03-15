package main.core;

import org.json.JSONObject;

public class Performance {

    int team, goals, assists, toi, plusminus;

    public Performance(int team) {
        this(team, 0, 0, 1, 0);
    }

    public Performance(int team, int goals, int assists, int toi, int plusminus) {
        this.team = team;
        this.goals = goals;
        this.assists = assists;
        this.toi = toi;
        this.plusminus = plusminus;
    }

    @Override
    public String toString() {
        return "Performance [team=" + team + ", goals=" + goals + ", assists=" + assists + ", toi=" + toi + ", plusminus=" + plusminus + "]";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("team", team);
        json.put("goals", goals);
        json.put("assists", assists);
        json.put("toi", toi);
        json.put("plusminus", plusminus);
        return json;
    }
}
