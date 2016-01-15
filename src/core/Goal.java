package core;

public class Goal {

	String scorer;
	String assister;
	int time;
	int period;
	int team;

	public Goal(String scorer, String assister, int time, int period, int team) {
		this.scorer = scorer;
		this.assister = assister;
		this.time = time;
		this.period = period;
		this.team = team;
	}

	@Override
	public String toString() {
		return "Goal [scorer=" + scorer + ", assister=" + assister + ", time="
				+ time + ", period=" + period + ", team=" + team + "]";
	}
}
