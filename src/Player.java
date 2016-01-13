public class Player {

	public int goals;
	public int assists;
	public int team;
	public String name;
	
	public Player(int goals, int assists, int team, String name) {
		this.goals = goals;
		this.assists = assists;
		this.team = team;
		this.name = name;
	}
	
	public String toString() {
		return name + ": " + teamString(team) + ", " + goals + "g, " + assists + "a";
	}
	
	private String teamString(int team) {
		switch (team){
			case 0:
				return "Blue";
			case 1:
				return "Red";
			default:
				return "Not skating";	
		}
	}
}
