package model.user;

public enum UserType {
	
	// constants
	FREE,
	PREMIUM,
	NONE;
	
	// matching string to enum
	public static UserType matchStringToEnum(String s) {
		UserType userAccess = NONE;
		
		// for loop to loop through all enum values
		for (UserType t: UserType.values()) {
			// set userAccess to userType if match is found
			if (t.toString().equalsIgnoreCase(s)) {
				userAccess = t;
			}
		}
		
		// error statement if no match
		if (userAccess == NONE) {
			System.err.println("Could not match " + s + " to any UserType enum");
		}
		
		return userAccess;
	}

}
