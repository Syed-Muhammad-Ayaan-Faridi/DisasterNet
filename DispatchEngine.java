import java.util.ArrayList;
import java.util.List;

// DispatchEngine — brain of DisasterNet
// Covers: method overloading, static method, instanceof, polymorphism, interfaces
public class DispatchEngine {

    private List<Responder> responderRoster;
    private List<DisasterEvent> activeEvents;
    private ReportManager reportManager;
    private AlertSystem alertSystem;
    private static int totalDispatches = 0;

    public DispatchEngine(ReportManager reportManager,
            AlertSystem alertSystem) {
        this.responderRoster = new ArrayList<>();
        this.activeEvents = new ArrayList<>();
        this.reportManager = reportManager;
        this.alertSystem = alertSystem;
    }

    // ─────────────────────────────────────────
    // ROSTER MANAGEMENT
    // ─────────────────────────────────────────

    public void addResponder(Responder r) {
        responderRoster.add(r);
        System.out.println("Responder registered: " + r.getName() + " (ID: " + r.getResponderID() + ")");
    }

    public void addDisasterEvent(DisasterEvent e) {
        e.activate(); // sets severity + requiredResponderTypes
        e.generateReport(); // builds report string into e.report
        activeEvents.add(e);

        // broadcast alert immediately
        alertSystem.broadcastFromDisaster(e);

        // save disaster report to file
        reportManager.saveIncidentReport(e.getReport());

        System.out.println("Disaster registered and activated: " +
                e.getClass().getSimpleName() +
                " at " + e.getLocation());
    }

    // OVERLOADED dispatch() methods

    // version 1 — basic dispatch, no resource allocation
    public void dispatch(Responder r, DisasterEvent e) {

        if (r.getDeploymentStatus()) {
            System.out.println(r.getName() + " is already deployed. " + "Cannot dispatch.");
            return;
        }

        // polymorphic — each subclass respond() behaves differently
        r.respond(e);

        // deploy through Deployable interface
        if (r instanceof Deployable) {
            ((Deployable) r).deploy(e.getLocation());
        }

        // build dispatch log entry
        String log = "DISPATCH | Responder: " + r.getName() + " | Type: " + r.getClass().getSimpleName() + " | Disaster: " + e.getClass().getSimpleName() + " | Location: " + e.getLocation() + " | Severity: " + e.getSeveritylevel() + "/5";

        // save responder log through Reportable interface
        if (r instanceof Reportable) {
            reportManager.saveDispatchLog(((Reportable) r).generateLog());
        }

        reportManager.saveDispatchLog(log);
        totalDispatches++;

        System.out.println("Dispatch complete: " + r.getName()+ " → " + e.getLocation());
    }

    // version 2 — dispatch with resource allocation
    public void dispatch(Responder r, DisasterEvent e, ResourcePool pool) {

        if (r.getDeploymentStatus()) {
            System.out.println(r.getName() + " is already deployed.");
            return;
        }

        // check pool status before allocating
        if (pool.isEmpty()) {
            System.out.println("Warning: " + pool.getPoolName() + " is empty. Dispatching without resources.");
        } else if (pool.isCriticallyLow()) {
            System.out.println("Warning: " + pool.getPoolName() + " critically low. Allocating remaining stock.");
        }

        // allocate based on responder type
        if (r instanceof MedicalUnit) {
            pool.allocate("Medicine", 50);
            ((MedicalUnit) r).restockMedicine(50);
        } else if (r instanceof RescueTeam) {
            pool.allocate("Shelter", 20);
        } else if (r instanceof FireBrigade) {
            pool.allocate("Water", 1000);
            ((FireBrigade) r).restockWater(1000);
        }

        // save resource status after allocation
        reportManager.saveResourceReport(pool.generateLog());

        // proceed with basic dispatch
        dispatch(r, e);
    }

    // version 3 — dispatch with resources and priority level
    public void dispatch(Responder r, DisasterEvent e, ResourcePool pool, int priority) {

        if (priority < 1 || priority > 5) {
            System.out.println("Invalid priority. Must be 1 to 5.");
            return;
        }

        System.out.println("Priority " + priority + " dispatch initiated for " + r.getName() + " → " + e.getLocation());

        if (priority == 5) {
            System.out.println("Maximum priority — doubling resource " + "allocation for " + r.getName() + ".");
        }

        // call version 2 — handles resource allocation + basic dispatch
        dispatch(r, e, pool);

        reportManager.saveDispatchLog("Priority " + priority + " dispatch: " + r.getName() + " → " + e.getLocation());
    }

    // AUTO DISPATCH
    // reads disaster's required responder types
    // finds matching available units automatically
    public void autoDispatch(DisasterEvent e) {

        System.out.println("\n=== AUTO DISPATCH: " +
                e.getClass().getSimpleName() +
                " at " + e.getLocation() + " ===");

        List<String> required = e.getRequiredResponderType();

        if (required.isEmpty()) {
            System.out.println("No responders required for this event.");
            return;
        }

        for (String requiredType : required) {
            boolean matched = false;

            for (Responder r : responderRoster) {

                // match string type name to actual class using instanceof
                boolean typeMatches = false;
                if (requiredType.equals("RescueTeam") &&
                        r instanceof RescueTeam) {
                    typeMatches = true;
                } else if (requiredType.equals("MedicalUnit") &&
                        r instanceof MedicalUnit) {
                    typeMatches = true;
                } else if (requiredType.equals("FireBrigade") &&
                        r instanceof FireBrigade) {
                    typeMatches = true;
                } else if (requiredType.equals("AirUnit") &&
                        r instanceof AirUnit) {
                    typeMatches = true;
                }

                // must match type AND be on standby
                if (typeMatches && !r.getDeploymentStatus()) {
                    dispatch(r, e);
                    matched = true;
                    break; // one unit per required type, move to next
                }
            }

            if (!matched) {
                System.out.println("Warning: No available " + requiredType + " found for " + e.getLocation() + ".");
                reportManager.saveDispatchLog( "UNMET REQUIREMENT: No " + requiredType + " available for " + e.getLocation());
            }
        }

        System.out.println("AUTO DISPATCH COMPLETE\n");
    }

    // STATIC prioritize() sorts disasters by severity — highest first

    public static List<DisasterEvent> prioritize(List<DisasterEvent> events) {
        int n = events.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if (events.get(j).getSeveritylevel() < events.get(j + 1).getSeveritylevel()) {
                    // swap — higher severity moves to front
                    DisasterEvent temp = events.get(j);
                    events.set(j, events.get(j + 1));
                    events.set(j + 1, temp);
                }
            }
        }

        System.out.println("Disasters prioritized by severity:");
        for (DisasterEvent e : events) {
            System.out.println("  " + e.getClass().getSimpleName() + " at " + e.getLocation() + " — Severity: " + e.getSeveritylevel());
        }
        return events;
    }

    // recall a responder back to standby
    public void recallResponder(Responder r) {
        if (!r.getDeploymentStatus()) {
            System.out.println(r.getName() + " is already on standby.");
            return;
        }
        if (r instanceof Deployable) {
            ((Deployable) r).recall();
        }
        reportManager.saveDispatchLog("RECALL: " + r.getName() + " returned to standby.");
        System.out.println(r.getName() + " recalled successfully.");
    }

    // resolve a disaster and recall all its responders
    public void resolveDisaster(DisasterEvent e) {
        activeEvents.remove(e);

        // recall every responder currently at this disaster's location
        for (Responder r : responderRoster) {
            if (r.getDeploymentStatus() && r.getCurrentZone().equals(e.getLocation())) {
                recallResponder(r);
            }
        }

        reportManager.saveIncidentReport( "RESOLVED: " + e.getClass().getSimpleName() + " at " + e.getLocation() + " marked as resolved.");

        System.out.println("Disaster resolved: " + e.getClass().getSimpleName() + " at " + e.getLocation());
    }

    // print full system status to console
    public void printSystemStatus() {
        System.out.println("\n DISASTERNET SYSTEM STATUS ");
        System.out.println("Active Disasters  : " + activeEvents.size());
        System.out.println("Total Responders  : " + responderRoster.size());
        System.out.println("Currently Deployed: " + Responder.getTotalDeployed());
        System.out.println("Total Dispatches  : " + totalDispatches);
        System.out.println("Total Alerts      : " + AlertSystem.getTotalAlertsBroadcast());
        System.out.println("Reports Saved     : " + ReportManager.getTotalReportsSaved());
        System.out.println("-------------------------------\n");
    }

    // static getter
    public static int getTotalDispatches() {
        return totalDispatches;
    }

    // getters
    public List<Responder> getResponderRoster() {
        return responderRoster;
    }

    public List<DisasterEvent> getActiveEvents() {
        return activeEvents;
    }
}
