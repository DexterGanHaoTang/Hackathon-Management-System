package hackathon.management.system;

/**
 * @author GAN HAO TANG
 * @studentID SCSJ2100495
 */

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * PermissionChecker Class
 * Manage and check permissions based on user role to enable or disable components
 */
public class PermissionChecker {

    // Define the core set of actions in the application
    public enum Action {
        // Main actions
        VIEW_REPORTS,          // View and Sort Teams
        UPDATE_SCORES,         // View and Edit Team Scores
        DELETE_TEAM,           // Remove Team
        MANAGE_USERS,          // View and Edit Team Details
    }

    // Can only view and not edit
    private static final Set<Action> VIEW_ONLY_PERMISSIONS = Collections.unmodifiableSet(
        EnumSet.of(Action.VIEW_REPORTS)
    );
    
    // Competitors and Public users have the same view only permissions
    private static final Set<Action> COMPETITOR_PERMISSIONS = VIEW_ONLY_PERMISSIONS;
    private static final Set<Action> PUBLIC_PERMISSIONS = VIEW_ONLY_PERMISSIONS;

    // Judge users can view and edit scores
    private static final Set<Action> JUDGE_PERMISSIONS = Collections.unmodifiableSet(
        EnumSet.of(Action.VIEW_REPORTS, Action.UPDATE_SCORES)
    );
    
    // Staff and Judge have identical permissions
    private static final Set<Action> STAFF_PERMISSIONS = JUDGE_PERMISSIONS; 
    
    // EventStaff can view, remove team and edit team details
    private static final Set<Action> EVENTSTAFF_PERMISSIONS = Collections.unmodifiableSet(
        EnumSet.of(Action.VIEW_REPORTS, Action.DELETE_TEAM, Action.MANAGE_USERS)
    );
    
    // Organizers can view
    private static final Set<Action> ORGANIZER_PERMISSIONS = Collections.unmodifiableSet(
        EnumSet.of(Action.VIEW_REPORTS)
    );

    // Checks if a user can perform a specific action based on their userType
    public static boolean canPerformAction(String userType, Action action) {
        if (userType == null) return false;

        switch (userType.toLowerCase()) {
            case "competitor":
                return COMPETITOR_PERMISSIONS.contains(action);
            case "public":
                return PUBLIC_PERMISSIONS.contains(action);
            case "judge":
                return JUDGE_PERMISSIONS.contains(action);
            case "staff":
                return STAFF_PERMISSIONS.contains(action);
            case "eventstaff":
                return EVENTSTAFF_PERMISSIONS.contains(action);
            case "organizer":
                return ORGANIZER_PERMISSIONS.contains(action);
            default:
                // Default to no permissions for unrecognised types
                return false;
        }
    }
}