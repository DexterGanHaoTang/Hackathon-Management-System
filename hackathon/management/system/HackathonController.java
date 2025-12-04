package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import java.util.List;

/**
 * HackathonController Class (CONTROLLER)
 * handles interaction logic between the HackathonGUI (VIEW) and TeamList (MODEL)
 */
public class HackathonController {
    private TeamList model;
    private HackathonGUI view;
    
    public HackathonController(TeamList model) {
        this.model = model;
        // Load data when the controller is initialized
        model.loadAllData();
    }

    public void setView(HackathonGUI view) {
        this.view = view;
    }
    
    /**
     * Authenticates a user based on username and password.
     * Returns the UserType string
     */
    public String authenticateUser(String username, String password) {
        // Delegate authentication details lookup to the Model
        User user = model.getUserAuthenticationDetails(username);
        
        // Delegate password validation to the User object's login method
        if (user != null && user.login(password)) {
            return user.getUserType();
        }
        return null;
    }

    // Get all teams
    public List<Team> getAllTeams() {
        return model.getTeams();
    }
    
    // Get teams by id
    public Team getTeamById(int teamId) {
        return model.findTeamById(teamId);
    }
    
    // Get full report
    public String getFullReport() {
        return model.generateFinalReport();
    }

    /**
     * Retrieves a list of unique category names from the teams
     * Delegates the retrieval of unique Category objects to the Model
     */
    public List<String> getAvailableCategories() {
        return model.getUniqueCategories(); // Calls the actual method in TeamList
    }

    /**
     * Finds a team by ID and updates its scores using a new int[] array
     * Returns true if the update was successful and false if not
     */
    public boolean updateTeamScores(int teamId, int[] newScores) {
        if (newScores == null || newScores.length != 5) {
            System.err.println("Error, invalid score array provided. Must contain only 5 scores.");
            return false;
        }
        
        // Get team by id
        Team team = getTeamById(teamId);
        
        if (team != null) {
            // Convert int[] to double[] to match the team.setScores(double[] scores)
            double[] doubleScores = new double[newScores.length];
            for (int i = 0; i < newScores.length; i++) {
                doubleScores[i] = newScores[i]; 
            }

            // Update the scores
            team.setScores(doubleScores); 
            System.out.printf("Scores for Team %d updated successfully.\n", teamId);
            return true;
        }
        
        // Team not found
        System.err.printf("Error: Team with ID %d not found.\n", teamId);
        return false;
    }
    
    // Updates static details for a specific team.
    public boolean editTeamDetails(int teamId, String newName, String newUniversity) {
        Team team = getTeamById(teamId);
        if (team != null) {
            team.setTeamName(newName);
            team.setUniversityName(newUniversity);
            // Notify the view to refresh
            if (view != null) {
                view.refreshAllPanels();
            }
            return true;
        }
        return false;
    }

    // Delegates sorting to the Model
    public List<Team> getSortedTeams(String sortBy) {
        return model.getSortedTeams(sortBy);
    }
    
    // Delegates filtering to the Model.
    public List<Team> getTeamsBySubclass(String subclass) {
        return model.getTeamsBySubclass(subclass);
    }

    // Removes a team from the list.
    public boolean removeTeam(int teamId) {
        Team teamToRemove = getTeamById(teamId);
        if (teamToRemove != null) {
            model.getTeams().remove(teamToRemove);
            if (view != null) {
                view.refreshAllPanels();
            }
            return true;
        }
        return false;
    }

    /**
     * Close and generate report
     * Delegates report generation to the Model and then exits the application
     */
    public void writeReportAndClose() {
        if (model.saveReportToFile("FinalReport_Hackathon.txt")) {
            System.out.println("\nReport successfully written to FinalReport_Hackathon.txt");
        } else {
            System.err.println("Error writing report to file.");
        }
        System.exit(0);
    }
}
