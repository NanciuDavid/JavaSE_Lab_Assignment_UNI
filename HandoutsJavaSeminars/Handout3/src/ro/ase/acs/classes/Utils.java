package ro.ase.acs.classes;

import java.io.*;
import java.util.*;

public class Utils {

	// Main method for example usage
	public static void main(String[] args) {
		List<HandballMatch> matches = readFromCSV("matches.csv");
		matchDayReport(matches, "results.txt");
		serialize(matches, "list.dat");
		List<HandballMatch> deserializedList = deserialize("list.dat");
		writePoints("testt.csv", matches);
	}

	// Method for generating match day report
	public static void matchDayReport(List<HandballMatch> matches, String filename) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			for (HandballMatch match : matches) {
				// Write each match in the specified format
				bw.write(String.format("%s %d - %d %s%n",
						match.getHomeTeam(), match.getGoalsHomeTeam(),
						match.getGoalsAwayTeam(), match.getAwayTeam()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method for reading matches from CSV file
	public static List<HandballMatch> readFromCSV(String filename) {
		List<HandballMatch> matches = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = br.readLine(); // Ignore header
			while ((line = br.readLine()) != null) {
				// Parsing and adding matches to list
				String[] parts = line.split(",\\s*");
				if (parts.length == 4) {
					String homeTeam = parts[0];
					String awayTeam = parts[1];
					int goalsHomeTeam = Integer.parseInt(parts[2]);
					int goalsAwayTeam = Integer.parseInt(parts[3]);
					matches.add(new HandballMatch(homeTeam, awayTeam, goalsHomeTeam, goalsAwayTeam));
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
		return matches;
	}

	// Method for getting secret information from a file
	public static int secretInfo(String filename) {
		try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
			raf.seek(12); // Jump to the 12th byte
			return raf.readInt(); // Read integer
		} catch (IOException e) {
			e.printStackTrace();
			return -1; // Return -1 in case of error
		}
	}

	// Method for serialization - saving data to a file
	public static void serialize(List<HandballMatch> matches, String filename) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(matches);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method for deserialization - reading data from a file
	public static List<HandballMatch> deserialize(String filename) {
		List<HandballMatch> matches = new ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			matches = (List<HandballMatch>) ois.readObject(); // Deserialize list of matches
			System.out.println("Deserialization successful.");
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Deserialization failed: " + e.getMessage());
		}
		return matches;
	}

	// Method for writing header to a file
	public static void writeHeader(String filename) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			// Write header line
			bw.write("NO, TEAM, PTS, GF, GA, GD");
			bw.newLine(); // Add a new line
			System.out.println("Header written successfully to: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error writing header: " + e.getMessage());
		}
	}

	// Method for writing points to a CSV file
	public static void writePoints(String filename, List<HandballMatch> matches) {
		// Create a HashMap to store team names and their points
		Map<String, Integer> matchesMap = new HashMap<>();

		// Iterate through each match to calculate points for each team
		for (HandballMatch match : matches) {
			// Update points for home team
			matchesMap.put(match.getHomeTeam(), matchesMap.getOrDefault(match.getHomeTeam(), 0) + calculatePointsHome(match));
			// Update points for away team
			matchesMap.put(match.getAwayTeam(), matchesMap.getOrDefault(match.getAwayTeam(), 0) + calculatePointsAway(match));
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
			// Write header line
			writeHeader(filename);

			// Iterate through HashMap to write team names and their points
			for (Map.Entry<String, Integer> entry : matchesMap.entrySet()) {
				bw.write(entry.getKey() + ", " + entry.getValue());
				bw.newLine(); // Add a new line
			}
			System.out.println("Data written successfully to: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error writing data: " + e.getMessage());
		}
	}

	// Method for calculating points for home team based on match result
	private static int calculatePointsHome(HandballMatch match) {
		if (match.getGoalsHomeTeam() > match.getGoalsAwayTeam()) {
			return 3; // Home team winner
		} else if (match.getGoalsHomeTeam() < match.getGoalsAwayTeam()) {
			return 0; // Home team defeated
		} else {
			return 1; // Match ended in draw
		}
	}

	// Method for calculating points for away team based on match result
	private static int calculatePointsAway(HandballMatch match) {
		if (match.getGoalsAwayTeam() > match.getGoalsHomeTeam()) {
			return 3; // Away team winner
		} else if (match.getGoalsAwayTeam() < match.getGoalsHomeTeam()) {
			return 0; // Away team defeated
		} else {
			return 1; // Match ended in draw
		}
	}

	// Method for writing points and goals to a file
	public static void writePointsAndGoals(String filename, List<HandballMatch> matches) {
		// Create a HashMap to store team names and their statistics
		Map<String, int[]> teamStats = new HashMap<>();

		// Iterate through each match to update team statistics
		for (HandballMatch match : matches) {
			String homeTeam = match.getHomeTeam();
			String awayTeam = match.getAwayTeam();
			int[] homeStats = teamStats.getOrDefault(homeTeam, new int[4]);
			int[] awayStats = teamStats.getOrDefault(awayTeam, new int[4]);

			// Update goals for and against for home and away teams
			homeStats[1] += match.getGoalsHomeTeam();
			homeStats[2] += match.getGoalsAwayTeam();
			awayStats[1] += match.getGoalsAwayTeam();
			awayStats[2] += match.getGoalsHomeTeam();

			// Update points for home and away teams
			homeStats[0] += calculatePointsHome(match);
			awayStats[0] += calculatePointsAway(match);

			// Update goal difference for home and away teams
			homeStats[3] = homeStats[1] - homeStats[2];
			awayStats[3] = awayStats[1] - awayStats[2];

			// Update team statistics in the map
			teamStats.put(homeTeam, homeStats);
			teamStats.put(awayTeam, awayStats);
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			// Write header to file
			bw.write("NO, TEAM, PTS, GF, GA, GD");
			bw.newLine();

			// Write team statistics to file
			int position = 1;
			for (Map.Entry<String, int[]> entry : teamStats.entrySet()) {
				int[] stats = entry.getValue();
				bw.write(String.format("%d, %s, %d, %d, %d, %d",
						position++, entry.getKey(), stats[0], stats[1], stats[2], stats[3]));
				bw.newLine();
			}
			System.out.println("Data written successfully to: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error writing data: " + e.getMessage());
		}
	}

	// Method for generating league table
	public static void leagueTable(String filename, List<HandballMatch> matches) {
		// Create a HashMap to store team names and their statistics
		Map<String, int[]> teamStats = new HashMap<>();

		// Iterate through each match to update team statistics
		for (HandballMatch match : matches) {
			String homeTeam = match.getHomeTeam();
			String awayTeam = match.getAwayTeam();
			int[] homeStats = teamStats.getOrDefault(homeTeam, new int[4]);
			int[] awayStats = teamStats.getOrDefault(awayTeam, new int[4]);

			// Update goals for and against for home and away teams
			homeStats[1] += match.getGoalsHomeTeam();
			homeStats[2] += match.getGoalsAwayTeam();
			awayStats[1] += match.getGoalsAwayTeam();
			awayStats[2] += match.getGoalsHomeTeam();

			// Update points for home and away teams
			homeStats[0] += calculatePointsHome(match);
			awayStats[0] += calculatePointsAway(match);

			// Update goal difference for home and away teams
			homeStats[3] = homeStats[1] - homeStats[2];
			awayStats[3] = awayStats[1] - awayStats[2];

			// Update team statistics in the map
			teamStats.put(homeTeam, homeStats);
			teamStats.put(awayTeam, awayStats);
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			// Sort teams based on points in descending order
			List<Map.Entry<String, int[]>> sortedTeams = new ArrayList<>(teamStats.entrySet());
			sortedTeams.sort((t1, t2) -> Integer.compare(t2.getValue()[0], t1.getValue()[0]));

			// Write table to file
			int position = 1;
			for (Map.Entry<String, int[]> entry : sortedTeams) {
				int[] stats = entry.getValue();
				bw.write(String.format("%d, %s, %d, %d, %d, %d",
						position++, entry.getKey(), stats[0], stats[1], stats[2], stats[3]));
				bw.newLine();
			}
			System.out.println("Table written successfully to: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error writing table: " + e.getMessage());
		}
	}

	// Method for generating special league table
	public static void specialLeagueTable(String filename, List<HandballMatch> matches) {
		// Create a HashMap to store team names and their statistics
		Map<String, int[]> teamStats = new HashMap<>();

		// Iterate through each match to update team statistics
		for (HandballMatch match : matches) {
			String homeTeam = match.getHomeTeam();
			String awayTeam = match.getAwayTeam();
			int[] homeStats = teamStats.getOrDefault(homeTeam, new int[5]); // Added one extra element for goal difference
			int[] awayStats = teamStats.getOrDefault(awayTeam, new int[5]);

			// Update goals for and against for home and away teams
			homeStats[1] += match.getGoalsHomeTeam();
			homeStats[2] += match.getGoalsAwayTeam();
			awayStats[1] += match.getGoalsAwayTeam();
			awayStats[2] += match.getGoalsHomeTeam();

			// Update points for home and away teams
			homeStats[0] += calculatePointsHome(match);
			awayStats[0] += calculatePointsAway(match);

			// Update goal difference for home and away teams
			homeStats[3] = homeStats[1] - homeStats[2];
			awayStats[3] = awayStats[1] - awayStats[2];

			// Update goal difference for home and away teams
			homeStats[4] = homeStats[1] - homeStats[2];
			awayStats[4] = awayStats[1] - awayStats[2];

			// Update team statistics in the map
			teamStats.put(homeTeam, homeStats);
			teamStats.put(awayTeam, awayStats);
		}

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			// Sort teams based on points, goal difference, goals scored, and alphabetical order
			List<Map.Entry<String, int[]>> sortedTeams = new ArrayList<>(teamStats.entrySet());
			sortedTeams.sort((t1, t2) -> {
				int[] stats1 = t1.getValue();
				int[] stats2 = t2.getValue();

				// Compare points
				if (stats1[0] != stats2[0]) {
					return Integer.compare(stats2[0], stats1[0]); // Sort by points descending
				}

				// Compare goal difference
				if (stats1[3] != stats2[3]) {
					return Integer.compare(stats2[3], stats1[3]); // Sort by goal difference descending
				}

				// Compare goals scored
				if (stats1[1] != stats2[1]) {
					return Integer.compare(stats2[1], stats1[1]); // Sort by goals scored descending
				}

				// Compare team names in alphabetical order
				return t1.getKey().compareTo(t2.getKey());
			});

			// Write special table to file
			int position = 1;
			for (Map.Entry<String, int[]> entry : sortedTeams) {
				int[] stats = entry.getValue();
				bw.write(String.format("%d, %s, %d, %d, %d, %d",
						position++, entry.getKey(), stats[0], stats[1], stats[2], stats[3]));
				bw.newLine();
			}
			System.out.println("Special table written successfully to: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error writing special table: " + e.getMessage());
		}
	}
}
