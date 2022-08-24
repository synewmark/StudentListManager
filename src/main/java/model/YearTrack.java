package model;

import static model.Track.AI;
import static model.Track.DS;

public enum YearTrack {

	FRESHMAN(1, null), SOPHMOREBA(2, null), SOPHMOREAI(2, AI), SOPHMOREDS(2, DS), JUNIORBA(3, null), JUNIORAI(3, AI),
	JUNIORDS(3, DS), SENIORAI(4, AI), SENIORDS(4, DS);

	public final int year;
	public final Track track;
	public final String prefix;

	YearTrack(int year, Track track) {
		this.year = year;
		this.track = track;
		if (this.toString().equalsIgnoreCase("Freshman")) {
			this.prefix = this.toString();
		} else {
			this.prefix = this.toString().substring(0, this.toString().length() - 2);
		}
	}

	public static int yearsFromGraduating(YearTrack yearTrack) {
		return yearsFromGraduating(yearTrack.prefix);
	}

	public static int yearsFromGraduating(String prefix) {
		switch (prefix.toLowerCase()) {
		case "freshman":
			return 4;
		case "sophmore":
			return 3;
		case "junior":
			return 2;
		case "senior":
			return 1;
		default:
			throw new IllegalArgumentException();
		}
	}

}