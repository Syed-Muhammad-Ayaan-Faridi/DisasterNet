import java.util.ArrayList;
import java.util.List;

public class RescueTeam extends Responder implements Deployable,Reportable,Trackable{
    private int teamSize;
    private int vehicleCount;
    private boolean isHeavyRescue; //Heavy rescue handle collapsed buildings.
    private String currentZone;
    private List<String> equipmentList;

    RescueTeam(int responderID, String name, int numberOfSkills,int teamsize, int vehicleCount, boolean isheavy){
        super(responderID, name, numberOfSkills);

        if (teamsize > 0 ) this.teamSize = teamsize;
        else System.out.println("Team size can not be zero");

        if (vehicleCount > 0 ) this.vehicleCount = vehicleCount;
        else System.out.println("Team size can not be negative or zero");

        this.vehicleCount = vehicleCount;
        this.isHeavyRescue = isheavy;
        this.currentZone = "NONE";
        this.equipmentList = new ArrayList<>();
    }

    @Override
    void respond(DisasterEvent event) {
        if (this.deploymentStatus) {
            System.out.println(this.name + " is already deployed at " + this.currentZone + ". Cannot redeploy.");
            return;
        }

        // heavy rescue check — warn if wrong team type for the disaster
        if (event instanceof Earthquake && !this.isHeavyRescue) {
            System.out.println("Warning: " + this.name + " is a light rescue team. Earthquake requires heavy rescue. Deploying anyway.");
        }

        // calculate how many survivors this team can handle per trip assuming each vehicle carries roughly 8 survivors
        int evacuationCapacity = this.vehicleCount * 8 * this.teamSize;
        System.out.println(this.name + " responding to " + event.getLocation() + " with evacuation" + " capacity of " + evacuationCapacity + " persons.");

        // update deployment state
        this.setDeploymentStatus(true);
        this.currentZone = event.getLocation();

        System.out.println(this.name + " is now active at " + this.currentZone + ".");
    }

    //Deployable Interface running
    @Override
    public void deploy(String zoneName) {
        if (zoneName != null) {
            this.currentZone = zoneName;
            setDeploymentStatus(true);
            System.out.printf("%s deployed to %s.",this.name,this.currentZone);
        } else {
            System.out.println("No zone name detected");
        }
    }

    @Override
    public void recall() {
        this.currentZone = "NONE";
        setDeploymentStatus(false);
        System.out.println(this.name + " recalled. Now on standby.");
    }

    //Trackable Interface Running 
    @Override
    public void updateLocation(String coordinates) {
        if (coordinates != null) {
            this.currentZone = coordinates;
            System.out.printf("%s Location updated to %s",this.name,this.currentZone);
        } else System.out.println("Invalid location entered, can not update");
    }

    @Override
    public String getStatus() {
        if (this.deploymentStatus) {
            return "Active at: "+this.currentZone;
        } else return "On Standby";
    }

    // Reportable Interface running
    @Override
    public String generateLog() {
        return ("RescueTeam ID: %d | Name: %s | Team Size: %d | Vehicles: %d | Heavy Rescue: %s | Zone: %s | Status: %s").formatted(this.responderID, this.name, this.teamSize, this.vehicleCount, this.isHeavyRescue ? "Yes" : "No", this.currentZone, this.getStatus());
    }

    // Adding equipments 
    public void addEquipment(String item){
        this.equipmentList.add(item);
    }

    // getters
    public int getTeamSize()  { return teamSize; }
    public int getVehicleCount() { return vehicleCount; }
    public boolean isHeavyRescue() { return isHeavyRescue; }
    public String getCurrentZone() { return currentZone; }
    public List<String> getEquipmentList() { return equipmentList; }
}
