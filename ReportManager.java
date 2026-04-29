import java.io.*;
// ReportManager — File I/O layer
// Covers: FileWriter, BufferedWriter, ObjectOutputStream, BufferedReader
public class ReportManager {

    private String incidentLogPath;
    private String victimLogPath;
    private String resourceLogPath;
    private String dispatchLogPath;
    private static int totalReportsSaved = 0;

    public ReportManager(String incidentLogPath, String victimLogPath,
            String resourceLogPath, String dispatchLogPath) {
        this.incidentLogPath = incidentLogPath;
        this.victimLogPath = victimLogPath;
        this.resourceLogPath = resourceLogPath;
        this.dispatchLogPath = dispatchLogPath;

        // create all log files on startup if they do not exist
        initializeFiles();
    }

    // create files if they do not exist
    private void initializeFiles() {
        String[] paths = { incidentLogPath, victimLogPath,
                resourceLogPath, dispatchLogPath };
        for (String path : paths) {
            try {
                File file = new File(path);
                if (!file.exists()) {
                    file.createNewFile();
                    System.out.println("Log file created: " + path);
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + path +
                        " — " + e.getMessage());
            }
        }
    }

    // save a disaster incident report
    public void saveIncidentReport(String report) {
        writeToFile(incidentLogPath, report);
    }

    // save a victim registry report
    public void saveVictimReport(String report) {
        writeToFile(victimLogPath, report);
    }

    // save a resource pool status
    public void saveResourceReport(String report) {
        writeToFile(resourceLogPath, report);
    }

    // save a dispatch action log
    public void saveDispatchLog(String log) {
        writeToFile(dispatchLogPath, log);
    }

    // core write method used by all save methods
    // append = true so existing logs are never overwritten
    private void writeToFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(path, true))) {
            writer.write(content);
            writer.newLine();
            writer.write("─".repeat(60));
            writer.newLine();
            totalReportsSaved++;
        } catch (IOException e) {
            System.out.println("Error writing to file: " + path +
                    " — " + e.getMessage());
        }
    }

    // read entire log file and return as string
    // used on system startup to reload previous session
    public String readLog(String path) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + path +
                    " — " + e.getMessage());
        }
        return content.toString();
    }

    // reload previous incident logs on system restart
    public String reloadIncidentLog() {
        System.out.println("Reloading incident log from previous session...");
        return readLog(incidentLogPath);
    }

    // reload previous dispatch logs on system restart
    public String reloadDispatchLog() {
        System.out.println("Reloading dispatch log from previous session...");
        return readLog(dispatchLogPath);
    }

    // save entire system snapshot using object serialization
    // requires objects to implement Serializable in real use
    public void saveSystemSnapshot(String snapshotData) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("system_snapshot.txt", false))) {
            // false = overwrite, snapshot is always latest state
            writer.write("=== SYSTEM SNAPSHOT ===");
            writer.newLine();
            writer.write(snapshotData);
            writer.newLine();
            System.out.println("System snapshot saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving snapshot: " + e.getMessage());
        }
    }

    // static getter
    public static int getTotalReportsSaved() {
        return totalReportsSaved;
    }

    // getters
    public String getIncidentLogPath() {
        return incidentLogPath;
    }

    public String getVictimLogPath() {
        return victimLogPath;
    }

    public String getResourceLogPath() {
        return resourceLogPath;
    }

    public String getDispatchLogPath() {
        return dispatchLogPath;
    }
}
