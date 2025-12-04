package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import java.io.File; 
import java.io.FileWriter; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.Collections; 
import java.util.Comparator; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 
import java.util.Scanner; 
import java.util.stream.Collectors; 

/**
 * TeamList Class (MODEL)
 * Manages the Team objects and handles all data analysis for reporting
 * This class represents the Model in the MVC pattern
 */
public class TeamList {
    private ArrayList<Team> teams; 
    private Map<String, User> userAuthenticationMap; 
    
    // CSV Files used for data loading
    private static final String TEAMS_FILE_NAME = "HackathonTeams.csv"; 
    private static final String SCORES_FILE_NAME = "HackathonScores.csv"; 
    private static final String USERS_FILE_NAME = "HackathonUsers.csv"; 

    // Constructor
    public TeamList() {
        this.teams = new ArrayList<>(); // Initialize the list of teams
        this.userAuthenticationMap = new HashMap<>(); // Initialize the user authentication map
    }

    // Getter method
    public ArrayList<Team> getTeams() {
        return teams; // Returns the list of all teams
    }
    
    // Get a User object using the username for authentication
    public User getUserAuthenticationDetails(String username) {
        return userAuthenticationMap.get(username); // Performs a fast lookup using the username key
    }
    
    /**
     * Data loading
     * Read and merge all 3 csv files
     * Returns true if all data loading steps succeed and false if not
     */
    public boolean loadAllData() {
        System.out.println("Attempting to read data from CSV files...");

        // Read Users
        System.out.println("Reading Users Data...");
        Map<String, User> usersMap = readUsersData(); // Load users into a map keyed by UserID
        if (usersMap.isEmpty()) { // Check if user data loading failed
            System.err.println("Error, could not load users data. Check " + USERS_FILE_NAME);
            return false;
        }
        
        // Read Scores
        System.out.println("Reading Scores Data...");
        Map<Integer, int[]> scoresMap = readScoresData(); // Load scores into a map keyed by TeamNumber
        if (scoresMap.isEmpty()) { // Check if score data loading failed
            System.err.println("Error, could not load scores data. Check " + SCORES_FILE_NAME);
            return false;
        }
        
        // Read Teams and merge with Users and Scores
        System.out.println("Reading and Merging Teams Data...");
        return readTeamsData(usersMap, scoresMap); // Attempt to load all teams
    }
    
    /**
     * Reads users from the HackathonUsers.csv
     * Returns a Map of UserID (String) to User object
     */
    private Map<String, User> readUsersData() {
        Map<String, User> usersById = new HashMap<>(); // Map to store users keyed by their unique ID
        userAuthenticationMap.clear(); // Clear the authentication map before next
        int sequentialIntId = 1;
        
        try (Scanner fileScanner = new Scanner(new File(USERS_FILE_NAME))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); // Skip header row
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine(); // Read the next line of user data
                // Split the line by comma using -1 limit to keep empty trailing fields
                String[] parts = line.split(",", -1); 
                
                // Format checking
                if (parts.length != 8) {
                    System.err.println("Skipping user line due to incorrect field count (Expected 8, Got " + parts.length + "): " + line);
                    continue; // Skip if the format is wrong
                }
                
                try {
                    // Extract and trim the 8 fields
                    String userId = parts[0].trim();
                    String username = parts[1].trim(); 
                    String email = parts[2].trim();
                    String password = parts[3].trim();
                    String userType = parts[4].trim();
                    String firstName = parts[5].trim();
                    String middleName = parts[6].trim(); 
                    String lastName = parts[7].trim(); 
                    
                    String studentID = userId; // UserID is used as StudentID

                    // Create Name object
                    Name nameObj = new Name(firstName, middleName, lastName);
                    // Generate a dummy integer ID
                    int userIdInt = sequentialIntId++;

                    User user;
                    // Create Competitor subclass if userType is "Competitor"
                    if ("Competitor".equalsIgnoreCase(userType)) {
                        // Create Competitor object with parsed and dummy data
                        user = new Competitor(userIdInt, nameObj, email, password, 
                                             studentID, "Unknown Uni", "01/01/2000");
                    } else {
                        // Create common User object for other types
                        user = new User(userIdInt, nameObj, email, password, userType);
                    }
                    
                    // Populate maps for quick lookup and authentication
                    usersById.put(userId, user); // Keyed by string UserID for team lookup
                    userAuthenticationMap.put(username, user); // Keyed by username for login

                } catch (NumberFormatException e) {
                    System.err.println("Skipping user line due to ID format error: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file not found or reading errors
            System.err.println("Error, could not read file " + USERS_FILE_NAME + ". Ensure it exists. " + e.getMessage());
            return Collections.emptyMap(); // Return an empty map if error
        }
        return usersById; // Return the map of all users by ID
    }
    
    /**
     * Reads scores from the scores file HackathonScores.csv
     * Return a Map of TeamNumber (Integer) to an array of 5 scores (int[])
     */
    private Map<Integer, int[]> readScoresData() {
        Map<Integer, int[]> scores = new HashMap<>(); // Map to store scores keyed by team number
        
        try (Scanner fileScanner = new Scanner(new File(SCORES_FILE_NAME))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); // Skip header row
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine(); // Read the next line of score data
                String[] parts = line.split(","); // Split the line by comma
                
                // Format check
                if (parts.length != 6) {
                    System.err.println("Skipping score line due to incorrect field count (Expected 6, Got " + parts.length + "): " + line);
                    continue;
                }
                
                try {
                    int teamNumber = Integer.parseInt(parts[0].trim()); // Parse the team number
                    int[] teamScores = new int[5]; // Initialize array for 5 scores
                    for (int i = 0; i < 5; i++) {
                        // Parse the 5 score values starting from index 1
                        teamScores[i] = Integer.parseInt(parts[i + 1].trim());
                    }
                    scores.put(teamNumber, teamScores); // Map the scores array to the team number
                } catch (NumberFormatException e) {
                    System.err.println("Skipping score line due to number format error: " + line);
                }
            }
        } catch (IOException e) {
            // Handle file not found or reading errors
            System.err.println("Error, could not read file " + SCORES_FILE_NAME + ". Ensure it exists. " + e.getMessage());
            return Collections.emptyMap(); // Return an empty map if error
        }
        return scores; // Return the map of scores
    }

    /**
     * Helper to convert a semicolon-separated string of names into a List of Competitor objects
     * Creates dummy Competitor objects for team members whose full data is not available
     */
    private List<Competitor> createCompetitorListFromNames(String namesString) {
        // Handle null, empty, or N/A input string
        if (namesString == null || namesString.trim().isEmpty() || "N/A".equalsIgnoreCase(namesString.trim())) {
            return new ArrayList<>();
        }
        
        String[] names = namesString.split(";"); // Split the string by semicolon to get individual names
        List<Competitor> members = new ArrayList<>(); // List to hold the resulting Competitor objects
        
        int dummySequentialId = 90000;
        
        for (String fullName : names) {
            String trimmedName = fullName.trim();
            if (trimmedName.isEmpty()) continue; // Skip if the resulting name is empty

            // Attempt to split into first and last name
            String[] nameParts = trimmedName.split(" ");
            String firstName = nameParts[0];
            String lastName = (nameParts.length > 1) ? nameParts[nameParts.length - 1] : ""; // Last word is last name

            Name nameObj = new Name(firstName, "", lastName);
            // Create a dummy ID
            int dummyId = dummySequentialId++;
            
            // Create a Competitor object with dummy data
            Competitor member = new Competitor(
                dummyId, nameObj, "dummy@gmail.com", "N/A", 
                "D" + dummyId, "University", "07/11/2002"
            );
            members.add(member); // Add the dummy competitor to the list
        }
        return members; // Return the list of Competitor members
    }

    /**
     * Reads team data from HackathonTeams.csv and merges it with users and scores 
     * Instantiates the correct subclass either UniversityTeam or ProfessionalTeam
     */
    private boolean readTeamsData(Map<String, User> usersMap, Map<Integer, int[]> scoresMap) {
        teams.clear(); // Clear the existing list of teams before loading new data
        try (Scanner fileScanner = new Scanner(new File(TEAMS_FILE_NAME))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); // Skip header row
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine(); // Read the next team line
                // Split the line by comma using -1 limit to keep empty trailing fields
                String[] parts = line.split(",", -1); 
                
                // Format check
                if (parts.length != 9) {
                    System.err.println("Skipping team line due to incorrect field count (Expected 9, Got " + parts.length + "): " + line);
                    continue; // Skip the line if the format is wrong
                }
                
                try {
                    // Extract and trim the 8 fields
                    int teamNumber = Integer.parseInt(parts[0].trim());
                    String teamName = parts[1].trim();
                    String universityName = parts[2].trim();
                    String category = parts[3].trim();
                    String leaderID = parts[4].trim();
                    String membersString = parts[5].trim(); 
                    String teamStatus = parts[6].trim(); 
                    String teamType = parts[7].trim();
                    String extraAttribute = parts[8].trim();
                    
                    // Leader and Scores Lookup
                    User leaderUser = usersMap.get(leaderID); // Look up the leader by ID using the users map
                    // Check if leader exists and is a Competitor object
                    if (!(leaderUser instanceof Competitor)) continue; 
                    Competitor teamLeader = (Competitor) leaderUser; // Cast the User object to Competitor
                    // Look up scores and default to {0,0,0,0,0} if not found
                    int[] teamScores = scoresMap.getOrDefault(teamNumber, new int[]{0, 0, 0, 0, 0});
                    
                    // Parse Team Members using the helper method
                    List<Competitor> teamMembersList = createCompetitorListFromNames(membersString);
                    
                    // Create Category object
                    Category teamCategory = new Category(teamNumber, category, "Description");

                    // Dynamic Subclass Instantiation
                    if ("University".equalsIgnoreCase(teamType)) {
                        // For University teams the extra attribute is academicDepartment
                        String academicDepartment = extraAttribute;
                        // Instantiate the UniversityTeam subclass
                        teams.add(new UniversityTeam(teamNumber, teamName, universityName,
                                 teamLeader, teamMembersList, teamCategory, teamStatus,
                                 teamScores[0], teamScores[1], teamScores[2], teamScores[3], teamScores[4], // Pass individual scores
                                 academicDepartment)); // Pass the unique subclass attribute
                    } else if ("Professional".equalsIgnoreCase(teamType)) {
                        // For Professional teams the extra attribute is sponsor
                        String sponsor = extraAttribute;
                        // Instantiate the ProfessionalTeam subclass
                        teams.add(new ProfessionalTeam(teamNumber, teamName, universityName, 
                                 teamLeader, teamMembersList, teamCategory, teamStatus, teamScores[0], 
                                 teamScores[1], teamScores[2], teamScores[3], teamScores[4],
                                 sponsor)); // Pass the unique subclass attribute
                    } else {
                        System.err.printf("Error, Team %d has unknown or incomplete subclass data. Skipping.\n", teamNumber);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Skipping team line due to format error: " + line + " | Error: " + e.getMessage());
                }
            }
            return true; // Return true if file processing complete
        } catch (IOException e) {
            // Handle file not found or reading errors
            System.err.println("Error, could not read file " + TEAMS_FILE_NAME + ". Ensure it exists. " + e.getMessage());
            return false; // Return false if error
        }
    }

    // Filtering and Sorting
    
    /**
     * Sorts the team list by a specified criteria
     * Returns a sorted copy to avoid changing the original list order
     */
    public List<Team> getSortedTeams(String sortBy) {
        List<Team> sortedList = new ArrayList<>(teams); // Create a copy of the list
        
        // Define the comparator based on the sorting criteria using a switch expression
        Comparator<Team> comparator = switch (sortBy.toLowerCase()) {
            case "team name" -> Comparator.comparing(Team::getTeamName); // sort team name
            case "team number" -> Comparator.comparing(Team::getTeamNumber); // sort team number
            case "university" -> Comparator.comparing(Team::getUniversityName); // Sort university name
            case "overall score" -> Comparator.comparingDouble(Team::getOverallScore).reversed(); // Sort descending for score
            default -> Comparator.comparing(Team::getTeamNumber); // Default sort by team number
        };
        
        sortedList.sort(comparator); // Apply the comparator to sort the copy
        return sortedList; // Return the sorted list
    }
    
    /**
     * Filters teams by a chosen subclass type
     * Returns a list containing only the teams of the specified subclass type
     */
    public List<Team> getTeamsBySubclass(String subclass) {
        return teams.stream() // Start a stream on the list of teams
            .filter(team -> { // Filter the stream
                return switch (subclass.toLowerCase()) {
                    case "university" -> team instanceof UniversityTeam; // Check if the team is a UniversityTeam
                    case "professional" -> team instanceof ProfessionalTeam; // Check if the team is a ProfessionalTeam
                    default -> true; // If category is unknown then return all teams
                };
            })
            .collect(Collectors.toList()); // Collect the filtered results into a new list
    }
    
    /**
     * Retrieves a list of all unique category names
     * Returns a list of unique category names
     */
    public List<String> getUniqueCategories() {
        return teams.stream() // Start a stream
            .map(team -> team.getTeamCategory().getCategoryName()) // Map each team to its category name
            .distinct() // Keep only unique category names
            .collect(Collectors.toList()); // Collect the unique names into a list
    }

    // Report Printing

    /**
     * Handles writing the generated report to a file
     * Returns true if successful and false if failed
     */
    public boolean saveReportToFile(String fileName) {
        String reportContent = generateFinalReport(); // Get the content to write
        try (FileWriter writer = new FileWriter(fileName)) { // Open file writer
            writer.write(reportContent); // Write the report string to the file
            return true; // Successful
        } catch (IOException e) {
            System.err.println("Error writing report to file: " + e.getMessage());
            return false; // Failed
        }
    }

    /**
     * Finds the team with the highest overall score
     * Returns the Team with the highest score or null if the list is empty
     */
    public Team getTeamWithHighestScore() {
        if (teams.isEmpty()) return null; // Handle empty list case
        return teams.stream()
            .max(Comparator.comparingDouble(Team::getOverallScore)) // Find the team with the highest score
            .orElse(null); // Return the team or null if none found
    }

    /**
     * Calculates the average overall score across all teams rounded to two decimal places
     * Returns the calculated average score
     */
    public double getAverageOverallScore() {
        if (teams.isEmpty()) return 0.0; // Handle empty list case
        double sum = teams.stream()
            .mapToDouble(Team::getOverallScore) // Convert stream to double stream of scores
            .sum(); // Calculate the total sum of scores
        // Calculate average, multiply by 100, round, and divide by 100 to achieve 2 decimal places
        return Math.round((sum / teams.size()) * 100.0) / 100.0; 
    }
    
    /**
     * Finds the maximum overall score achieved by any team
     * Returns the maximum overall score or 0.0 if the list is empty
     */
    public double getMaxOverallScore() {
        return teams.stream().mapToDouble(Team::getOverallScore).max().orElse(0.0); // Use stream max() function
    }

    /**
     * Counts the number of teams associated with a specific university
     * Returns the count of teams from that specific university
     */
    public long countTeamsByUniversity(String university) {
        return teams.stream()
            .filter(t -> t.getUniversityName().equalsIgnoreCase(university)) // Filter teams by university name
            .count(); // Return the count of filtered teams
    }

    /**
     * Generates a frequency map showing how many times each individual score 
     * was awarded across all 5 scoring criteria for all teams
     */
    public Map<String, Map<Integer, Integer>> getCriteriaScoreFrequencyReport() {
        // Outer map: Key = Criteria Name (String), Value = Frequency Map (Map<Score Value, Count>)
        Map<String, Map<Integer, Integer>> criteriaFrequencyMap = new java.util.LinkedHashMap<>();

        // Initialize the map structure
        for (String criteria : CRITERIA_NAMES) {
            Map<Integer, Integer> scoreFrequency = new HashMap<>();
            // Initialize score values 0 through 5 with a count of 0 for this criterion
            for (int i = 0; i <= 5; i++) {
                scoreFrequency.put(i, 0);
            }
            // Add the initialized frequency map to the outer map
            criteriaFrequencyMap.put(criteria, scoreFrequency);
        }

        // Populate the frequencies
        for (Team team : teams) { // Loop through all teams
            int[] teamScores = team.getScoreArray();
            
            if (teamScores != null) {
                // Loop through the 5 scores which correspond to the 5 criteria
                for (int i = 0; i < CRITERIA_NAMES.length; i++) {
                    String criteriaName = CRITERIA_NAMES[i];
                    int score = teamScores[i];

                    // Check if the score is in the valid range
                    if (score >= 0 && score <= 5) {
                        // Retrieve the specific frequency map for the current criterion
                        Map<Integer, Integer> frequency = criteriaFrequencyMap.get(criteriaName);
                        
                        // Increment the count for that score value (key) in the criterion's map
                        frequency.put(score, frequency.get(score) + 1);
                    }
                }
            }
        }
        return criteriaFrequencyMap;
    }

    // Finds a team object by its unique team number ID
    public Team findTeamById(int id) {
        return teams.stream()
            .filter(t -> t.getTeamNumber() == id) // Filter for teams matching the ID
            .findFirst() // Get the first match
            .orElse(null); // Return null if no match is found
    }
    
    // Label for scores
    private static final String[] CRITERIA_NAMES = {
        "Creativity", "Technical", "Teamwork", "Presentation", "Functionality"
};

    /**
     * Generates the final report string by compiling all analysis sections
     * Returns the fully formatted report string
     */
    public String generateFinalReport() {
        StringBuilder report = new StringBuilder();
        
        // Report Header
        report.append("=========================================================================\n");
        report.append("                      HACKATHON TEAM FINAL REPORT                        \n");
        report.append("=========================================================================\n\n");
        
        // 1. Table of Teams with Full Details
        report.append("==== 1. FULL TEAM DETAILS ====\n");
        report.append("Total Teams Loaded: ").append(teams.size()).append("\n\n"); // Number of loaded teams
        for (Team team : teams) {
            // Calls the getFullDetails() based on subclass of either University or Professional
            report.append(team.getFullDetails()).append("\n\n"); 
        }
        report.append("\n\n");

        // 2. Team with Highest Overall Score
        Team highestScoringTeam = getTeamWithHighestScore();
        report.append("==== 2. TEAM WITH HIGHEST OVERALL SCORE ====\n");
        if (highestScoringTeam != null) {
            report.append(highestScoringTeam.getFullDetails()).append("\n\n");
        } else {
            report.append("No teams found.\n\n");
        }
        
        // 3. Summary Statistics
        report.append("==== 3. SUMMARY STATISTICS ====\n");
        report.append(String.format("Average Overall Score (All Teams): %.2f\n", getAverageOverallScore()));
        report.append(String.format("Maximum Overall Score: %.2f\n", getMaxOverallScore()));
        // Calculate Minimum Overall Score
        report.append(String.format("Minimum Overall Score: %.2f\n", teams.stream().mapToDouble(Team::getOverallScore).min().orElse(0.0)));
        report.append(String.format("Total Teams from UTM: %d\n", countTeamsByUniversity("UTM")));
        report.append("\n\n");

        // 4. Frequency Report
        report.append("==== 4. CRITERIA SCORE FREQUENCY REPORT ====\n");
        Map<String, Map<Integer, Integer>> reportData = getCriteriaScoreFrequencyReport();

        for (Map.Entry<String, Map<Integer, Integer>> criteriaEntry : reportData.entrySet()) {
            report.append("\nCriterion: ").append(criteriaEntry.getKey()).append("\n");

            // Sort the scores (0, 1, 2, 3, 4, 5) for clean output
            String scoreDetail = criteriaEntry.getValue().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(scoreEntry -> String.format("  Score %d awarded: %d times", scoreEntry.getKey(), scoreEntry.getValue()))
                    .collect(Collectors.joining("\n"));
            report.append(scoreDetail).append("\n");
        }
        
        return report.toString(); // Return the complete report as a single string
    }
}