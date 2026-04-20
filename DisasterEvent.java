import java.util.ArrayList;

public abstract class DisasterEvent {
    protected final String eventID; // set one time for each disaster
    protected String location; // where disaster occurred 
    protected int severitylevel; //between 1-5 priority
    protected String Timestamp; // when disaster occured 
    protected boolean danger;
    protected boolean rescueTeamRequired;
    protected ArrayList<String> requiredResponderType = new ArrayList<>();
    protected String report;
    private static int eventCount = 0; // note how many disasters occured throughout the system
    
    DisasterEvent(String eventID,String location, int level, String timestamp){
        this.eventID = eventID;
        this.location = location;
        this.severitylevel = level;
        this.Timestamp = timestamp;
        this.danger = false;
        this.rescueTeamRequired = false;
        DisasterEvent.eventCount++;
    }

    abstract void activate(); //triggers the disaster in the system
    abstract void generateReport(); //disaster is written in the form of report 

    //getter 
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