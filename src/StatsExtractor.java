import java.util.ArrayList;

public class StatsExtractor {
	
	private static long TIME = 0x7D349A8L;
	private static long PERIOD = 0x7D349B0L;
	private static long RED_SCORE = 0x7D33D98L;
	private static long BLUE_SCORE = 0x7D33D9CL;
	private static long PLAYER_STRUCT = 0x04A5B860L;
	private static long PLAYER_POSTION_OFFSET = 4;
	private static long PLAYER_TEAM_OFFSET = 8;
	private static long PLAYER_NAME_OFFSET = 20;
	private static long PLAYER_GOALS_OFFSET = 136;
	private static long PLAYER_ASSISTS_OFFSET = 140;
    private static long PLAYER_FULL_OFFSET = 152;

	public static void init() {
		MemoryExtractor.init();
	}
	
	public static int getTime() {
		return MemoryExtractor.readMemory(TIME, 4).getInt(0);
	}
	
	public static int getPeriod() {
		return MemoryExtractor.readMemory(PERIOD, 4).getInt(0);
	}
	
	public static int getRedScore() {
		return MemoryExtractor.readMemory(RED_SCORE, 4).getInt(0);
	}
	
	public static int getBlueScore() {
		return MemoryExtractor.readMemory(BLUE_SCORE, 4).getInt(0);
	}
	
	public static String getName(long fromMemLoc) {
		return MemoryExtractor.readMemory(fromMemLoc + PLAYER_NAME_OFFSET, 32).getString(0);
	}
	
	public static int getGoals(long fromMemLoc) {
		return MemoryExtractor.readMemory(fromMemLoc + PLAYER_GOALS_OFFSET, 4).getInt(0);
	}
	
	public static int getAssists(long fromMemLoc) {
		return MemoryExtractor.readMemory(fromMemLoc + PLAYER_ASSISTS_OFFSET, 4).getInt(0);
	}
	
	public static int getTeam(long fromMemLoc) {
		return MemoryExtractor.readMemory(fromMemLoc + PLAYER_TEAM_OFFSET, 4).getInt(0);
	}
	
	public static ArrayList<Player> getPlayers() {
		return getPlayers(PLAYER_STRUCT, new ArrayList<Player>());
	}
	
	public static ArrayList<Player> getPlayers(long fromMemLoc, ArrayList<Player> players) {
		String name = getName(fromMemLoc);
		if (name.isEmpty()) {
			return players;
		}
		
		int goals = getGoals(fromMemLoc);
		int assists = getGoals(fromMemLoc);
		int team = getTeam(fromMemLoc);
		
		players.add(new Player(goals, assists, team, name));
		return getPlayers(fromMemLoc + PLAYER_FULL_OFFSET, players);
	}
	

}
