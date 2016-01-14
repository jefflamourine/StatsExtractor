package core;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class StatsTracker {

	// Time of last memory read
	static long lastTickTime = 0;
	// Is a game being tracked?
	static boolean tracking = true;
	
	// Map of players who have played, i.e. those that should be shown in output
	// to their performance in the game
	static HashMap<String, Performance> players = new HashMap<String, Performance>();
	// Lists of goals, to be output
	static ArrayList<Goal> redGoals = new ArrayList<Goal>();
	static ArrayList<Goal> blueGoals = new ArrayList<Goal>();
	
	// Scores from last tick
	static int previousRedScore, previousBlueScore;
	// Game time from last tick
	static int previousTime;
	// Lists of player structs in game memory i.e. those that are currently in game
	// From last tick
	static ArrayList<GamePlayerStruct> previousGamePlayers = new ArrayList<GamePlayerStruct>();
	// From current tick
	static ArrayList<GamePlayerStruct> gamePlayers = new ArrayList<GamePlayerStruct>();

	/**
	 * Turn a game time integer into a pretty readable time
	 * @param time
	 * @return
	 */
	public static String formatTime(int time) {
		int totalSeconds = time / 100;
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds - (minutes * 60);

		// Add a leading zero to seconds if < 10
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}

	public static void main(String... args) {
		StatsExtractor.init();
		
		// Initial previous scores to score when tracker starts
		previousRedScore = StatsExtractor.getRedScore();
		previousBlueScore = StatsExtractor.getBlueScore();
		
		while (tracking) {
			long currentTime = System.currentTimeMillis();

			if (currentTime - lastTickTime >= 1000) {
								
				lastTickTime = currentTime;
				
				int time = StatsExtractor.getTime();
				int period = StatsExtractor.getPeriod();
				int redScore = StatsExtractor.getRedScore();
				int blueScore = StatsExtractor.getBlueScore();

				gamePlayers = StatsExtractor.getPlayers();
				
				if (previousTime / 100 != time / 100) {
					for (GamePlayerStruct gps : gamePlayers) {
						if (gps.isPlaying()) {
							String name = gps.name;
							if (players.containsKey(name)) {
								players.get(name).toi++;
							} else {
								players.put(name, new Performance(gps.team));
							}
						}
					}
				}
				
				// If score has changed
				
				if (redScore != previousRedScore) {
					addGoal(0, time, period);
					previousRedScore = redScore;
					previousBlueScore = blueScore;
				}
				
				if (blueScore != previousBlueScore) {
					addGoal(1, time, period);
					previousRedScore = redScore;
					previousBlueScore = blueScore;
				}
				
				previousGamePlayers = gamePlayers;
				previousTime = time;
				
				// Period value increases at start of intermission not next period
				// it appears: time = 0 if game ends in regulation
				// 			   time = 1 if goal is scored in OT
				if (period > 3 && redScore != blueScore) {
					tracking = false;
				}
			}
		}

		System.out.println("GAME OVER --- RED: " + redGoals.size() + " BLUE: " + blueGoals.size());
		
		for (Goal g : redGoals) {
			if (!g.scorer.equals("")) {
				players.get(g.scorer).goals++;
			}
			if (!g.assister.equals("")) {
				players.get(g.assister).assists++;
			}
			System.out.println(g.toString());
		}
		
		for (Goal g : blueGoals) {
			if (!g.scorer.equals("")) {
				players.get(g.scorer).goals++;
			}
			if (!g.assister.equals("")) {
				players.get(g.assister).assists++;
			}
			System.out.println(g.toString());
		}
		
		for (Entry<String, Performance> e : players.entrySet()) {
			System.out.println(e.getKey() + ": " + e.getValue().toString());
		}
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void addGoal(int team, int time, int period) {
			String scorer = "", assister = "";
			
			// Find the name of the scorer and the assister, update +/-
			for (GamePlayerStruct current : gamePlayers) {
				if (current.isPlaying()) {
					if (current.team == team) {
						players.get(current.name).plusminus++;
					} else {
						players.get(current.name).plusminus--;
					}
				}
				for (GamePlayerStruct previous : previousGamePlayers) {
					if (current.name.equals(previous.name)) {
						if (current.goals != previous.goals) {
							scorer = current.name;
						}
						if (current.assists != previous.assists) {
							assister = current.name;
						}
					}
				}
			}
			
			if (team == 0) {
				redGoals.add(new Goal(scorer, assister, time, period, 0));
			} else {
				blueGoals.add(new Goal(scorer, assister, time, period, 1));
			}
	}
}