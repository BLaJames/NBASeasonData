package model.user;

import java.util.List;
import java.util.Scanner;

import view.Helper;

public class Premium extends User implements Password {

	// instance variable
	private String pin;
	private String txtFile;
	
	// constructor
	public Premium(String firstName, String lastName, String userName, 
			String password, UserType userType, String pin, String txtFile) {
		super(firstName, lastName, userName, password, userType);
		this.pin = pin;
		this.txtFile = txtFile;
	}

	// accessors and mutators
	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getTxtFile() {
		return txtFile;
	}

	public void setTxtFile(String txtFiles) {
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
			String userPin = help.inputLine("Correct password. Please enter your PIN for two-step verification:");
			
			if (!userPin.equalsIgnoreCase(this.getPin())) {
				System.out.println("Invalid PIN");
			} else {
				isUser = true;
			}
		}
		
		return isUser;
	}
	
	// parse each line in .txt file to free user
	public static Premium parseLinetoPremium(String line, Scanner ls) {
		// initialize scanner with comma as delimiter
		ls.useDelimiter(",");
			
		// go through entire line to create new Free object
		String firstName = ls.next();
		String lastName = ls.next();
		
		String userName = ls.next();
		String password = ls.next();
			
		String userTypeString = ls.next();
		UserType userType = UserType.matchStringToEnum(userTypeString);
		
		String pin = ls.next();
			
		String txtFile = ls.next();
			
		return new Premium(firstName, lastName, userName, password, userType, pin, txtFile);
	}

	
	
}
