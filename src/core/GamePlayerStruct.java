package core;

public class GamePlayerStruct {

    int team;
    String name;
    int goals;
    int assists;
    int pos;

    GamePlayerStruct(int team, String name, int goals, int assists, int pos) {
        this.team = team;
        this.name = name;
        this.goals = goals;
        this.assists = assists;
        this.pos = pos;
    }

    public boolean isPlaying() {
        return pos != -1;
    }
}