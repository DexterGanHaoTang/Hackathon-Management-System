package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Staff Class (Inherits from User)
 * Represents staff managing tasks
 */
public class Staff extends User {
    // Constructor
    public Staff(int userID, Name userName, String email, String userPassword) {
        super(userID, userName, email, userPassword, "Staff"); 
    }

    // Enter new score
    public void enterScore() { }

    // Update score
    public void updateScore() { }
}
