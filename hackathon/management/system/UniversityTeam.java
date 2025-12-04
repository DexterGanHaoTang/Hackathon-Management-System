package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import java.util.List;
import java.util.stream.Collectors;

/**
 * Subclass for University teams
 * Scoring Logic: Simple Average of the 5 scores
 */
public class UniversityTeam extends Team {
    private String academicDepartment;

    // Constructor
    public UniversityTeam(int teamNumber, String teamName, String universityName, 
                          Competitor teamLeader, List<Competitor> teamMembers, Category teamCategory, 
                          String teamStatus, double creativity, double technical, double teamwork, 
                          double presentation, double functionality, String academicDepartment) {
        super(teamNumber, teamName, universityName, teamLeader, teamMembers, teamCategory, teamStatus, 
              creativity, technical, teamwork, presentation, functionality);
        this.academicDepartment = academicDepartment;
    }
    
    // Calculates the simple average score
    @Override
    public double getOverallScore() {
        return teamScore.getFinalScoreValue();
    }

    // Format the list of team members for printing
    private String formatTeamMembers() {
        if (teamMembers == null || teamMembers.isEmpty()) {
            return "  - No additional members listed.";
        }
        // Use stream and collectors to format the list neatly
        return teamMembers.stream()
            .map(c -> "  - " + c.toString())
            .collect(Collectors.joining("\n"));
    }

    // Print the full details for the team including the academic department
    @Override
    public String getFullDetails() {
        return String.format(
            "---- University Team Details (Team %d) ----\n" +
            "Team Name: %s\n" +
            "University: %s\n" +
            "Category: %s\n" +
            "Department: %s\n" +
            "Status: %s\n" +
            "Leader: %s\n" +
            "Members:\n%s\n" +
            "Scores: %s\n" +
            "Overall Score (Avg): %.2f\n" +
            "-----------------------------------------------------------------------------------------------------",
            teamNumber, teamName, universityName,
            teamCategory.getCategoryName(), 
            academicDepartment,
            teamStatus,
            teamLeader.toString(),
            formatTeamMembers(),
            teamScore.toString(), getOverallScore()
        );
    }
}