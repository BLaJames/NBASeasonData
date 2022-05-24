package model.user;

import java.util.Scanner;

import model.nba.Player;
import model.nba.Position;
import model.nba.TeamAbbreviation;
import view.Helper;

public class Free extends User implements Password {
	
	// instance variables
	private String txtFile;
	
	// constructor
	public Free(String firstName, String lastName, String userName, 
			String password, UserType userType, String txtFile) {
		super(firstName, lastName, userName, password, userType);
		this.txtFile = txtFile;
	}

	// accessor and mutator
	public String getTxtFile() {
		return txtFile;
	}
	
	public void setTxtFile(String txtFile) {
		this.txtFile = txtFile;
	}

	// method from interface to verify premium user's password
	@Override
	public boolean verifyPassword(Helper help) {
		// initalize boolean to determine whether password is valid
		boolean isUser = false;
			
		// allow for user input
		String userPassword = help.inputLine("Please enter your password: ");
		// only advance to PIN if password is correct
		if (!userPassword.equals(this.getPassword())) {
			System.out.println("Invalid password");
		} else {
			isUser = true;
		}
			
		return isUser;
	}
	
	// parse each line in .txt file to free user
	public static Free parseLinetoFree(String line, Scanner ls) {
		// initialize scanner with comma as delimiter
		ls.useDelimiter(",");
		
		// go through entire line to create new Free object
		String firstName = ls.next();
		String lastName = ls.next();
	
		String userName = ls.next();
		String password = ls.next();
		
		String userTypeString = ls.next();
		UserType userType = UserType.matchStringToEnum(userTypeString);
		
		String txtFile = ls.next();
		
		return new Free(firstName, lastName, userName, password, userType, txtFile);
	}
}
