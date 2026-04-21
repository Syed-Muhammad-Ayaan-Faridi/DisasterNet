public class WildFire extends DisasterEvent {
    private double spreadRate; // measured in km/h
    private double windSpeed; // measured in km/h
    private boolean evacuationOrdered;

    WildFire(String eventID,String location, int level, String timestamp, double spreadRate, double windSpeed){
        super(eventID, location, level, timestamp);
        this.spreadRate = spreadRate;
        this.windSpeed = windSpeed;
        this.evacuationOrdered = false;
    }

    @Override
    void activate() {
        if (this.spreadRate > 0 && this.spreadRate <= 0.1) {
            this.severitylevel = 1;
        } else if (this.spreadRate > 0.1 && this.spreadRate <= 0.5) {
            this.severitylevel = 2;
        } else if (this.spreadRate > 0.5 && this.spreadRate <= 2) {
            this.severitylevel = 3;
        } else if (this.spreadRate > 2 && this.spreadRate <= 6) {
            this.severitylevel = 4;
        } else if (this.spreadRate > 6) {
            this.severitylevel = 5;
        } else {
            System.out.println("Invalid spread rate");
        }

        if (this.windSpeed > 10 && this.windSpeed <= 20) {
            this.severitylevel += 1;
            if (this.severitylevel >= 5) this.severitylevel = 5;
        }else if (this.windSpeed > 20) {
            this.severitylevel += 2;
            if (this.severitylevel >= 5) this.severitylevel = 5;
        }

        if (this.severitylevel >= 2){ 
            this.rescueTeamRequired = true;
            this.requiredResponderType.add("FireBrigade");
        }
        if (this.severitylevel >= 3) {
            this.danger = true;
            this.evacuationOrdered = true;
            this.requiredResponderType.add("RescueTeam");
            this.requiredResponderType.add("MedicalUnit");
        }
        if (this.severitylevel >= 4) this.requiredResponderType.add("AirUnit");
    }

    @Override
    void generateReport() {
        if (this.rescueTeamRequired) {
            this.report = "Event ID: %s | WildFire reported in %s at %s. Spread rate of wildfire is %f with a wind speed of %f. The priority level is %d/5. Evacuation required %b. Reponders required are:".formatted(this.eventID, this.location, this.Timestamp, this.spreadRate, this.windSpeed, this.severitylevel, this.evacuationOrdered);
            for (String string : requiredResponderType) {
                this.report += string + " ";
            }
        } else
            this.report = "Event ID: %s | WildFire reported in %s at %s. Spread rate of wildfire is %f with a wind speed of %f. The priority level is %d/5. Evacuation required %b.".formatted(this.eventID, this.location, this.Timestamp, this.spreadRate, this.windSpeed, this.severitylevel, this.evacuationOrdered);
        System.out.println("Report generated Successfully");
    }
}
