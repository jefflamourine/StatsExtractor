package core;

import java.util.ArrayList;

public class GameState {

	ArrayList<GamePlayerStruct> players;
	int redScore;
	int blueScore;
	
	public GameState() {
		this.players = new ArrayList<GamePlayerStruct>();
		this.redScore = 0;
		this.blueScore = 0;
	}
}