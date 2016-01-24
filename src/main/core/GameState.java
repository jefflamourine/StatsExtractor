package main.core;

import java.util.ArrayList;

public class GameState {

    int redScore, blueScore, time, period;
    ArrayList<GamePlayerStruct> players;
    
    public GameState() {
        redScore = StatsExtractor.getRedScore();
        blueScore = StatsExtractor.getBlueScore();
    }
    
    public GameState (GameState state) {
        redScore = state.redScore;
        blueScore = state.blueScore;
        time = state.time;
        period = state.period;
        players = state.players;
    }
    
    void update() {
        time = StatsExtractor.getTime();
        period = StatsExtractor.getPeriod();
        redScore = StatsExtractor.getRedScore();
        blueScore = StatsExtractor.getBlueScore();
        players = StatsExtractor.getPlayers();
    }
    
    /**
     * Check for the end of the game
     * Period value increases at the start of intermission not the next period
     * it appears: time = 0 if game ends in regulation
     *             time = 1 if goal is scored in OT
     */
    public boolean endOfGame() {
        return period > 3 && redScore != blueScore;
    }
}
