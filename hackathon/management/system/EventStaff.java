package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/** 
 * EventStaff Class (Inherits from Staff)
 * Represents Staff that handles registration and team status
 */
public class EventStaff extends Staff{
    // Constructor
    public EventStaff(int userID, Name userName, String email, String userPassword) {
        super(userID, userName, email, userPassword);
        this.setUserType("EventStaff"); // Override userType set by Staff constructor
    }
    
    // Register Team
    public void registerTeam() { }

    // Update Team
    public void updateTeam() { }

    // Remove Team
    public void removeDisqualifiedTeam() { }
}
