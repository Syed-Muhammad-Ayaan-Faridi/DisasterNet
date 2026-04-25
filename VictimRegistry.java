import java.util.ArrayList;
import java.util.List;

public class VictimRegistry {

    private List<Victim> victimList;
    private static int totalVictimsRegistered = 0;
    private static int totalRescued = 0;
    private static int totalDeceased = 0;

    public VictimRegistry() {
        this.victimList = new ArrayList<>();
    }

    // register a new victim into the system
    public void registerVictim(Victim victim) {
        // check for duplicate ID
        for (Victim v : victimList) {
            if (v.getVictimID().equals(victim.getVictimID())) {
                System.out.println("Victim ID " + victim.getVictimID() + " already registered.");
                return;
            }
        }
        this.victimList.add(victim);
        totalVictimsRegistered++;
        System.out.println("Victim registered: " + victim.getName() + " (ID: " + victim.getVictimID() + ") — Status: MISSING");
    }

    // find victim by ID
    public Victim findVictim(String victimID) {
        for (Victim v : victimList) {
            if (v.getVictimID().equals(victimID)) {
                return v;
            }
        }
        System.out.println("Victim ID " + victimID + " not found.");
        return null;
    }

    // update a victim's status
    public void updateVictimStatus(String victimID, Victim.Status newStatus) {
        Victim v = findVictim(victimID);
        if (v != null) {
            // track rescued and deceased counts statically
            if (newStatus == Victim.Status.RESCUED) {
                totalRescued++;
            } else if (newStatus == Victim.Status.DECEASED) {
                totalDeceased++;
            }
            v.updateStatus(newStatus);
        }
    }

    // get all victims with a specific status
    public List<Victim> getVictimsByStatus(Victim.Status status) {
        List<Victim> result = new ArrayList<>();
        for (Victim v : victimList) {
            if (v.getStatus() == status) {
                result.add(v);
            }
        }
        return result;
    }

    // get all victims from a specific disaster location
    public List<Victim> getVictimsByLocation(String location) {
        List<Victim> result = new ArrayList<>();
        for (Victim v : victimList) {
            if (v.getDisasterLocation().equals(location)) {
                result.add(v);
            }
        }
        return result;
    }

    // add medical record to a specific victim
    public void addMedicalRecord(String victimID, String bloodType, String[] injuries, boolean requiresHospital) {
        Victim v = findVictim(victimID);
        if (v != null) {
            Victim.MedicalRecord record = v.createMedicalRecord(bloodType, injuries, requiresHospital);
            System.out.println("Medical record created for " + v.getName() + ".");

            // if hospital required — flag status as critical
            if (requiresHospital) {
                updateVictimStatus(victimID, Victim.Status.CRITICAL);
            }
        }
    }

    // assign a medical unit to a victim's medical record
    public void assignMedicalUnit(String victimID, String unitName) {
        Victim v = findVictim(victimID);
        if (v != null && v.getMedicalRecord() != null) {
            v.getMedicalRecord().assignUnit(unitName);
            System.out.println(unitName + " assigned to victim " + v.getName() + ".");
        }
    }

    // generate full registry report for ReportManager
    public String generateRegistryReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== VICTIM REGISTRY REPORT ===\n");
        report.append("Total Registered: ").append(totalVictimsRegistered).append(" | Rescued: ").append(totalRescued).append(" | Deceased: ").append(totalDeceased).append(" | Still Missing: ").append(getVictimsByStatus(Victim.Status.MISSING).size()).append("\n\n");

        for (Victim v : victimList) {
            report.append(v.generateLog()).append("\n");
            // include medical summary if record exists
            if (v.getMedicalRecord() != null) {
                report.append("  └─ ") .append(v.getMedicalRecord().getMedicalSummary()) .append("\n");
            }
        }
        return report.toString();
    }

    // static getters — system wide counts
    public static int getTotalVictimsRegistered() { return totalVictimsRegistered; }
    public static int getTotalRescued()           { return totalRescued; }
    public static int getTotalDeceased()          { return totalDeceased; }

    // getter
    public List<Victim> getVictimList() { return victimList; }
}