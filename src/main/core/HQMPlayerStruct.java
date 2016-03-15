package main.core;

/**
 * Representation of a player in Hockey? Memory
 *
 */
public class HQMPlayerStruct {

    int team;
    String name;
    int goals;
    int assists;
    int pos;

    HQMPlayerStruct(int team, String name, int goals, int assists, int pos) {
        this.team = team;
        this.name = name;
        this.goals = goals;
        this.assists = assists;
        this.pos = pos;
    }

    /**
     * Is the player currently on the ice?
     */
    public boolean isPlaying() {
        return pos != -1;
    }
}