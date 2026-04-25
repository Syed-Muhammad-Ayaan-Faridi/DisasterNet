public class AirUnit extends Responder
        implements Deployable, Trackable, Reportable {

    private int helicopterCount;
    private double maxWindTolerance;
    private boolean rescueHoistAvailable;
    private int cargoCapacity;
    private int passengerCapacity;
    private double fuelLevel;
    private String currentZone;

    public AirUnit(int responderID, String name, int numberOfSkills, int helicopterCount, double maxWindTolerance, boolean rescueHoistAvailable, int cargoCapacity, int passengerCapacity) {
        super(responderID, name, numberOfSkills);

        if (helicopterCount <= 0) {
            System.out.println("Helicopter count must be greater than 0.");
        } else {
            this.helicopterCount = helicopterCount;
        }

        if (maxWindTolerance <= 0) {
            System.out.println("Wind tolerance must be greater than 0.");
        } else {
            this.maxWindTolerance = maxWindTolerance;
        }

        this.rescueHoistAvailable = rescueHoistAvailable;
        this.cargoCapacity = cargoCapacity;
        this.passengerCapacity = passengerCapacity;
        this.fuelLevel = 100.0;
        this.currentZone = "NONE";
    }

    @Override
    public void respond(DisasterEvent event) {

        if (this.deploymentStatus) {
            System.out.println(this.name + " is already deployed at " + this.currentZone + ". Cannot redeploy.");
            return;
        }

        if (this.fuelLevel < 25.0) {
            System.out.println("Warning: " + this.name + " fuel critically low (" + this.fuelLevel +"%). Refuel before deployment.");
            return; 
        }

        if (event instanceof Cyclone) {
            Cyclone cyclone = (Cyclone) event;
            if (cyclone.getWindSpeed() > this.maxWindTolerance) {
                System.out.println(this.name + " cannot deploy. Wind speed " + cyclone.getWindSpeed() + " km/h exceeds " + "max tolerance of " + this.maxWindTolerance + " km/h.");
                return; 
            }
        }

        // calculate total evacuation and cargo capacity
        int totalEvacCapacity = this.helicopterCount * this.passengerCapacity;
        int totalCargoCapacity = this.helicopterCount * this.cargoCapacity;

        System.out.println(this.name + " responding to " +
                event.getLocation() + ". Evacuation capacity: " +
                totalEvacCapacity + " persons. Cargo: " +
                totalCargoCapacity + "kg per sortie.");

        if (this.rescueHoistAvailable) {
            System.out.println("Rescue hoist available " +
                    "— rooftop and water extraction capable.");
        } else {
            System.out.println("No rescue hoist. Landing zone required " +
                    "for evacuation.");
        }

        this.setDeploymentStatus(true);
        this.currentZone = event.getLocation();
    }

    // fuel consumption per sortie
    public void consumeFuel(double percentage) {
        this.fuelLevel = Math.max(this.fuelLevel - percentage, 0);
        if (this.fuelLevel < 25.0) {
            System.out.println("Warning: " + this.name +
                    " fuel below 25%. Return to base to refuel.");
        }
    }

    // refuel at base
    public void refuel() {
        this.fuelLevel = 100.0;
        System.out.println(this.name + " fully refuelled.");
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
        System.out.println(this.name + " recalled to airbase.");
    }

    // Trackable interface
    @Override
    public void updateLocation(String zone) {
        this.currentZone = zone;
    }

    @Override
    public String getStatus() {
        return this.deploymentStatus? "ACTIVE at " + this.currentZone : "StandBy";
    }

    @Override
    public String generateLog() {
        return ("AirUnit ID: %d | Name: %s | Helicopters: %d | " + "Max Wind: %.1f km/h | Hoist: %s | Cargo: %dkg | " + "Passengers: %d | Fuel: %.1f%% | Zone: %s | Status: %s") .formatted(this.responderID, this.name, this.helicopterCount, this.maxWindTolerance, this.rescueHoistAvailable ? "Yes" : "No", this.cargoCapacity, this.passengerCapacity, this.fuelLevel, this.currentZone, this.getStatus());
    }

    public int getHelicopterCount() {
        return helicopterCount;
    }

    public double getMaxWindTolerance() {
        return maxWindTolerance;
    }

    public boolean isRescueHoistAvailable() {
        return rescueHoistAvailable;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getCurrentZone() {
        return currentZone;
    }
}