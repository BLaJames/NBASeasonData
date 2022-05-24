package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.nba.*;
import model.user.*;
import view.Helper;

public class FinalProgram {

	// instance variables
	private Helper help = new Helper();
	private Map<String, UserType> userMap;
	private Map<TeamAbbreviation, List<Player>> playerMap;
	private Map<TeamAbbreviation, Coach> coachMap;
	private Map<String, Free> freeMap;
	private Map<String, Premium> premiumMap;
	
	// constructor
	public FinalProgram() {
		playerMap = new HashMap<>();
		coachMap = new HashMap<>();
		freeMap = new HashMap<>();
		premiumMap = new HashMap<>();
		userMap = new HashMap<>();
	}
	
	// method to run program
	private void run() {
		// display welcome message
		System.out.println("Welcome to the NBA program: 2018-2019 Season Edition");
		System.out.println(); // formatting
		
		// create maps for all users
		freeMap = DataBaseFileReader.readFreeFile();
		premiumMap = DataBaseFileReader.readPremiumFile();
		
		userMap = createUserMap(freeMap, premiumMap);
		
		// determine whether user is already in database
		boolean isCurrentUser = help.inputYesNo("Are you already registed in the system? ");
		System.out.println(); // formatting
		
		// determine if user is in database
		if (isCurrentUser) {
			String userName = help.inputWord("Please enter a username");
			
			boolean isValid = checkUser(userMap, userName);
			
			// verify password if user is valid
			if (isValid) {
				// branching depending on whether valid user is free or premium
				if (userMap.get(userName).toString().equalsIgnoreCase("free")) {
					Free freeUser = freeMap.get(userName);
					boolean validPassword = freeUser.verifyPassword(help);
					
					// initalize variable to continue guess
					boolean continueGuess = true;
					// get user's correct password until they decide to make new account or guess password correct
					while (!validPassword && continueGuess) {
						continueGuess = help.inputYesNo("Incorrect password. Do you want to continue guessing password?");
						
						if (continueGuess) {
							// try again with password
							validPassword = freeUser.verifyPassword(help);
						// create new user and run program if user does not want to continue guessing password
						} else {
							System.out.println("Creating new user.");
							continueGuess = false;
						}
					}
					
					// branching to determine if new or returning user
					if (!validPassword) {
						System.out.println(); // formatting
						runNewUserProgram();
					} else {
						// run game for free user if authorized --> returning game that utilizes saved file
						System.out.println(); // formatting
						runReturningFree(freeUser);
					}
					
				} else {
					Premium premiumUser = premiumMap.get(userName);
					boolean validPassword = premiumUser.verifyPassword(help);
					
					// initialize variable to continue guess
					boolean continueGuess = true;
					// get user's correct password until they decide to make new account or guess password correct
					while (!validPassword && continueGuess) {
						continueGuess = help.inputYesNo("Do you want to continue guessing password?");
						
						if (continueGuess) {
							// try again with password
							validPassword = premiumUser.verifyPassword(help);
						// create new user and run program if user does not want to continue guessing password
						} else {
							System.out.println("Creating new user.");
							continueGuess = false;
						}
					}
					
					// branching to determine if new or returning user
					if (!validPassword) {
						System.out.println(); // formatting
						runNewUserProgram();
					} else {
						// run game for premium user if authorized --> returning game that utilizes saved file
						System.out.println(); // formatting
						runReturningPremium(premiumUser);
					}
				}
			// automatically make new user if they enter foreign user name
			} else {
				System.out.println("Username is not in our database. Creating new user.");
				System.out.println(); // formatting
				runNewUserProgram();
			}
		} else {
			runNewUserProgram();
		}
	}

	// returning premium user program (allows premium users to user their own player file)
	private void runReturningPremium(Premium premiumUser) {
		// ask user if they want to use their existing file
				boolean useUser = help.inputYesNo("Would you like to use your saved player file? ");
				
				// get all player and coach data
				Collection<Player> allPlayers; // declare collection for all players
				
				// get player data depending on user's choice
				if (useUser) {
					allPlayers = DataBaseFileReader.readPlayerFile(premiumUser.getTxtFile() + ".txt");
				} else {
					allPlayers = DataBaseFileReader.readPlayerFile();
				}
				
				Collection<Coach> allCoaches = DataBaseFileReader.readCoachFile();
				
				// create both general maps
				playerMap = createPlayerMap(allPlayers);
				coachMap = getCoachMap(allCoaches);
				
				// create count for number of players created
				int createPlayerCount = 0;
				
				// initialize boolean to continue program
				boolean continueSim = true;
				
				// run while user wants to continue game 
				while (continueSim) {
						// free user capabilities
						int userChoice = displayOptions("premium");
						switch (userChoice) {
							case 0:
								displayPlayerMap(playerMap);
								System.out.println(); // formatting
								break;
							case 1:
								displayCoachMap(coachMap);
								System.out.println(); // formatting
								break;
							case 2:
								TeamAbbreviation userABB = getUserABB();
								displayTeam(userABB);
								System.out.println(); // formatting
								break;
							case 3:
								int jerseyNum = getUserNum();
								displayJersey(jerseyNum, allPlayers);
								System.out.println(); // formatting
								break;
							case 4:
								int heightMin = getHeightMin();
								int heightMax = getHeightMax(heightMin);
								displayHeight(heightMin, heightMax, allPlayers);
								System.out.println(); // formatting
								break;
							case 5:
								Position userPosition = getUserPosition();
								displayPosition(userPosition, allPlayers);
								System.out.println(); // formatting
								break;
							case 6:
								displayGameSimulator();
								
								// get all factors from user
								double coachWinFactor = getCoachWinFactor();
								double coachExpFactor = getCoachExpFactor(coachWinFactor);
								double teamHeightFactor = displayTeamHeightFactor(coachWinFactor, coachExpFactor);
								
								// get user's teams
								TeamAbbreviation firstTeam = getUserABB();
								TeamAbbreviation secondTeam = getUserABB();
								
								// play simulation
								simulateGame(coachWinFactor, coachExpFactor, teamHeightFactor, firstTeam, secondTeam);
								System.out.println(); // formatting
								break;
							case 7:
								displayCoachMatchup();
								
								// get all factors from user
								double winFactor = getCoachWinFactor();
								
								double expFactor = 1.0 - winFactor;
								System.out.printf("After calculations, given your previous percentages of importance, the factor of coach's experience is %.2f\n" , expFactor);
								
								// get user's teams
								TeamAbbreviation team1 = getUserABB();
								TeamAbbreviation team2 = getUserABB();
								
								// play matchup simulator
								simulateMatchup(winFactor, expFactor, team1, team2);
								System.out.println(); // formatting
								break;
							case 8:
								// make sure that a limit is imposed for premium players as well
								if (createPlayerCount < 5) {
									Player userPlayer = createCustomPlayer();
									
									// ask if user wants to add player to rosters
									boolean addPlayer = help.inputYesNo("Do you want to add player to roster?");
									
									// add player to file if desired
									if (addPlayer) {
										// check if file is already created
										boolean fileCreated = createNewFile(premiumUser.getTxtFile());
										
										if (fileCreated) {
											// add all current players for new file
											addCurrentPlayersToFile(allPlayers, premiumUser.getTxtFile());
										}
										
										// add new player
										addNewPlayerToFile(premiumUser.getTxtFile(), userPlayer);
									}
									createPlayerCount++;
								} else {
									System.out.println("You have exceeded your player creation limit (5 per session for free users)");
								}
								System.out.println(); // formatting
								break;
						}
						
						// ask if user wants to continue
						continueSim = help.inputYesNo("Do you want to continue simulating?");
						System.out.println(); // formatting
				}
	}

	// returning free user program (allows free user to use their own player file)
	private void runReturningFree(Free freeUser) {
		// ask user if they want to use their existing file
		boolean useUser = help.inputYesNo("Would you like to use your saved player file? ");
		
		// get all player and coach data
		Collection<Player> allPlayers; // declare collection for all players
		
		// get player data depending on user's choice
		if (useUser) {
			allPlayers = DataBaseFileReader.readPlayerFile(freeUser.getTxtFile() + ".txt");
		} else {
			allPlayers = DataBaseFileReader.readPlayerFile();
		}
		
		Collection<Coach> allCoaches = DataBaseFileReader.readCoachFile();
		
		// create both general maps
		playerMap = createPlayerMap(allPlayers);
		coachMap = getCoachMap(allCoaches);
		
		// create count for number of players created
		int createPlayerCount = 0;
		
		// initialize boolean to continue program
		boolean continueSim = true;
		
		// run while user wants to continue game 
		while (continueSim) {
				// free user capabilities
				int userChoice = displayOptions("free");
				switch (userChoice) {
					case 0:
						displayPlayerMap(playerMap);
						System.out.println(); // formatting
						break;
					case 1:
						displayCoachMap(coachMap);
						System.out.println(); // formatting
						break;
					case 2:
						TeamAbbreviation userABB = getUserABB();
						displayTeam(userABB);
						System.out.println(); // formatting
						break;
					case 3:
						int jerseyNum = getUserNum();
						displayJersey(jerseyNum, allPlayers);
						System.out.println(); // formatting
						break;
					case 4:
						int heightMin = getHeightMin();
						int heightMax = getHeightMax(heightMin);
						displayHeight(heightMin, heightMax, allPlayers);
						System.out.println(); // formatting
						break;
					case 5:
						Position userPosition = getUserPosition();
						displayPosition(userPosition, allPlayers);
						System.out.println(); // formatting
						break;
					case 6:
						// make sure user does not exceed maximum
						if (createPlayerCount < 2) {
							Player userPlayer = createCustomPlayer();
							
							// ask if user wants to add player to rosters
							boolean addPlayer = help.inputYesNo("Do you want to add player to roster?");
							
							// add player to file if desired
							if (addPlayer) {
								// check if file is already created
								boolean fileCreated = createNewFile(freeUser.getTxtFile());
								
								if (fileCreated) {
									// add all current players for new file
									addCurrentPlayersToFile(allPlayers, freeUser.getTxtFile());
								}
								
								// add new player
								addNewPlayerToFile(freeUser.getTxtFile(), userPlayer);
							}
							createPlayerCount++;
						} else {
							System.out.println("You have exceeded your player creation limit (2 per session for free users)");
						}
						System.out.println(); // formatting
						break;
				}
				
				// ask if user wants to continue
				continueSim = help.inputYesNo("Do you want to continue simulating?");
				System.out.println(); // formatting
		}
		
	}

	private void runNewUserProgram() {
		// TODO Auto-generated method stub
		// determine whether new user wants to be free or premium
		boolean userPreferred = help.inputYesNo("Would you like to be a premium user (comes with game simulation capabilities and increased player creation)? ");
		System.out.println(); // formatting
		
		// user gives up on trying to guess password
		if (!userPreferred) {
			// create new free user
			Free freeUser = createNewFreeUser();
			System.out.println(); // formatting
			
			// writes user to .txt file
			writeFreeUser(freeUser);
			System.out.println(); // formatting
			
			// run game for free user
			runFreeGame(freeUser);
		} else {
			// create new premium user
			Premium premiumUser = createNewPremiumUser();
			System.out.println(); // formatting			
			
			// write user to .txt file
			writePremiumUser(premiumUser);
			System.out.println(); // formatting
			
			// run game for premium user
			runPremiumGame(premiumUser);
		}
	}

	// write premium user to PremiumUsers.txt
	private void writePremiumUser(Premium premiumUser) {
		String str = premiumUser.getFirstName() + "," + premiumUser.getLastName() + "," + premiumUser.getUserName() +
				"," + premiumUser.getPassword() + "," + premiumUser.getUserType() + "," + premiumUser.getPin() +
				"," + premiumUser.getTxtFile();
		
		// try-catch to add new premium user
		try {
			// use buffered writer and file writer to append file --> makes sure that file 
			// isn't deleted when file is written into for a second time
			PrintWriter outFS = new PrintWriter(new BufferedWriter(new FileWriter("src/resources/PremiumUsers.txt", true)));				
			outFS.println(str);
			outFS.close();
							
			System.out.println("Premium user added to file");
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		}
	}

	// add free user to FreeUsers.txt
	private void writeFreeUser(Free freeUser) {
		// string for free user
		String str = freeUser.getFirstName() + "," + freeUser.getLastName() + "," + freeUser.getUserName() +
				"," + freeUser.getPassword() + "," + freeUser.getUserType() + "," + freeUser.getTxtFile();
		
		// try-catch to add new free user
		try {
			// use buffered writer and file writer to append file --> makes sure that file 
			// isn't deleted when file is written into for a second time
			PrintWriter outFS = new PrintWriter(new BufferedWriter(new FileWriter("src/resources/FreeUsers.txt", true)));				
			outFS.println(str);
			outFS.close();
					
			System.out.println("Free user added to file");
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		}
	}

	// creates new premium user
	private Premium createNewPremiumUser() {
		// get  user input
		String firstName = help.inputWord("Please enter your first name");
		String lastName = help.inputWord("Please enter your last name");
				
		String userName = help.inputWord("Please enter a username");
				
		// determine whether username has already been taken
		boolean validUser = uniqueUserName(userName);
		while (!validUser) {
			System.err.println("Username is taken.");
			userName = help.inputWord("Please enter a username");
		}
				
		String password = help.inputWord("Please enter a password");
				
		UserType user = UserType.PREMIUM;
		
		String pin = help.inputLine("Please enter your PIN verification: ");
				
		String userFile = help.inputWord("Please enter your preferred file name for saving (do not include \".txt\": ");
				
		return new Premium(firstName, lastName, userName, password, user, pin, userFile);
	}

	// creates new free user
	private Free createNewFreeUser() {
		// get  user input
		String firstName = help.inputWord("Please enter your first name");
		String lastName = help.inputWord("Please enter your last name");
		
		String userName = help.inputWord("Please enter a username");
		
		// determine whether username has already been taken
		boolean validUser = uniqueUserName(userName);
		while (!validUser) {
			System.err.println("Username is taken.");
			userName = help.inputWord("Please enter a username");
			validUser = uniqueUserName(userName);
		}
		
		String password = help.inputWord("Please enter a password");
		
		UserType user = UserType.FREE;
		
		String userFile = help.inputWord("Please enter your preferred file name for saving (do not include \".txt\": ");
		
		return new Free(firstName, lastName, userName, password, user, userFile);
	}

	// runs simulation
	private void runFreeGame(Free f) {
		// get all data files
				Collection<Player> allPlayers = DataBaseFileReader.readPlayerFile();
				Collection<Coach> allCoaches = DataBaseFileReader.readCoachFile();
				
				// create both general maps
				playerMap = createPlayerMap(allPlayers);
				coachMap = getCoachMap(allCoaches);
				
				// create count for number of players created
				int createPlayerCount = 0;
				
				// initialize boolean to continue program
				boolean continueSim = true;
				
				// run while user wants to continue game 
				while (continueSim) {
						// free user capabilities
						int userChoice = displayOptions("free");
						switch (userChoice) {
							case 0:
								displayPlayerMap(playerMap);
								System.out.println(); // formatting
								break;
							case 1:
								displayCoachMap(coachMap);
								System.out.println(); // formatting
								break;
							case 2:
								TeamAbbreviation userABB = getUserABB();
								displayTeam(userABB);
								System.out.println(); // formatting
								break;
							case 3:
								int jerseyNum = getUserNum();
								displayJersey(jerseyNum, allPlayers);
								System.out.println(); // formatting
								break;
							case 4:
								int heightMin = getHeightMin();
								int heightMax = getHeightMax(heightMin);
								displayHeight(heightMin, heightMax, allPlayers);
								System.out.println(); // formatting
								break;
							case 5:
								Position userPosition = getUserPosition();
								displayPosition(userPosition, allPlayers);
								System.out.println(); // formatting
								break;
							case 6:
								// make sure user does not exceed maximum
								if (createPlayerCount < 2) {
									Player userPlayer = createCustomPlayer();
									
									// ask if user wants to add player to rosters
									boolean addPlayer = help.inputYesNo("Do you want to add player to roster?");
									
									// add player to file if desired
									if (addPlayer) {
										// check if file is already created
										boolean fileCreated = createNewFile(f.getTxtFile());
										
										if (fileCreated) {
											// add all current players for new file
											addCurrentPlayersToFile(allPlayers, f.getTxtFile());
										}
										
										// add new player
										addNewPlayerToFile(f.getTxtFile(), userPlayer);
									}
									createPlayerCount++;
								} else {
									System.out.println("You have exceeded your player creation limit (2 per session for free users)");
								}
								System.out.println(); // formatting
								break;
						}
						
						// ask if user wants to continue
						continueSim = help.inputYesNo("Do you want to continue simulating?");
						System.out.println(); // formatting
				}
	}
	
	// run game for premium user
	private void runPremiumGame(Premium premiumUser) {
		Collection<Player> allPlayers = DataBaseFileReader.readPlayerFile();
		Collection<Coach> allCoaches = DataBaseFileReader.readCoachFile();
		
		// create both general maps
		playerMap = createPlayerMap(allPlayers);
		coachMap = getCoachMap(allCoaches);
		
		// int to set limit on player creation
		int createPlayerCount = 0;
		
		// initialize boolean to continue program
		boolean continueSim = true;
		
		while (continueSim) {
			int userChoice = displayOptions("premium");
			// premium user capabilities
			switch (userChoice) {
				case 0:
					displayPlayerMap(playerMap);
					System.out.println(); // formatting
					break;
				case 1:
					displayCoachMap(coachMap);
					System.out.println(); // formatting
					break;
				case 2:
					TeamAbbreviation userABB = getUserABB();
					displayTeam(userABB);
					System.out.println(); // formatting
					break;
				case 3:
					int jerseyNum = getUserNum();
					displayJersey(jerseyNum, allPlayers);
					System.out.println(); // formatting
					break;
				case 4:
					int heightMin = getHeightMin();
					int heightMax = getHeightMax(heightMin);
					displayHeight(heightMin, heightMax, allPlayers);
					System.out.println(); // formatting
					break;
				case 5:
					Position userPosition = getUserPosition();
					displayPosition(userPosition, allPlayers);
					System.out.println(); // formatting
					break;
				case 6:
					displayGameSimulator();
					
					// get all factors from user
					double coachWinFactor = getCoachWinFactor();
					double coachExpFactor = getCoachExpFactor(coachWinFactor);
					double teamHeightFactor = displayTeamHeightFactor(coachWinFactor, coachExpFactor);
					
					// get user's teams
					TeamAbbreviation firstTeam = getUserABB();
					TeamAbbreviation secondTeam = getUserABB();
					
					// play simulation
					simulateGame(coachWinFactor, coachExpFactor, teamHeightFactor, firstTeam, secondTeam);
					System.out.println(); // formatting
					break;
				case 7:
					displayCoachMatchup();
					
					// get all factors from user
					double winFactor = getCoachWinFactor();
					
					double expFactor = 1.0 - winFactor;
					System.out.printf("After calculations, given your previous percentages of importance, the factor of coach's experience is %.2f\n" , expFactor);
					
					// get user's teams
					TeamAbbreviation team1 = getUserABB();
					TeamAbbreviation team2 = getUserABB();
					
					// play matchup simulator
					simulateMatchup(winFactor, expFactor, team1, team2);
					System.out.println(); // formatting
					break;
				case 8:
					// make sure that a limit is imposed for premium players as well
					if (createPlayerCount < 5) {
						Player userPlayer = createCustomPlayer();
						
						// ask if user wants to add player to rosters
						boolean addPlayer = help.inputYesNo("Do you want to add player to roster?");
						
						// add player to file if desired
						if (addPlayer) {
							// check if file is already created
							boolean fileCreated = createNewFile(premiumUser.getTxtFile());
							
							if (fileCreated) {
								// add all current players for new file
								addCurrentPlayersToFile(allPlayers, premiumUser.getTxtFile());
							}
							
							// add new player
							addNewPlayerToFile(premiumUser.getTxtFile(), userPlayer);
						}
						createPlayerCount++;
					} else {
						System.out.println("You have exceeded your player creation limit (5 per session for free users)");
					}
					System.out.println(); // formatting
					break;
			}
			
			// ask if user wants to continue
			continueSim = help.inputYesNo("Do you want to continue simulating?");
			System.out.println(); // formatting
		}
	}

	// determine if user is valid
	private boolean checkUser(Map<String, UserType> allUsers, String user) {
		// initalize boolean to determine if user is valid
		boolean isValid = false;
		
		// loop through userMap to see if username is valid
		for (String s: allUsers.keySet()) {
			if (s.equals(user)) {
				isValid = true;
			}
		}
		
		return isValid;
	}

	// create userMap
	private Map<String, UserType> createUserMap(Map<String, Free> freeUserMap, Map<String, Premium> premiumUserMap) {
		// create empty map for users
		Map<String, UserType> allUsers = new HashMap<>();
		
		// loop through free user map to add users to userMap
		for (Free f: freeUserMap.values()) {
			allUsers.put(f.getUserName(), f.getUserType());
		}
		
		// loop through premium map to add users to userMap
		for (Premium p: premiumUserMap.values()) {
			allUsers.put(p.getUserName(), p.getUserType());
		}
		
		return allUsers;
	}

	// add new player to file
	private void addNewPlayerToFile(String userFile, Player p) {
		String str = p.getFirstName() + "," + p.getLastName() + "," + p.getTeamAbbreviation().teamName +
				"," + p.getTeamAbbreviation() + "," + p.getPosition() + "," + p.getNumber()
				+ "," + p.getHeight();
		
		// try-catch to add new player
		try {
			// use buffered writer and file writer to append file --> makes sure that file 
			// isn't deleted when file is written into for a second time
			PrintWriter outFS = new PrintWriter(new BufferedWriter(new FileWriter(userFile + ".txt", true)));
			outFS.println(str);
			outFS.close();
			
			System.out.println("Player added");
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		}
	}

	// adding current players to file
	private void addCurrentPlayersToFile(Collection<Player> players, String userFile) {
		// try-catch to write all birthdays to file
		try {
			PrintWriter outFS = new PrintWriter(new FileOutputStream(userFile + ".txt"));
			for (Player p: players) {
				outFS.println(p.getFirstName() + "," + p.getLastName() + "," + p.getTeamAbbreviation().teamName +
						"," + p.getTeamAbbreviation() + "," + p.getPosition() + "," + p.getNumber()
						+ "," + p.getHeight());
			}
			outFS.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not write to file");
			e.printStackTrace();
		}
		
	}

	// creating file with user's file name
	private boolean createNewFile(String userFile) {
		boolean isCreated = false;
		
		try {
			File playerFile = new File(userFile + ".txt");
			if (playerFile.createNewFile()) {
				System.out.println("File created: " + playerFile.getName());
				isCreated = true;
			} else {
				System.out.println("File already exists");
			}
		} catch (IOException e) {
			System.err.println("An error occurred");
			e.printStackTrace();
		}
		
		return isCreated;
		
	}

	// allow user to create their own player
	private Player createCustomPlayer() {
		// display welcome message
		System.out.println("Welcome to the NBA Player Generator\n");
		
		// get all player information
		String firstName = help.inputWord("Please enter player's first name:");
		String lastName = help.inputWord("Please enter player's last name:");
		TeamAbbreviation userTeam = getUserABB();
		Position userPosition = getUserPosition();
		int userJersey = help.inputInt("Please enter player's jersey number: ", 0, 99);
		
		int userHeight = help.inputInt("Please enter player's height (in inches): ");
		while (userHeight < 0) {
			System.err.println("Player height cannot be negative.");
			userHeight = help.inputInt("Please enter player's height (in inches): ");
		}
		
		Player userPlayer = new Player(firstName, lastName, userTeam, userPosition, userJersey, userHeight);
		
		return userPlayer;
	}

	// display welcome message for game
	private void displayCoachMatchup() {
		System.out.println("Welcome to the NBA Coaches' Matchup.\n" + 
				 "Note: total probability must equal 1.0, or else simulator will not run.\n");
		System.out.println("This simulator determines winner of game based on two factors\n" +
				 "\tCoach's win percentage\n\tCoach's total experience\n");
	}

	// method to simulate coaches' matchup
	private void simulateMatchup(double winFactor, double expFactor, TeamAbbreviation team1, TeamAbbreviation team2) {
		// get stat total for first team
		Coach firstCoach = getUserCoach(team1);
		
		double totalScore1 = winFactor * firstCoach.getWinPercent() +
				expFactor * firstCoach.getNumSeason();
		
		// get stat total for second team
		Coach secondCoach = getUserCoach(team2);
		
		double totalScore2 = winFactor * secondCoach.getWinPercent() +
				expFactor * secondCoach.getNumSeason();
		
		// calculate difference between scores
		double teamDifference = totalScore1 - totalScore2;
		
		// display messages depending on winner
		if (Math.abs(teamDifference) < 0.001) {
			System.out.println("Just your lucky day! Both teams have tied.");
		} else if(teamDifference > 0) {
			System.out.println("Coach " + firstCoach.getFirstName() + " " + firstCoach.getLastName() + 
					" from " + firstCoach.getTeamAbbreviation() + " has won this game!");
		} else {
			System.out.println("Coach " + secondCoach.getFirstName() + " " + secondCoach.getLastName() + 
					" from " + secondCoach.getTeamAbbreviation() + " has won this game!");
		}
	}

	// prints out welcome message for game simulator
	private void displayGameSimulator() {
		System.out.println("Welcome to the NBA Game Simulator.\n" + 
				 "Note: total probability must equal 1.0, or else simulator will not run.\n");
		System.out.println("This simulator determines winner of game based on three factors\n" +
				 "\tCoach's win percentage\n\tCoach's total experience\n\tTeam's average height\n");
	}

	// method to simulate game
	private void simulateGame(double coachWinFactor, double coachExpFactor, double teamHeightFactor,
			TeamAbbreviation firstTeam, TeamAbbreviation secondTeam) {
		// get stat total for first team
		Coach firstCoach = getUserCoach(firstTeam);
		int team1AvgHeight = getTeamHeight(firstTeam, teamHeightFactor);
		
		// calculate total score
		double totalScore1 = coachWinFactor * firstCoach.getWinPercent() +
				coachExpFactor * firstCoach.getNumSeason() +
				teamHeightFactor * team1AvgHeight;
		
		// get stat total for second team
		Coach secondCoach = getUserCoach(secondTeam);
		int team2AvgHeight = getTeamHeight(secondTeam, teamHeightFactor);
				
		// calculate total score
		double totalScore2 = coachWinFactor * secondCoach.getWinPercent() +
				coachExpFactor * secondCoach.getNumSeason() +
				teamHeightFactor * team2AvgHeight;
				
		// calculate difference between scores
		double teamDifference = totalScore1 - totalScore2;
		
		// display messages depending on winner
		if (Math.abs(teamDifference) < 0.001) {
			System.out.println("Just your lucky day! Both teams have tied.");
		} else if(teamDifference > 0) {
			System.out.println(firstTeam + " has won this game!");
		} else {
			System.out.println(secondTeam + " has won this game!");
		}
	}

	// method to get average team height
	private int getTeamHeight(TeamAbbreviation firstTeam, double teamHeightFactor) {
		// get all players' teams
		Set<TeamAbbreviation> keySet = playerMap.keySet();
		
		// declare variable for total team height and total players
		int teamHeight = 0;
		int teamCount = 0;
		
		for (TeamAbbreviation t: keySet) {
			if (t.equals(firstTeam)) {
				// loop through all players and add height to total
				List<Player> teamList = playerMap.get(t);
				for (Player p: teamList) {
					teamHeight = teamHeight + p.getHeight();
					teamCount++;
				}
			}
		}
		
		// return average
		return (teamHeight / teamCount);
	}

	// get user's desired coach
	private Coach getUserCoach(TeamAbbreviation firstTeam) {
		// get all coaches' teams
		Set<TeamAbbreviation> keySet = coachMap.keySet();
		
		// declare variable for coach
		Coach c = null;
		
		// loop through all keys to look for match
		for (TeamAbbreviation t: keySet) {
			if (t.equals(firstTeam)) {
				c = coachMap.get(t);
			}
		}
		
		return c;
	}

	// get user's importance of team's average height
	private double displayTeamHeightFactor(double coachWinFactor, double coachExpFactor) {
		// get user's importance of team height by subtracting current percent total from 1.0
		double currentPercent = 1.0 - (coachWinFactor + coachExpFactor);
		
		// display user's importance of team height
		System.out.printf("After calculations, given your previous percentages of importance, the factor of team's average height is %.2f\n" ,currentPercent );
		
		return currentPercent;
	}

	// get user's importance of experience factor
	private double getCoachExpFactor(double percent) {
		// allow for user input
		double coachExp = help.inputDouble("Please enter importance of coach's total experience (0.0 - 1.0):", 0.0, (1.0 - percent));
				
		
		return coachExp;
	}

	// get user's importance of win factor
	private double getCoachWinFactor() {
		// allow for user input
		double coachWin = help.inputDouble("Please enter importance of coach's win percentage (0.0 - 1.0):", 0.0, 1.0);
		
		return coachWin;
	}

	// display all players with user's position
	private void displayPosition(Position userPosition, Collection<Player> allPlayers) {
		System.out.println("All players with position " + userPosition.toString());
		
		// loop through all players to find matching position
		for (Player p: allPlayers) {
			if (p.getPosition().equals(userPosition)) {
				System.out.println(p);
			}
		}
		
	}

	// gets user's preferred position
	private Position getUserPosition() {
		// get array of all enums
		Position[] enumList = Position.values();
		
		// print out all enums except NONE
		System.out.println("All positions:");
		
		for (int i = 0; i < enumList.length - 1; i++) {
			System.out.println(i + ": " + enumList[i]);
		}
		
		// get user input
		String userString = help.inputWord("Please enter your preferred position (use - instead of _): ");
		
		// match string to position
		Position userPosition = Position.matchStringToPosition(userString);
		
		// continue getting user input until valid
		while (userPosition.toString().equalsIgnoreCase("none")) {
			System.out.println(userString + " is an invalid position");
			userString = help.inputWord("Please enter your preferred position: ");
			userPosition = Position.matchStringToPosition(userString);
		}
		
		return userPosition;
	}

	// display all players within height range
	private void displayHeight(int heightMin, int heightMax, Collection<Player> allPlayers) {
		// looping to ensure that there are players in this height range
		int count = 0;
		
		for (Player p: allPlayers) {
			if (p.getHeight() >= heightMin && p.getHeight() <= heightMax) {
				count++;
			}
		}
		
		// statement depending on whether there are players in height range
		if (count == 0) {
			System.out.println("No players between " + heightMin + " inches and " + heightMax + " inches");
		} else {
			System.out.println("All players between " + heightMin + " inches and " + heightMax + " inches");
		}
		
		// loop through all players to display all players in height range
		for (Player p: allPlayers) {
			if (p.getHeight() >= heightMin && p.getHeight() <= heightMax) {
				System.out.println(p);
			}
		}
		
	}

	// method to get user's height max
	private int getHeightMax(int heightMin) {
		// get user input
		int heightMax = help.inputInt("Please enter your desired maximum height");
		
		// continue getting user input until valid
		while (heightMax < heightMin) {
			System.out.println("Maximum height cannot be lower than the minimum height");
			heightMax = help.inputInt("Please enter your desired maximum height");
		}
		
		return heightMax;
	}

	// method to get user's height minimum
	private int getHeightMin() {
		// get user input
		int heightMin = help.inputInt("Please enter your desired minimum height");
		
		// continue getting user input until valid
		while (heightMin < 0) {
			System.out.println("Negative heights not allowed");
			heightMin = help.inputInt("Please enter your desired minimum height");
		}
		
		return heightMin;
	}

	// print out all players with specific jersey number
	private void displayJersey(int jerseyNum, Collection<Player> allPlayers) {
		// create new player list to hold all players
		List<Player> playerNum = new ArrayList<>();
		
		// loop through all players to find matching numbers
		for (Player p: allPlayers) {
			if (p.getNumber() == jerseyNum) {
				playerNum.add(p);
			}
		}
		
		// print out all players with matching num
		System.out.println("All players with #" + jerseyNum);
		
		if (playerNum.size() != 0) {
			for (Player pl: playerNum) {
				System.out.println(pl);
			}
		} else {
			System.out.println("No players with #" + jerseyNum); // print message if no matches
		}
		
	}

	// gets user's preferred jersey number
	private int getUserNum() {
		int userNum = help.inputInt("Please enter your preferred jersey number:");
		return userNum;
	}

	// prints out every player from team
	private void displayTeam(TeamAbbreviation userABB) {
		// get key set for coaches' map
		Set<TeamAbbreviation> coachSet = coachMap.keySet();
		
		// match coach to user abbreviation
		for (TeamAbbreviation c: coachSet) {
			if (c.equals(userABB)) {
				System.out.println(coachMap.get(c));
			}
		}
		
		// get players from players' map
		Set<TeamAbbreviation> playerSet = playerMap.keySet();
		
		// match entire team to user abbreviation
		for (TeamAbbreviation p: playerSet) {
			if (p.equals(userABB)) {
				List<Player> teamList = playerMap.get(p);
				
				// print all players
				for (Player pl: teamList) {
					System.out.println(pl);
				}
			}
		}
		
	}

	// method to get user's team choice
	private TeamAbbreviation getUserABB() {
		// get all team abbreviation enums
		TeamAbbreviation[] teamSet = TeamAbbreviation.values();
		
		// display all choices
		System.out.println("Here are your team choices");
		for (int i = 0; i < teamSet.length - 1; i++) {
			System.out.println(i + ": " + teamSet[i]);
		}
		
		// get user choice
		int userInt = help.inputInt("Please enter your team choice (by corresponding number", 0, teamSet.length - 2);
		
		return teamSet[userInt];
	}

	// display all options for free user and get user choice
	private int displayOptions(String s) {
		// array for free and premium options
		String[] freeOptions = {"Display all players", "Display all coaches", "Display certain team",
                "Display player by jersey number", "Display player by height",
                "Display player by position", "Create new player"};
		String[] premiumOptions = {"Display all players", "Display all coaches", "Display certain team",
                "Display player by jersey number", "Display player by height",
                "Display player by position", "Simulate game", "Simulate coaching matchup",
                "Create new player"};
		
		// print out all options (depending on free or premium
		System.out.println("Here are your options: ");
		
		int count = 0;
		int userChoice; // declare variable for user's choice
		
		if (s.equalsIgnoreCase("free")) {
			for (String option: freeOptions) {
				System.out.println(count + ": " + option);
				count++;
			}
			
			// get user choice for free game
			userChoice = help.inputInt("Please enter your desired action (0-6)", 0, freeOptions.length - 1);
		} else {
			for (String option: premiumOptions) {
				System.out.println(count + ": " + option);
				count++;
			}
			
			// get user choice for premium game
			userChoice = help.inputInt("Please enter your desired action (0-8)", 0, premiumOptions.length - 1);
		}
		
		return userChoice;
	}

	// verifying that userName is not taken
	private boolean uniqueUserName(String userName) {
		// initalize boolean for testing
		boolean validUser = true;
		
		// get keySet for userMap
		Set<String> allUserNames = userMap.keySet();
		
		// loop through all keys to make sure username is unique
		for (String s: allUserNames) {
			if (s.equals(userName)) {
				validUser = false;
			}
		}
		
		return validUser;
	}

	// printing all user options
	private UserType getUserOptions() {
		// get list of all enums
		UserType[] userNames = UserType.values();
		
		// print out all user types
		System.out.println("Here are your user types: ");
		
		for (int i = 0; i < userNames.length - 1; i++) {
			System.out.println("\t" + i + ": " + userNames[i]);
		}
		
		// get user's type
		String userChoice = help.inputWord("Please enter your preferred user type");
		UserType userType = UserType.matchStringToEnum(userChoice);
		
		// while loop to continue getting user input until valid
		while (userType.toString().equalsIgnoreCase("none")) {
			System.out.println("Invalid user type");
			userChoice = help.inputWord("Please enter your preferred user type");
			userType = UserType.matchStringToEnum(userChoice);
		}
		
		return userType;
		
	}

	private void displayCoachMap(Map<TeamAbbreviation, Coach> coachMap) {
		// get all keys from coach map
		Set<TeamAbbreviation> keySet = coachMap.keySet();
		
		System.out.println("All NBA Coaches:");
		
		int count = 0; // set count to 0 to display coach's index
		// for loop to loop through all team abbreviations
		for(TeamAbbreviation key: keySet) {
			// print out coach in formatted way
			System.out.println(count + ": " + coachMap.get(key));
			count++;
		}
		
	}

	// create coachMap
	private Map<TeamAbbreviation, Coach> getCoachMap(Collection<Coach> allCoaches) {
		// for loop to loop through all NBA teams
		for (TeamAbbreviation t: TeamAbbreviation.values()) {
			// loop through all coaches
			for (Coach c: allCoaches) {
				// find coach that matches with team abbreviation
				if (t.equals(c.getTeamAbbreviation())) {
					coachMap.put(t, c);
				}
			}
		}
		
		return coachMap;
	}

	// displaying playerMap
	private void displayPlayerMap(Map<TeamAbbreviation, List<Player>> playerMap) {
		// get all keys from map
		Set<TeamAbbreviation> keySet = playerMap.keySet();
		
		
		// for loop to loop through all keys and get list of players for each team
		for (TeamAbbreviation key: keySet) {
			List<Player> teamMap = playerMap.get(key);
			System.out.println("Players from " + key);
			
			// count of player on team;
			int count = 0;
			// display each player from team
			for (Player p: teamMap) {
				System.out.println(count + ": " + p);
				count++;
				
			}
		}
	}

	// getting Map of all players, separated by team
	private Map<TeamAbbreviation, List<Player>> createPlayerMap(Collection<Player> allPlayers) {
		// for loop to loop through all team abbreviations
		for (TeamAbbreviation t: TeamAbbreviation.values()) {
			List<Player> teamList = new ArrayList<>();
			
			// for loop to loop through all players, looking for matching team abbreviations
			for (Player p: allPlayers) {
				// add player if team abbreviation matches
				if (t.equals(p.getTeamAbbreviation())) {
					teamList.add(p);
				}
			}
			// add team abbreviation and list of corresponding players to playerMap
			playerMap.put(t, teamList);
		}
		return playerMap;
	}
	
	// main method
	public static void main(String[] args) {
		FinalProgram fp = new FinalProgram(); // get new favoritesProgram with all maps
		fp.run(); // run program

	}
}
