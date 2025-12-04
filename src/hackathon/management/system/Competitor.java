package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Competitor Class (Inherits from User)
 * Represents a student participating in the Hackathon
 */
public class Competitor extends User {
    private String studentID;
    private String universityName;
    private String dateOfBirth; 

    public Competitor(int userID, Name userName, String email, String userPassword, String studentID, String universityName, String dateOfBirth) {
        super(userID, userName, email, userPassword, "Competitor"); 
        this.studentID = studentID;
        this.universityName = universityName;
        this.dateOfBirth = dateOfBirth;
    }

    // Getter
    public String getUniversityName() { return universityName; }  
    
    // Get leader initials
    public String getInitials() { 
        return this.getUserName().getInitials(); 
    }
    
    // Register team
    public void registerTeam() { }
    
    // returns a String of competitor names
    @Override
    public String toString() {
        return String.format("Competitor [Name: %s]",
            this.getUserName().getAllName());
    }
}
