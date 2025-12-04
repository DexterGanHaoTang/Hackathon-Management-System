package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import javax.swing.SwingUtilities;

/**
 * HackathonManagementSystem Class 
 * The main orchestrator of the system
 * Follows the MVC pattern by instantiating the Model and Controller, then launching the View
 */
public class HackathonManagementSystem {
    
    private TeamList teamList; // Model

    public HackathonManagementSystem() {
        // Initialize the model
        this.teamList = new TeamList();
    }

    // Initializes MVC components and launches the GUI
    public void startApplication() {
        System.out.println("==== Hackathon Team Management System ====\n");

        // Instantiate Controller passing the Model
        HackathonController controller = new HackathonController(teamList);
        
        // Launch the View (GUI) on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new HackathonGUI(controller);
        });
    }
    
    // Create an instance called manager
    public static void main(String[] args) {
        HackathonManagementSystem manager = new HackathonManagementSystem();
        manager.startApplication();
    }
}