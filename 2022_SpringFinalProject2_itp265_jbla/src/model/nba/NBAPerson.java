package model.nba;

import java.util.Objects;

public abstract class NBAPerson implements Comparable<NBAPerson> {
	
	// instance variables
	protected String firstName;
	protected String lastName;
	protected TeamAbbreviation teamAbbreviation;
	
	// constructor
	public NBAPerson(String firstName, String lastName, TeamAbbreviation teamAbbreviation) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.teamAbbreviation = teamAbbreviation;
	}

	// accessors and mutators
	public TeamAbbreviation getTeamAbbreviation() {
		return teamAbbreviation;
	}

	public void setTeamAbbreviation(TeamAbbreviation teamAbbreviation) {
		this.teamAbbreviation = teamAbbreviation;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	// compareTo method to get NBA players by team
	
	@Override
	public int compareTo(NBAPerson other) {
		// ordering --> NBA Player by team and separated by last name, then first name
		int diff = this.teamAbbreviation.compareTo(other.teamAbbreviation);
		
		if (diff == 0) {
			diff = this.lastName.compareTo(other.lastName);
			if (diff == 0) {
				diff = this.firstName.compareTo(other.firstName);
			}
		}
		
		return diff;
	}

	// toString method to print person
	
	@Override
	public String toString() {
		return "[Name = " + firstName + " " + lastName + ", from " + teamAbbreviation + "; ";
	}

	// hashCode and equals method
	
	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, teamAbbreviation);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NBAPerson other = (NBAPerson) obj;
		return Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(teamAbbreviation, other.teamAbbreviation);
	}
	
	
}
