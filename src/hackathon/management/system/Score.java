package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Score Class (Aggregation by Team)
 * Stores the scores for a team and calculates the simple average as a default
 */
public class Score {
    private int teamNumber;
    private double creativityScore;
    private double technicalScore;
    private double teamworkScore;
    private double presentationScore;
    private double functionalityScore;
    private double finalScore;

    // Constructor
    public Score(int teamNumber, double creativityScore, double technicalScore, double teamworkScore, double presentationScore, double functionalityScore) {
        this.teamNumber = teamNumber;
        this.creativityScore = creativityScore;
        this.technicalScore = technicalScore;
        this.teamworkScore = teamworkScore;
        this.presentationScore = presentationScore;
        this.functionalityScore = functionalityScore;
        this.finalScore = calculateFinalScore(); 
    }
    
    // Getter methods
    public int getTeamNumber() { return teamNumber; }
    public double getCreativityScore() { return creativityScore; }
    public double getTechnicalScore() { return technicalScore; }
    public double getTeamworkScore() { return teamworkScore; }
    public double getPresentationScore() { return presentationScore; }
    public double getFunctionalityScore() { return functionalityScore; }
    public double getFinalScoreValue() { return finalScore; }
    
    // Setter method
    public void setFinalScore(double calculatedScore) {
        this.finalScore = calculatedScore;
    }
    
    // Calculates the default final score which is a simple average (University Team)
    public double calculateFinalScore() { 
        double sum = creativityScore + technicalScore + teamworkScore + 
                     presentationScore + functionalityScore;
        double averageScore = sum / 5.0; 
        
        // Round to 2 decimals
        return Math.round(averageScore * 100.0) / 100.0;
    }

    // Updates all scores and recalculates the final score
    public void updateScoreRecord(double creativity, double technical, double teamwork, double presentation, double functionality) {
        this.creativityScore = creativity;
        this.technicalScore = technical;
        this.teamworkScore = teamwork;
        this.presentationScore = presentation;
        this.functionalityScore = functionality;
        this.finalScore = calculateFinalScore();
        System.out.printf("Score record for Team %d updated. New default final score: %.2f%n", teamNumber, this.finalScore);
    }

    // return String Scores
    @Override
    public String toString() {
        return String.format("[Creativity:%.1f, Technical:%.1f, Teamwork:%.1f, Presentation:%.1f, Functionality:%.1f, Overall:%.2f]",
            creativityScore, technicalScore, teamworkScore, presentationScore, functionalityScore, finalScore);
    }
}
