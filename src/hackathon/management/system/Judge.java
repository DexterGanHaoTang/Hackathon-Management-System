package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/** 
 * Judge Class (Inherits from User)
 * Represents a judge that scores the teams
 */
public class Judge extends User {
    // Constructor
    public Judge(int userID, Name userName, String email, String userPassword) {
        super(userID, userName, email, userPassword, "Judge"); 
    }
    
    // Enter new score
    public void enterScore() { }

    // Update score
    public void updateScore() { }
}
