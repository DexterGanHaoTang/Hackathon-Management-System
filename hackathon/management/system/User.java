package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * User Class (Superclass for all user roles)
 * Common attributes and methods for all users in the system
 */
public class User {
    protected int userID;
    protected Name userName;
    protected String email;
    protected String userPassword;
    protected String userType; 

    // Constructor
    public User(int userID, Name name, String email, String userPassword, String userType) {
        this.userID = userID;
        this.userName = name;
        this.email = email;
        this.userPassword = userPassword;
        this.userType = userType;
    }

    // Getter methods
    public Name getUserName() { return userName; } 
    public String getUserType() { return userType; }
    public String getPassword() { return userPassword; }
    
    // Setter method
    public void setUserType(String userType) { this.userType = userType; } 

    // Login user
     public boolean login(String enteredPassword) {
        // Checks if the entered password matches its stored password
        return this.userPassword.equals(enteredPassword);
    }

    // Logout user
    public void logout() {
    }

    // View team details
    public void viewTeamDetails() {
    }

    // View results
    public void viewResults() {
    }
    
    // View report
    public void viewReport() {
    }
    
    // Print report
    public void printReport() {
    }
}
