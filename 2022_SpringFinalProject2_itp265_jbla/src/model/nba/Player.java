package model.nba;

import java.util.Scanner;

public class Player extends NBAPerson {

	// instance variables
	private Position position;
	private int number;
	private int height;
	
	// constructors
	public Player(String firstName, String lastName, TeamAbbreviation teamAbbreviation, Position position, int number, int height) {
		super(firstName, lastName, teamAbbreviation);
		this.position = position;
		this.number = number;
		this.height = height;
	}

	// accessors and mutators
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	// toString method to print NBA player
	
	@Override
	public String toString() {
		return "Player: " + super.toString() + this.height + " inch " + this.position + " who wears #" + this.number + "]";
	}

	// parse each line in .txt file to Player
	public static Player parseLineToPlayer(String line, Scanner ls) {
		// initialize scanner with comma as delimiter
		ls.useDelimiter(",");
		
		// go through entire line to create new Player object
		String firstName = ls.next();
		String lastName = ls.next();
		
		String team = ls.next();
		TeamAbbreviation teamAbb = TeamAbbreviation.teamToAbb(team);
		
		String abbStr = ls.next();
		
		String posString = ls.next();
		Position position = Position.matchStringToPosition(posString);
		
		int number = ls.nextInt();
		int height = ls.nextInt();
		
		return new Player(firstName, lastName, teamAbb, position, number, height);
	}
}
