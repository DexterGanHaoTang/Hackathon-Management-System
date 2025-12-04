package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import java.util.List;

/**
 * Abstract Superclass Team for all teams that contain the common attributes and methods
 * Represent a Team in the Hackathon
 */
public abstract class Team {
    protected int teamNumber;
    protected String teamName;
    protected String universityName;
    protected Competitor teamLeader;
    protected List<Competitor> teamMembers;
    protected Category teamCategory;
    protected String teamStatus;
    protected Score teamScore;

    // Constructor
    public Team(int teamNumber, String teamName, String universityName, Competitor teamLeader,
            List<Competitor> teamMembers, Category teamCategory, String teamStatus, double creativity,
            double technical, double teamwork, double presentation, double functionality) {
        this.teamNumber = teamNumber;
        this.teamName = teamName;
        this.universityName = universityName;
        this.teamLeader = teamLeader;
        this.teamMembers = teamMembers;
        this.teamCategory = teamCategory;
        this.teamStatus = teamStatus;
        this.teamScore = new Score(teamNumber, creativity, technical, teamwork, presentation, functionality);
    }

    // Getter methods
    public int getTeamNumber() { return teamNumber; }
    public String getTeamName() { return teamName; }
    public String getUniversityName() { return universityName; }
    public Competitor getTeamLeader() { return teamLeader; }
    public List<Competitor> getTeamMembers() { return teamMembers; }
    public Category getTeamCategory() { return teamCategory; }
    public String getTeamStatus() { return teamStatus; }
    public int[] getScoreArray() {
        // Converts double scores to int by rounding them first
        return new int[]{
            (int) Math.round(teamScore.getCreativityScore()),
            (int) Math.round(teamScore.getTechnicalScore()),
            (int) Math.round(teamScore.getTeamworkScore()),
            (int) Math.round(teamScore.getPresentationScore()),
            (int) Math.round(teamScore.getFunctionalityScore())
        };
    }

    // Setter methods
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }
    public void setTeamMembers(List<Competitor> teamMembers) { this.teamMembers = teamMembers; }
    public void setTeamStatus(String teamStatus) { this.teamStatus = teamStatus; }
    public void setScores(double[] newScores) {
        if (newScores != null && newScores.length == 5 && this.teamScore != null) {
            this.teamScore.updateScoreRecord(
                    newScores[0], newScores[1], newScores[2], newScores[3], newScores[4]
            );
        } else {
            System.err.println("Error: Cannot set scores as either it is null or the incorrect length.");
        }
    }
    
    // Get short details of a team
    public String getShortDetails() {
        return String.format("TID %d (%s) has an overall score of %.1f",
                              teamNumber, teamLeader.getInitials(), getOverallScore());
    }
    
    // Abstract Methods for subclass
    public abstract double getOverallScore();
    public abstract String getFullDetails();
}