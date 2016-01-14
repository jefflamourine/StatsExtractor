package core;

public class Performance {

	int team, goals, assists, toi;
	
	public Performance(int team) {
		this(team, 0, 0, 1);
	}

	public Performance(int team, int goals, int assists, int toi) {
		this.team = team;
		this.goals = goals;
		this.assists = assists;
		this.toi = toi;
	}
	
	@Override
	public String toString() {
		return "Performance [team=" + team + ", goals=" + goals + ", assists="
				+ assists + ", toi=" + toi + "]";
	}
}
