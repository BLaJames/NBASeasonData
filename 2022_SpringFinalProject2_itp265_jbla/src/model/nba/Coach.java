package model.nba;

import java.util.Scanner;

public class Coach extends NBAPerson {
	
	// instance variables
	private int numSeason;
	private double winPercent;
	
	// constructor
	public Coach(String firstName, String lastName, TeamAbbreviation teamAbbreviation, int numSeason, double winPercent) {
		super(firstName, lastName, teamAbbreviation);
		this.numSeason = numSeason;
		this.winPercent = winPercent;
	}

	// accessors and mutators
	public int getNumSeason() {
		return numSeason;
	}

	public void setNumSeason(int numSeason) {
		this.numSeason = numSeason;
	}

	public double getWinPercent() {
		return winPercent;
	}

	public void setWinPercent(double winPercent) {
		this.winPercent = winPercent;
	}

	// toString method to print NBA coach
	
	@Override
	public String toString() {
		return "Coach: " + super.toString() + this.numSeason + " season(s) of experience with a "
				+ this.winPercent + " win percentage" + "]";
	}
	
	public static Coach parseLineToCoach(String line, Scanner ls) {
		ls.useDelimiter(",");
		
		String firstName = ls.next();
		String lastName = ls.next();
		
		String abbString = ls.next();
		TeamAbbreviation teamAbb = TeamAbbreviation.matchAbb(abbString);
		
		int numSeason = ls.nextInt();
		double winPercent = ls.nextDouble();
		
		return new Coach(firstName, lastName, teamAbb, numSeason, winPercent);
	}
}
