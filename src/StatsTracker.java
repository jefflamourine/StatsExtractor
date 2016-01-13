public class StatsTracker {

	static long lastTickTime = 0;
	static int lastGameTime = 0;
	static boolean tracking = true;

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

		while (tracking) {
			long currentTime = System.currentTimeMillis();

			if (currentTime - lastTickTime >= 1000) {
				lastTickTime = currentTime;

				int time = StatsExtractor.getTime();
				int period = StatsExtractor.getPeriod();
				int redScore = StatsExtractor.getRedScore();
				int blueScore = StatsExtractor.getBlueScore();

				// Period value increases at start of intermission not next period
				// it appears: time = 0 if game ends in regulation
				// 			   time = 1 if goal is scored in OT
				if (period > 3 && redScore != blueScore) {
					tracking = false;
				}

				System.out.println(formatTime(time));
			}
		}

		System.out.println("GAME OVER");
	}
}