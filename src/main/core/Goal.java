package main.core;

import org.json.JSONObject;

public class Goal {

    String scorer;
    String assister;
    int time;
    int period;
    String team;

    public Goal(String scorer, String assister, int time, int period, int team) {
        this.scorer = scorer;
        this.assister = assister;
        this.time = time;
        this.period = period;
        if (team == 0) {
            this.team = "red";
        } else {
            this.team = "blue";
        }
    }

    @Override
    public String toString() {
        return "Goal [scorer=" + scorer + ", assister=" + assister + ", time="
                + time + ", period=" + period + ", team=" + team + "]";
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("scorer", scorer);
        json.put("assister", assister);
        json.put("team", team);
        json.put("period", period);
        json.put("time", time);
        return json;
    }
}
