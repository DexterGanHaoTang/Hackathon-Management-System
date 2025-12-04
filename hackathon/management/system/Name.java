package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Name Class (Composition for User)
 * First, Middle, and Last names for a User
 */
public class Name {
    private String firstName;
    private String middleName;
    private String lastName;
    
    // Constructor
    public Name(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
    
    // Getter
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    
    // Returns the full name
    public String getAllName() {
        if (middleName == null || middleName.trim().isEmpty()) {
            return firstName + " " + lastName;
        }
        return firstName + " " + middleName + " " + lastName;
    }
    
    // Get Initials of Team Leader
    public String getInitials() {
        StringBuilder initials = new StringBuilder();

        // Get First Name Initials
        if (firstName != null && !firstName.trim().isEmpty()) {
            initials.append(firstName.trim().charAt(0));
        }

        // Get Middle Name Initials if its present
        if (middleName != null && !middleName.trim().isEmpty()) {
            initials.append(middleName.trim().charAt(0));
        }
        
        // Get Last Name Initials
        if (lastName != null && !lastName.trim().isEmpty()) {
            // Only add the last name initial if the full initials string is not empty
            if (initials.length() > 0 || initials.length() == 0) { // Always include last name initial
                 initials.append(lastName.trim().charAt(0));
            }
        }

        // Return the initials string in uppercase
        return initials.toString().toUpperCase();
    }
    
    // returns String fullname
    @Override
    public String toString() {
        return getAllName();
    }
}
