import java.util.ArrayList;

public class StatsTracker {
	
	public static void main(String... args) {
		StatsExtractor.init();
		int time = StatsExtractor.getTime();
		int period = StatsExtractor.getPeriod();
		int redScore = StatsExtractor.getRedScore();
		int blueScore = StatsExtractor.getBlueScore();
		ArrayList<Player> players = StatsExtractor.getPlayers();
		System.out.println(time / 100 / 60 +  ":" + time / 100 % 60);
		System.out.println(period);
		System.out.println(redScore);
		System.out.println(blueScore);
		for (Player p : players) {
			System.out.println(p.toString());
		}
	}
}
