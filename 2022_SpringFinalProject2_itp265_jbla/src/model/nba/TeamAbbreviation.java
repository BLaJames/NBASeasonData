package model.nba;

public enum TeamAbbreviation {

	// constants
	ATL("Hawks"),
	BKN("Nets"),
	BOS("Celtics"),
	CHA("Hornets"),
	CHI("Bulls"),
	CLE("Cavaliers"),
	DAL("Mavericks"),
	DEN("Nuggets"),
	DET("Pistons"),
	GSW("Warriors"),
	HOU("Rockets"),
	IND("Pacers"),
	LAC("Clippers"),
	LAL("Lakers"),
	MEM("Grizzlies"),
	MIA("Heat"),
	MIL("Bucks"),
	MIN("Timberwolves"),
	NOP("Pelicans"),
	NYK("Knicks"),
	OKC("Thunder"),
	ORL("Magic"),
	PHI("Sixers"),
	PHX("Suns"),
	POR("Blazers"),
	SAC("Kings"),
	SAS("Spurs"),
	TOR("Raptors"),
	UTA("Jazz"),
	WAS("Wizards"),
	NONE("N/A");
	
	// instance variable
	public final String teamName;
	
	private TeamAbbreviation(String teamName) {
		this.teamName = teamName;
	}
	
	// matches string to teamName, which gets enum 
	public static TeamAbbreviation teamToAbb(String team) {
		TeamAbbreviation teamAbb = NONE;
		
		// loop through all enums
		for (TeamAbbreviation t: TeamAbbreviation.values()) {
			// check if string matches any teamName
			if (t.teamName.equalsIgnoreCase(team)) {
				teamAbb = t; 
			}
		}
		
		// error message if no teamName matches string
		if (teamAbb == NONE) {
			System.err.println("Could not match " + team + " to any TeamAbbreviation enums");
		}
		
		return teamAbb;
	}
	
	// matches teamAbbreviation enum to a string
	public static TeamAbbreviation matchAbb(String abb) {
		TeamAbbreviation teamAbb = NONE;
		
		// loop through all enums
		for (TeamAbbreviation t: TeamAbbreviation.values()) {
			// check if string matches any enum
			if (t.toString().equalsIgnoreCase(abb)) {
				teamAbb = t;
			}
		}
		
		// error message if no enum matches string
		if (teamAbb == NONE) {
			System.err.println("Could not match " + abb + " to any TeamAbbreviation enums");
		}
		
		return teamAbb;
	}
	
}
