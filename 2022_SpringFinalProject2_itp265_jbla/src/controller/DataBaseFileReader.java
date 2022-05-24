package controller;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

import model.nba.*;
import model.user.*;

public class DataBaseFileReader {

	// constants --> both files from resources
	public static final String PLAYER_FILE = "src/resources/NBAPlayers.txt";
	public static final String COACH_FILE = "src/resources/NBACoaches.txt";
	public static final String FREE_USER_FILE = "src/resources/FreeUsers.txt";
	public static final String PREMIUM_USER_FILE = "src/resources/PremiumUsers.txt";
	
	// getting collection of free
	public static Map<String, Free> readFreeFile() {
		// empty hashmap for all free users
		Map<String, Free> freeUsers = new HashMap<>();
		
		// try-with-resources to read through player file and get players
		try(FileInputStream s = new FileInputStream(FREE_USER_FILE); Scanner fileScan = new Scanner(s)) {
			fileScan.nextLine();
			
			// while loop for when file has another line
			while (fileScan.hasNextLine()) {
				String line = fileScan.nextLine();
				
				// get free user from each line
				try (Scanner ls = new Scanner(line)) {
					Free freeUser = Free.parseLinetoFree(line, ls);
					freeUsers.put(freeUser.getUserName(), freeUser);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.err.println("Error when reading line");
			e.printStackTrace();
		}
		
		return freeUsers;
	}
	
	// getting collection of premium
		public static Map<String, Premium> readPremiumFile() {
			// empty hashmap for all free users
			Map<String, Premium> premiumUsers = new HashMap<>();
			
			// try-with-resources to read through player file and get players
			try(FileInputStream s = new FileInputStream(PREMIUM_USER_FILE); Scanner fileScan = new Scanner(s)) {
				fileScan.nextLine();
				
				// while loop for when file has another line
				while (fileScan.hasNextLine()) {
					String line = fileScan.nextLine();
					
					// get free user from each line
					try (Scanner ls = new Scanner(line)) {
						Premium premiumUser = Premium.parseLinetoPremium(line, ls);
						premiumUsers.put(premiumUser.getUserName(), premiumUser);
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading file");
				e.printStackTrace();
			} catch (InputMismatchException e) {
				System.err.println("Error when reading line");
				e.printStackTrace();
			}
			
			return premiumUsers;
		}
	
	// getting collection of all players
	public static Collection<Player> readPlayerFile() {
		Collection<Player> playerCollection = new ArrayList<>();
		
		// try-with-resources to read through player file and get players
		try(FileInputStream s = new FileInputStream(PLAYER_FILE); Scanner fileScan = new Scanner(s)) {
			fileScan.nextLine();
			
			// while loop for when file has another line
			while (fileScan.hasNextLine()) {
				String line = fileScan.nextLine();
				
				// get player from each line
				try(Scanner ls = new Scanner(line)) {
					Player p = Player.parseLineToPlayer(line, ls);
					playerCollection.add(p);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.err.println("Error when reading line");
			e.printStackTrace();
		}
		
		return playerCollection;
	}
	
	// getting collection of all coaches
	public static Collection<Coach> readCoachFile() {
		Collection<Coach> coachCollection = new ArrayList<>();
		
		// try-with-resources to read through coach file and get coaches
		try(FileInputStream s = new FileInputStream(COACH_FILE); Scanner fileScan = new Scanner(s)) {
			fileScan.nextLine();
			
			// while loop for when file has another line
			while (fileScan.hasNextLine()) {
				String line = fileScan.nextLine();
				
				// get coach from each line
				try(Scanner ls = new Scanner(line)) {
					Coach c = Coach.parseLineToCoach(line, ls);
					coachCollection.add(c);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.err.println("Error when reading line");
			e.printStackTrace();
		}
		
		return coachCollection;
	}
	
	// duplicate methods for when Premium user writes new file and logs back in --> now has paramater
	public static Collection<Player> readPlayerFile(String fileName) {
		Collection<Player> playerCollection = new ArrayList<>();
		
		// try-with-resources to read through player file and get players
		try(FileInputStream s = new FileInputStream(fileName); Scanner fileScan = new Scanner(s)) {
			fileScan.nextLine();
			
			// while loop for when file has another line
			while (fileScan.hasNextLine()) {
				String line = fileScan.nextLine();
				
				// get player from each line
				try(Scanner ls = new Scanner(line)) {
					Player p = Player.parseLineToPlayer(line, ls);
					playerCollection.add(p);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.err.println("Error when reading line");
			e.printStackTrace();
		}
		
		return playerCollection;
	}
	
	public static Collection<Coach> readCoachFile(String fileName) {
		Collection<Coach> coachCollection = new ArrayList<>();
		
		// try-with-resources to read through coach file and get coaches
		try(FileInputStream s = new FileInputStream(COACH_FILE); Scanner fileScan = new Scanner(s)) {
			fileScan.nextLine();
			
			// while loop for when file has another line
			while (fileScan.hasNextLine()) {
				String line = fileScan.nextLine();
				
				// get coach from each line
				try(Scanner ls = new Scanner(line)) {
					Coach c = Coach.parseLineToCoach(line, ls);
					coachCollection.add(c);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		} catch (InputMismatchException e) {
			System.err.println("Error when reading line");
			e.printStackTrace();
		}
		
		return coachCollection;
	}
	
}
