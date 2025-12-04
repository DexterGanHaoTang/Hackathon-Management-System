package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Category Class
 * Define the categories where the teams compete
 */
public class Category {
    private int categoryID;
    private String categoryName;
    private String categoryDescription;
    
    // Constructor
    public Category(int categoryID, String categoryName, String categoryDescription) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }
    
    // Getter
    public int getCategoryID() { return categoryID; }
    public String getCategoryName() { return categoryName; }
    public String getCategoryDescription() { return categoryDescription; }
    
    // Get results for a category
    public String getCategoryResult() { return ""; }
}
