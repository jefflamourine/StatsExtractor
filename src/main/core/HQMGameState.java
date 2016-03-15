package main.core;

import java.util.ArrayList;

/**
 * Representation of the instantaneous state of a Hockey? game.
 */
public class HQMGameState {

    int redScore, blueScore, time, period;
    ArrayList<HQMPlayerStruct> players;

    public HQMGameState() {
        redScore = StatsExtractor.getRedScore();
        blueScore = StatsExtractor.getBlueScore();
    }

    public HQMGameState(HQMGameState state) {
        redScore = state.redScore;
        blueScore = state.blueScore;
        time = state.time;
        period = state.period;
        players = state.players;
    }

    /**
     * Updates the game state with the current data from memory
     */
    void update() {
        time = StatsExtractor.getTime();
        period = StatsExtractor.getPeriod();
        redScore = StatsExtractor.getRedScore();
        blueScore = StatsExtractor.getBlueScore();
        players = StatsExtractor.getPlayers();
    }

    /**
     * Check for the end of the game. Period value increases at the start of
     * intermission not the next period. It appears: time = 0 if game ends in
     * regulation time = 1 if goal is scored in OT
     */
    public boolean endOfGame() {
        return period > 3 && redScore != blueScore;
    }
}
