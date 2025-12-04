package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

// Import the PermissionChecker utility
import hackathon.management.system.PermissionChecker.Action;

/**
 * HackathonGUI Class (VIEW)
 * The main GUI frame containing three panels for interaction with the system
 */
public class HackathonGUI extends JFrame {
    private HackathonController controller;

    // GUI components
    private JPanel loginPanel;
    private JPanel mainContentPanel;
    private JPanel teamScoresPanel;
    private JPanel teamTablePanel;
    private JPanel teamDetailsPanel;

    // Login components
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    // Team scores panel Components
    private JComboBox<String> teamIdSelector;
    private JTextField[] scoreFields = new JTextField[5];
    private JLabel overallScoreLabel;
    private JButton updateScoresButton;

    // Team table panel Components
    private JTable teamTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> sortSelector;
    private JComboBox<String> filterSelector;
    private JButton removeButton;

    // Team details panel components
    private JTextField detailTeamIdField;
    private JTextField editTeamNameField;
    private JTextField editUniversityField;
    private JTextArea fullDetailsTextArea;
    private JButton saveDetailsButton;
    private Team currentDetailTeam = null;
    
    // Current user state
    private String loggedInUserType = null;

    // Constructor
    public HackathonGUI(HackathonController controller) {
        this.controller = controller;
        this.controller.setView(this); // Set the view reference in the controller

        setTitle("Hackathon Management System - Login");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializePanels();
        
        // Start by showing only the Login Panel
        add(loginPanel, BorderLayout.CENTER);

        pack();
        setSize(500, 300);
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
    
    // Initialization
    private void initializePanels() {
        initializeLoginPanel();
        initializeMainContentPanel();
    }

    // Initialize Login
    private void initializeLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "System Login"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        loginButton.addActionListener(e -> loginAction());
        loginPanel.add(loginButton, gbc);
        
        // Public view button
        gbc.gridy = 3;
        JButton publicButton = new JButton("View Public Results");
        publicButton.addActionListener(e -> switchToMainContent("Public"));
        loginPanel.add(publicButton, gbc);
        
        // Close button
        gbc.gridy = 4;
        JButton closeButton = new JButton("Close System");
        closeButton.addActionListener(e -> controller.writeReportAndClose());
        loginPanel.add(closeButton, gbc);
    }

    // Initialize Main Page
    private void initializeMainContentPanel() {
        // Initialize all subpanels
        initializeScoresPanel();
        initializeTablePanel();
        initializeDetailsPanel();
        
        mainContentPanel = new JPanel(new BorderLayout(10, 10));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        centerPanel.add(teamScoresPanel);
        centerPanel.add(teamDetailsPanel);
        
        mainContentPanel.add(centerPanel, BorderLayout.NORTH);
        
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tableWrapper.add(teamTablePanel, BorderLayout.CENTER);
        mainContentPanel.add(tableWrapper, BorderLayout.CENTER);
        
        // Add footer with close button
        JButton closeButton = new JButton("Close & Write Final Report");
        closeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeButton.addActionListener(e -> controller.writeReportAndClose());
        JPanel footer = new JPanel();
        footer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        footer.add(closeButton);
        mainContentPanel.add(footer, BorderLayout.SOUTH);
    }

    // Initialize scores (1. View and Edit Team Scores)
    private void initializeScoresPanel() {
        teamScoresPanel = new JPanel(new BorderLayout(5, 10));
        teamScoresPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "1. View and Edit Team Scores"));
        
        // Input grid
        JPanel inputGrid = new JPanel(new GridLayout(6, 2, 10, 10));
        inputGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Team selector
        inputGrid.add(new JLabel("Select Team ID:"));
        teamIdSelector = new JComboBox<>();
        inputGrid.add(teamIdSelector);
        teamIdSelector.addActionListener(e -> displayCurrentScores());

        // Score fields
        String[] scoreLabels = {"Creativity (0 - 5):", "Technical (0 - 5):", "Teamwork (0 - 5):", "Presentation (0 - 5):", "Functionality (0 - 5):"};
        for (int i = 0; i < 5; i++) {
            inputGrid.add(new JLabel(scoreLabels[i]));
            scoreFields[i] = new JTextField(3);
            inputGrid.add(scoreFields[i]);
        }

        // Output and action
        overallScoreLabel = new JLabel("Overall Score: N/A");
        overallScoreLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        updateScoresButton = new JButton("Update Scores");
        updateScoresButton.addActionListener(e -> updateScoresAction());
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.add(updateScoresButton);
        actionPanel.add(overallScoreLabel);
        
        teamScoresPanel.add(inputGrid, BorderLayout.CENTER);
        teamScoresPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    // Initialize table view (2. View and Sort Teams)
    private void initializeTablePanel() {
        teamTablePanel = new JPanel(new BorderLayout(5, 5));
        teamTablePanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "2. View and Sort Teams"));

        // Table setup
        String[] columnNames = {"ID", "Name", "University", "Category", "Team Type", "Status",
                                "CreativityScore", "TechnicalScore", "TeamworkScore",
                                "PresentationScore", "FunctionalityScore", "Overall Score"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read only
            }
        };
        teamTable = new JTable(tableModel);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Controls panel
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        
        sortSelector = new JComboBox<>(new String[]{"Team Number", "Team Name", "University", "Overall Score"});
        sortSelector.addActionListener(e -> refreshTeamTable());
        controls.add(new JLabel("Sort By:"));
        controls.add(sortSelector);
        
        // Filter selector is initialized empty and populated later with categories
        filterSelector = new JComboBox<>(); 
        filterSelector.addActionListener(e -> refreshTeamTable());
        controls.add(new JLabel("Filter By:"));
        controls.add(filterSelector);
        
        // Remove a team
        removeButton = new JButton("Remove Selected Team");
        removeButton.addActionListener(e -> removeTeamAction());
        controls.add(removeButton);

        teamTablePanel.add(controls, BorderLayout.NORTH);
        teamTablePanel.add(new JScrollPane(teamTable), BorderLayout.CENTER);
    }
    
    // Initialize team details (3. View and Edit Team Details)
    private void initializeDetailsPanel() {
        teamDetailsPanel = new JPanel(new BorderLayout(5, 10));
        teamDetailsPanel.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), "3. View and Edit Team Details"));

        // Input and view setup
        JPanel inputAndEditPanel = new JPanel(new BorderLayout(10, 10));
        inputAndEditPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Team ID input for lookup
        JPanel lookupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lookupPanel.add(new JLabel("Enter Team ID:"));
        detailTeamIdField = new JTextField(5);
        JButton viewButton = new JButton("Load Team");
        viewButton.addActionListener(e -> displayTeamDetailsAction());
        lookupPanel.add(detailTeamIdField);
        lookupPanel.add(viewButton);
        
        // Editable fields
        JPanel editFields = new JPanel(new GridLayout(3, 2, 5, 5));
        editFields.add(new JLabel("Team Name:"));
        editTeamNameField = new JTextField(15);
        editFields.add(editTeamNameField);
        
        editFields.add(new JLabel("University"));
        editUniversityField = new JTextField(15);
        editFields.add(editUniversityField);
        
        // Edit detail
        saveDetailsButton = new JButton("Save Team Details");
        saveDetailsButton.addActionListener(e -> saveTeamDetailsAction());
        editFields.add(new JLabel(""));
        editFields.add(saveDetailsButton);

        inputAndEditPanel.add(lookupPanel, BorderLayout.NORTH);
        inputAndEditPanel.add(editFields, BorderLayout.CENTER);

        // Full details display area
        fullDetailsTextArea = new JTextArea(10, 30);
        fullDetailsTextArea.setEditable(false);
        fullDetailsTextArea.setText("Enter a Team ID and click 'Load Team' to view and edit details.");
        fullDetailsTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        teamDetailsPanel.add(inputAndEditPanel, BorderLayout.NORTH);
        teamDetailsPanel.add(new JScrollPane(fullDetailsTextArea), BorderLayout.CENTER);
    }

    // Login
    private void loginAction() {
        String username = usernameField.getText().trim();
        // Get password from field
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authenticate
        String userType = null;
        try {
            // Delegate authentication to Controller
            userType = controller.authenticateUser(username, password); 
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Authentication error. Check Controller.", "System Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        if (userType != null) {
            switchToMainContent(userType);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Switch to main page
    private void switchToMainContent(String userType) {
        this.loggedInUserType = userType;
        
        // Remove login panel and add main panel
        getContentPane().removeAll();
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Configure access based on userType
        configureAccess(userType);
        
        // Update title and UI state
        setTitle("Hackathon Management System - Logged in as: " + userType);
        
        // Load initial data and refresh
        populateTeamSelectors();
        populateFilterSelectors();
        refreshTeamTable();

        // Adjust frame size for main content
        setSize(1500, 850);
        revalidate();
        repaint();
    }
    
    // Configure access
    private void configureAccess(String userType) {
        String normalizedUserType = userType.toLowerCase().replace(" ", "");
        
        // Scores panel components (Edit Scores)
        boolean canUpdateScores = PermissionChecker.canPerformAction(normalizedUserType, Action.UPDATE_SCORES);
        boolean canViewReports = PermissionChecker.canPerformAction(normalizedUserType, Action.VIEW_REPORTS);
        teamIdSelector.setEnabled(canViewReports);
        updateScoresButton.setEnabled(canUpdateScores);
        for (JTextField field : scoreFields) {
            field.setEditable(canUpdateScores);
            field.setEnabled(canViewReports);
        }
        
        // Team table panel access (Delete Team)
        boolean canDeleteTeams = PermissionChecker.canPerformAction(normalizedUserType, Action.DELETE_TEAM);
        removeButton.setEnabled(canDeleteTeams);
        
        // Team details panel access (Edit Team Details)
        boolean canEditDetails = PermissionChecker.canPerformAction(normalizedUserType, Action.MANAGE_USERS);
        
        // Detail viewing components are enabled for anyone with VIEW_REPORTS permission
        detailTeamIdField.setEnabled(canViewReports);
        
        // Detail editing components are controlled by the MANAGE_USERS permission
        editTeamNameField.setEditable(canEditDetails);
        editUniversityField.setEditable(canEditDetails);
        saveDetailsButton.setEnabled(canEditDetails);
    }
    
    // Save team details
    private void saveTeamDetailsAction() {
        if (!saveDetailsButton.isEnabled()) return;

        if (currentDetailTeam == null) {
            JOptionPane.showMessageDialog(this, "Please load a team first using 'Load Team'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String newName = editTeamNameField.getText().trim();
        String newUni = editUniversityField.getText().trim();
        int teamId = currentDetailTeam.getTeamNumber();

        if (newName.isEmpty() || newUni.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Team Name and University fields cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        if (controller.editTeamDetails(teamId, newName, newUni)) {
            JOptionPane.showMessageDialog(this, "Team " + teamId + " details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Refresh to ensure the table and full details show the new data
            refreshAllPanels();
            
            // Reload the details view to show the full updated report in the text area
            displayTeamDetailsAction(); 
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update details. Team not found or internal error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update team scores
    private void updateScoresAction() {
        if (!updateScoresButton.isEnabled()) return;
        
        if (teamIdSelector.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No team selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int teamId = Integer.parseInt(teamIdSelector.getSelectedItem().toString());
            int[] newScores = new int[5];
            for (int i = 0; i < 5; i++) {
                int score = Integer.parseInt(scoreFields[i].getText());
                if (score < 0 || score > 5) {
                    JOptionPane.showMessageDialog(this, "Scores must be between 0 and 5.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                newScores[i] = score;
            }

            if (controller.updateTeamScores(teamId, newScores)) {
                refreshAllPanels();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update scores. Team not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please ensure all scores are numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove team
    private void removeTeamAction() {
        if (!removeButton.isEnabled()) return;
        
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                // Get the Team ID from the table model
                int teamId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove Team " + teamId + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    if (controller.removeTeam(teamId)) {
                        JOptionPane.showMessageDialog(this, "Team " + teamId + " removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        refreshAllPanels();
                    } else {
                        JOptionPane.showMessageDialog(this, "Removal failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error parsing Team ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a team from the table to remove.", "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Display team details
    private void displayTeamDetailsAction() {
        String normalizedUserType = loggedInUserType.toLowerCase().replace(" ", "");
        if (!PermissionChecker.canPerformAction(normalizedUserType, Action.VIEW_REPORTS)) return; 
        
        try {
            int teamId = Integer.parseInt(detailTeamIdField.getText().trim());
            Team team = controller.getTeamById(teamId);
            currentDetailTeam = team;
            
            if (team != null) {
                // Populate editable fields (which are disabled/enabled by configureAccess)
                editTeamNameField.setText(team.getTeamName());
                editUniversityField.setText(team.getUniversityName());
                
                // Display team details
                fullDetailsTextArea.setText("==== SHORT DETAILS ====\n" + team.getShortDetails() + "\n\n" +
                                   "==== FULL DETAILS ====\n" + team.getFullDetails());
            } else {
                fullDetailsTextArea.setText("Team ID " + teamId + " not found.");
                editTeamNameField.setText("");
                editUniversityField.setText("");
            }
        } catch (NumberFormatException e) {
            fullDetailsTextArea.setText("Please enter a valid Team ID (number).");
            currentDetailTeam = null;
        }
    }

    // Refresh panels
    public void refreshAllPanels() {
        populateTeamSelectors();
        populateFilterSelectors();
        refreshTeamTable();
        
        // If a team was loaded in the details panel then refresh its display
        if (currentDetailTeam != null) {
            displayTeamDetailsAction(); 
        }
    }

    // Populates the Team ID selector
    private void populateTeamSelectors() {
        List<Team> teams = controller.getAllTeams();
        
        Object currentSelection = teamIdSelector.getSelectedItem();
        
        teamIdSelector.removeAllItems();
        
        for (Team team : teams) {
            teamIdSelector.addItem(String.valueOf(team.getTeamNumber()));
        }
        
        // Restore previous selection if still available
        if (currentSelection != null && teams.stream().anyMatch(t -> String.valueOf(t.getTeamNumber()).equals(currentSelection))) {
            teamIdSelector.setSelectedItem(currentSelection);
        } else if (teamIdSelector.getItemCount() > 0) {
            teamIdSelector.setSelectedIndex(0);
        } else {
            // Clear score fields if no teams are left
            for (JTextField field : scoreFields) field.setText("");
            overallScoreLabel.setText("Overall Score: N/A");
        }
        displayCurrentScores();
    }
    
    // Populates the filter selector with team types and unique categories
    private void populateFilterSelectors() {
        Object currentSelection = filterSelector.getSelectedItem();
        filterSelector.removeAllItems();

        // Team filters
        filterSelector.addItem("All Teams");
        filterSelector.addItem("University");
        filterSelector.addItem("Professional");

        // Unique categories filter
        try {
             List<String> categories = controller.getAvailableCategories();
             for (String category : categories) {
                 filterSelector.addItem("Category: " + category);
             }
        } catch (Exception e) {
             System.err.println("Could not load categories from controller: " + e.getMessage());
        }
        
        // Restore previous selection if available
        if (currentSelection != null) {
            boolean found = false;
            for (int i = 0; i < filterSelector.getItemCount(); i++) {
                if (filterSelector.getItemAt(i).equals(currentSelection)) {
                    filterSelector.setSelectedItem(currentSelection);
                    found = true;
                    break;
                }
            }
            if (!found && filterSelector.getItemCount() > 0) {
                filterSelector.setSelectedIndex(0);
            }
        } else if (filterSelector.getItemCount() > 0) {
             filterSelector.setSelectedIndex(0);
        }
    }

    // Display scores
    private void displayCurrentScores() {
        if (teamIdSelector.getSelectedItem() == null || controller.getAllTeams().isEmpty()) {
            for (JTextField field : scoreFields) field.setText("");
            overallScoreLabel.setText("Overall Score: N/A");
            return;
        }
        
        try {
            int teamId = Integer.parseInt(teamIdSelector.getSelectedItem().toString());
            Team team = controller.getTeamById(teamId);
            
            if (team != null) {
                int[] scores = team.getScoreArray();
                for (int i = 0; i < 5; i++) {
                    scoreFields[i].setText(String.valueOf(scores[i]));
                }
                overallScoreLabel.setText(String.format("Overall Score: %.2f", team.getOverallScore()));
            }
        } catch (Exception ex) {
            for (JTextField field : scoreFields) field.setText("");
            overallScoreLabel.setText("Overall Score: Error");
        }
    }

    // Refresh team table
    private void refreshTeamTable() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Get the filter selection
        String filterSelection = (String) filterSelector.getSelectedItem();
        
        // Get all teams
        List<Team> allTeams = controller.getAllTeams();
        
        // Apply Filtering Logic
        List<Team> filteredTeams = allTeams.stream()
            .filter(team -> {
                if (filterSelection == null || filterSelection.equals("All Teams")) {
                    return true; // No filter applied
                } else if (filterSelection.equals("University")) {
                    return team instanceof UniversityTeam;
                } else if (filterSelection.equals("Professional")) {
                    return team instanceof ProfessionalTeam;
                } else if (filterSelection.startsWith("Category: ")) {
                    String categoryName = filterSelection.substring("Category: ".length()).trim();
                    return team.getTeamCategory() != null && 
                           team.getTeamCategory().getCategoryName().equalsIgnoreCase(categoryName);
                }
                return true;
            })
            .collect(Collectors.toList());
        
        // Apply Sorting Logic
        String sortBy = (String) sortSelector.getSelectedItem();
        List<Team> sortedTeams = controller.getSortedTeams(sortBy).stream()
            .filter(filteredTeams::contains) // Filter the sorted list
            .collect(Collectors.toList()); 

        // Add rows to the model
        for (Team team : sortedTeams) {
            int[] scores = team.getScoreArray();
            
            // Get the team type
            String teamType = team instanceof UniversityTeam ? "University" : 
                              (team instanceof ProfessionalTeam ? "Professional" : "Unknown");
            
            // Get the team status
            String teamStatus = team.getTeamStatus();
            
            Object[] row = {
                team.getTeamNumber(),
                team.getTeamName(),
                team.getUniversityName(),
                team.getTeamCategory().getCategoryName(), 
                teamType,
                teamStatus,
                scores[0], // CreativityScore
                scores[1], // TechnicalScore
                scores[2], // TeamworkScore
                scores[3], // PresentationScore
                scores[4], // FunctionalityScore
                String.format("%.2f", team.getOverallScore())
            };
            tableModel.addRow(row);
        }
    }
}