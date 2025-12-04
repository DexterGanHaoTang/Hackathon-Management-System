package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Organizer Class (Inherits from User)
 * Manages Hackathon judges, tasks, and reports
 */
public class Organizer extends User {
    // Constructor
    public Organizer(int userID, Name userName, String email, String userPassword) {
        super(userID, userName, email, userPassword, "Organizer");
    }

    // Manage categories
    public void manageCategories() { }

    // Add judge
    public void addJudge() { }

    // Remove judge
    public void removeJudge() { }
    
    // Generate report
    public void generateReport() { }

    // View report
    public void viewReport() { }

    // Print report
    public void printReport() { }
}
