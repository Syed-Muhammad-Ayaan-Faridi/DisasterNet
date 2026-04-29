import java.util.ArrayList;
import java.util.List;

// AlertSystem — broadcasts emergency alerts
// Covers: method overloading, static nested class
public class AlertSystem {

    private ReportManager reportManager;
    private static int totalAlertsBroadcast = 0;

    public AlertSystem(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    // STATIC NESTED CLASS
    // static — belongs to AlertSystem as a whole
    // not tied to any single AlertSystem instance
    // maintains system wide alert history
    public static class Log {

        private static List<String> alertHistory = new ArrayList<>();
        private static int alertCount = 0;
        // add new entry to history
        public static void addEntry(String entry) {
            alertCount++;
            String logEntry = "[ALERT #" + alertCount + "] " + entry;
            alertHistory.add(logEntry);
            System.out.println(logEntry);
        }
        // get full history as formatted string
        public static String getFullHistory() {
            StringBuilder history = new StringBuilder();
            history.append("=== ALERT HISTORY ===\n");
            for (String entry : alertHistory) {
                history.append(entry).append("\n");
            }
            history.append("Total Alerts: ").append(alertCount);
            return history.toString();
        }
        // get most recent alert
        public static String getLatestAlert() {
            if (alertHistory.isEmpty()) {
                return "No alerts broadcast yet.";
            }
            return alertHistory.get(alertHistory.size() - 1);
        }
        // clear history — used when starting a new session
        public static void clearHistory() {
            alertHistory.clear();
            alertCount = 0;
            System.out.println("Alert history cleared.");
        }
        public static int getAlertCount() {
            return alertCount;
        }
    }

    // OVERLOADED broadcast() methods
    // three versions — basic, targeted, full

    // version 1 — basic zone alert
    public void broadcast(String zone, int severity) {
        if (severity < 1 || severity > 5) {
            System.out.println("Invalid severity. Must be between 1 and 5.");
            return;
        }
        String message = "EMERGENCY ALERT | Zone: " + zone + " | Severity: " + severity + "/5" + " | All residents evacuate immediately.";
        AlertSystem.Log.addEntry(message);
        reportManager.saveIncidentReport("ALERT: " + message);
        totalAlertsBroadcast++;
    }

    // version 2 — targeted alert to specific contacts
    public void broadcast(String zone, int severity, String[] contacts) {
        if (severity < 1 || severity > 5) {
            System.out.println("Invalid severity. Must be between 1 and 5.");
            return;
        }
        String message = "TARGETED ALERT | Zone: " + zone + " | Severity: " + severity + "/5" + " | Notifying " + contacts.length + " contacts.";
        AlertSystem.Log.addEntry(message);
        // notify each contact
        for (String contact : contacts) {
            System.out.println("Notifying: " + contact + " — " + message);
        }
        reportManager.saveIncidentReport("TARGETED ALERT: " + message);
        totalAlertsBroadcast++;
    }

    // version 3 — full alert with disaster type and instructions
    public void broadcast(String zone, int severity, String disasterType, String instructions) {
        if (severity < 1 || severity > 5) {
            System.out.println("Invalid severity. Must be between 1 and 5.");
            return;
        }
        String message = "FULL EMERGENCY ALERT" + " | Disaster: " + disasterType + " | Zone: " + zone + " | Severity: " + severity + "/5" + " | Instructions: " + instructions;
        AlertSystem.Log.addEntry(message);
        reportManager.saveIncidentReport("FULL ALERT: " + message);
        totalAlertsBroadcast++;
    }

    // broadcast alert directly from a disaster event
    public void broadcastFromDisaster(DisasterEvent event) {
        String zone = event.getLocation();
        int severity = event.getSeveritylevel();
        String disasterType = event.getClass().getSimpleName();
        String instructions = severity >= 4
                ? "Evacuate immediately. Do not return until all clear."
                : "Stay alert. Follow official guidance.";

        broadcast(zone, severity, disasterType, instructions);
    }

    // save full alert history to file
    public void saveAlertHistory() {
        reportManager.saveIncidentReport(AlertSystem.Log.getFullHistory());
        System.out.println("Alert history saved to incident log.");
    }

    // static getter
    public static int getTotalAlertsBroadcast() {
        return totalAlertsBroadcast;
    }
}
