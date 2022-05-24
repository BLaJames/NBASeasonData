package model.nba;

public enum Position {

	// constants
	GUARD,
	GUARD_FORWARD,
	FORWARD,
	FORWARD_GUARD,
	FORWARD_CENTER,
	CENTER,
	CENTER_FORWARD,
	NONE;
	
	// matching string to Position enum
	public static Position matchStringToPosition(String s) {
		Position position = NONE; // set initial value to NONE
		s = s.replace("-", "_"); // change format of string to match enum
		
		// for loop to loop through all Position enums to find match
		for (Position p: Position.values()) {
			if (s.equalsIgnoreCase(p.toString())) {
				position = p;
			}
		}
		
		// print error message if no enum matches
		if (position == NONE) {
			System.err.println("Could not match " + s + " to any Position enums");
		}
		
		return position;
	}
	
}
