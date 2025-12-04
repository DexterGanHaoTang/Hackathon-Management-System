package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import java.util.List;
import java.util.stream.Collectors;

/**
 * Subclass for Professional teams
 * Scoring Logic: Weighted average as Score 1 is weighted double
 */
public class ProfessionalTeam extends Team {
    private String sponsor; 

    // Constructor
    public ProfessionalTeam(int teamNumber, String teamName, String universityName, 
                            Competitor teamLeader, List<Competitor> teamMembers, Category teamCategory, 
                            String teamStatus, 
                            double creativity, double technical, double teamwork, 
                            double presentation, double functionality, String sponsor) {
        super(teamNumber, teamName, universityName, teamLeader, teamMembers, teamCategory, teamStatus, 
              creativity, technical, teamwork, presentation, functionality);
        this.sponsor = sponsor;
    }
    
    // Calculates a custom weighted average score with Creativity weighted 2
    @Override
    public double getOverallScore() {
        // Retrieve individual scores from the inherited teamScore object
        double c = teamScore.getCreativityScore();
        double t = teamScore.getTechnicalScore();
        double w = teamScore.getTeamworkScore();
        double p = teamScore.getPresentationScore();
        double f = teamScore.getFunctionalityScore();
        
        // Weights: 2 (Creativity), 1, 1, 1, 1 (Total weights = 6)
        double weightedSum = (c * 2) + t + w + p + f;
        double totalWeights = 6.0;

        // Calculate and round the weighted average to two decimal places
        double weightedAverage = weightedSum / totalWeights;
        
        // Update the finalScore in the Score object
        teamScore.setFinalScore(weightedAverage);
        
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

    // Print the full details for the team including the sponsor
    @Override
    public String getFullDetails() {
        return String.format(
                "---- Professional Team Details (Team %d) ----\n"
                + "Team Name: %s\n"
                + "University: %s\n"
                + "Category: %s\n"
                + "Sponsor: %s\n"
                + "Status: %s\n"
                + "Leader: %s\n"
                + "Members:\n%s\n"
                + "Scores: %s\n"
                + "Overall Score (Weighted): %.2f\n"
                + "-----------------------------------------------------------------------------------------------------",
                teamNumber, teamName,
                universityName, 
                teamCategory.getCategoryName(),
                sponsor, 
                teamStatus,
                teamLeader.toString(),
                formatTeamMembers(),
                teamScore.toString(), getOverallScore()
        );
    }
}