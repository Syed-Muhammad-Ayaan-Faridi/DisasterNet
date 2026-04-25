public class FireBrigade extends Responder implements Deployable, Trackable, Reportable {

    private int truckCount;
    private double waterCapacity;
    private boolean isHazmatTrained;
    private boolean foamAvailable;
    private int ladderHeight;
    private double currentWaterLevel;
    private String currentZone;

    public FireBrigade(int responderID, String name, int numberOfSkills, int truckCount, double waterCapacity, boolean isHazmatTrained, boolean foamAvailable, int ladderHeight) {
        super(responderID, name, numberOfSkills);

        if (truckCount <= 0) {
            System.out.println("Truck count must be greater than 0.");
        } else {
            this.truckCount = truckCount;
        }

        if (waterCapacity <= 0) {
            System.out.println("Water capacity must be greater than 0.");
        } else {
            this.waterCapacity    = waterCapacity;
            this.currentWaterLevel = waterCapacity; // starts full
        }

        this.isHazmatTrained = isHazmatTrained;
        this.foamAvailable   = foamAvailable;
        this.ladderHeight    = ladderHeight;
        this.currentZone     = "NONE";
    }

    @Override
    public void respond(DisasterEvent event) {

        if (this.deploymentStatus) {
            System.out.println(this.name + " is already deployed at " + this.currentZone + ". Cannot redeploy.");
            return;
        }

        // water level check before deploying
        if (this.currentWaterLevel < this.waterCapacity * 0.2) {
            System.out.println("Warning: " + this.name +  " water level critically low (" + this.currentWaterLevel + "L). " + "Requesting restock before deployment.");
        }

        // hazmat check for wildfire with chemical risk
        if (event instanceof WildFire && !this.isHazmatTrained) {
            System.out.println("Warning: " + this.name + " is not hazmat trained. Industrial" + " fire risk present. Deploying with caution.");
        }

        // suppression capacity — each truck covers roughly 500 sq metres
        int suppressionArea = this.truckCount * 500;
        System.out.println(this.name + " responding to " + event.getLocation() + ". Suppression area: " + suppressionArea + " sq metres. Water: " + this.currentWaterLevel + "L available.");

        if (this.foamAvailable) {
            System.out.println("Foam suppressant available " + "chemical fire capable.");
        }

        this.setDeploymentStatus(true);
        this.currentZone = event.getLocation();
    }

    // consume water during suppression
    public void consumeWater(double litres) {
        if (litres > this.currentWaterLevel) {
            System.out.println("Insufficient water. Only " + this.currentWaterLevel + "L remaining.");
            this.currentWaterLevel = 0;
        } else {
            this.currentWaterLevel -= litres;
        }
        // trigger restock warning at 20% capacity
        if (this.currentWaterLevel < this.waterCapacity * 0.2) {
            System.out.println("Critical: " + this.name + " water below 20%. Restock required.");
        }
    }

    // restock from ResourcePool
    public void restockWater(double litres) {
        this.currentWaterLevel = Math.min(this.currentWaterLevel + litres, this.waterCapacity);
        System.out.println(this.name + " water restocked to " + this.currentWaterLevel + "L.");
    }

    // Deployable interface
    @Override
    public void deploy(String zone) {
        this.currentZone = zone;
        this.setDeploymentStatus(true);
        System.out.println(this.name + " deployed to " + zone + ".");
    }

    @Override
    public void recall() {
        this.currentZone = "NONE";
        this.setDeploymentStatus(false);
        System.out.println(this.name + " recalled to station.");
    }

    // Trackable interface
    @Override
    public void updateLocation(String zone) {
        this.currentZone = zone;
    }

    @Override
    public String getStatus() {
        return this.deploymentStatus ? "ACTIVE at " + this.currentZone : "STANDBY";
    }

    // Reportable interface
    @Override
    public String generateLog() {
        return ("FireBrigade ID: %d | Name: %s | Trucks: %d | " + "Water: %.1fL / %.1fL | Hazmat: %s | Foam: %s | " + "Ladder: %dm | Zone: %s | Status: %s") .formatted(this.responderID, this.name, this.truckCount, this.currentWaterLevel, this.waterCapacity, this.isHazmatTrained ? "Yes" : "No", this.foamAvailable ? "Yes" : "No", this.ladderHeight, this.currentZone, this.getStatus() );
    }

    // getters
    public int getTruckCount()           { return truckCount; }
    public double getWaterCapacity()     { return waterCapacity; }
    public double getCurrentWaterLevel() { return currentWaterLevel; }
    public boolean isHazmatTrained()     { return isHazmatTrained; }
    public boolean isFoamAvailable()     { return foamAvailable; }
    public int getLadderHeight()         { return ladderHeight; }
    public String getCurrentZone()       { return currentZone; }
}