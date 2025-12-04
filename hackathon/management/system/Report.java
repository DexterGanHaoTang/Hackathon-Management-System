package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

/**
 * Report Class
 * Contains information about Reports
 */
public class Report {
    private int reportID;
    private String dateOfGeneration;
    private String reportDetails;

    // Constructor
    public Report(int reportID, String dateOfGeneration, String reportDetails) {
        this.reportID = reportID;
        this.dateOfGeneration = dateOfGeneration;
        this.reportDetails = reportDetails;
    }

    // Getter method
    public int getReportID() { return reportID; }

    // Generate a report
    public void generateReport() { }

    // Print a report
    public void printReport() { }
}
