import java.util.ArrayList;

abstract class DisasterEvent {
    protected final String eventID; // set one time for each disaster
    protected String location; // where disaster occurred
    protected int severitylevel; // between 1-5 priority
    protected String Timestamp; // when disaster occured
    protected boolean danger;
    protected boolean rescueTeamRequired;
    protected ArrayList<String> requiredResponderType = new ArrayList<>();
    protected String report;
    private static int eventCount = 0; // note how many disasters occured throughout the system

    DisasterEvent(String eventID, String location, int level, String timestamp) {
        this.eventID = eventID;
        this.location = location;
        this.severitylevel = level;
        this.Timestamp = timestamp;
        this.danger = false;
        this.rescueTeamRequired = false;
        DisasterEvent.eventCount++;
    }

    abstract void activate(); // triggers the disaster in the system

    abstract void generateReport(); // disaster is written in the form of report

    // getter
    public String getEventID() {
        return eventID;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getRequiredResponderType() {
        return requiredResponderType;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public static int getEventCount() {
        return eventCount;
    }

    public int getSeveritylevel() {
        return severitylevel;
    }

    public String getReport() {
        return report;
    }
}
class Earthquake extends DisasterEvent {
    private double magnitude;
    private String depth; // Shallow or Deep Earthquake

    Earthquake(String eventID, String location, int level, String timestamp, double magnitude, String depth) {
        super(eventID, location, level, timestamp);
        this.magnitude = magnitude;
        this.depth = depth;
    }

    void activate() {
        //set severity level based on magnitude irrespective of depth
        if (this.magnitude >= 1 && this.magnitude <= 3) {
            this.severitylevel = 1;
        } else if (this.magnitude > 3 && this.magnitude <= 5) {
            this.severitylevel = 2;
        } else if (this.magnitude > 5 && this.magnitude <= 7) {
            this.severitylevel = 3;
        } else if (this.magnitude > 7 && this.magnitude <= 9) {
            this.severitylevel = 4;
        } else if (this.magnitude > 9 && this.magnitude <= 10) {
            this.severitylevel = 5;
        } else {
            System.out.println("Invalid Magnitude!\nEnter magnitude between 1 and 10");
        }

        // Update severity level based on depth 
        if (this.depth.equalsIgnoreCase("shallow")) {
            // if depth is shallow then increasing severity level by 1 level, max can be 5
            this.severitylevel = Math.min(this.severitylevel + 1, 5);
        } else if (this.depth.equalsIgnoreCase("deep")) {
            // if depth is deep then reduce severity level by 1 level, min can be 1
            this.severitylevel = Math.max(this.severitylevel - 1, 1);
        }
        //based on final severity level set rescue status
        if (this.severitylevel >= 3) {
            this.danger = true;
            this.rescueTeamRequired = true;
            this.requiredResponderType.add("RescueTeam");
            this.requiredResponderType.add("MedicalUnit");
        }
        if (this.severitylevel >= 4) {
            this.requiredResponderType.add("AirUnit"); // aerial support at high severity
        }
        if (this.severitylevel == 5) {
            this.requiredResponderType.add("FireBrigade"); // gas line fires at catastrophic level
        }
    }

    void generateReport() {
        if (this.rescueTeamRequired) {
            this.report = "Event ID: %s | Earthquake reported in %s of Depth: %s and  magnitude %f at %s. The priority level is %d/5. Reponders required are:".formatted(this.eventID, this.location, this.depth,this.magnitude, this.Timestamp, this.severitylevel);
            for (String string : requiredResponderType) {
                this.report += string + " ";
            }
        } else {
            this.report = "Event ID: %s Earthquake reported in %s of Depth: %s and magnitude %f at %s. The priority level is %d/5. No rescue required.".formatted(this.eventID, this.location, this.depth ,this.magnitude, this.Timestamp, this.severitylevel);
        }
        System.out.println("Report generated Successfully");
    }
}


// parent of all rescue units 
abstract class Responder {
    protected final int ResponderID;
    protected String name;
    protected boolean deployStatus; // true = active 
    private String[] skills;
    protected static int totalDeployed = 0;

    Responder(int ResponderID, String name, int numberOfSkills){
        this.ResponderID = ResponderID;
        this.name = name;
        this.skills = new String[numberOfSkills];
    }

    abstract void respond(DisasterEvent eve); // because a medical unit and a fire brigade respond very differently
}


public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}


//Classes ready to use: Disaster Event and Earthquake
//Classes work in progress: Responder and rest