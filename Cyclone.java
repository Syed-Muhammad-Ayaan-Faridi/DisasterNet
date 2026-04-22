public class Cyclone extends DisasterEvent {

    private int category;
    private double windSpeed;
    private double stormSurgeHeight;
    private boolean isLandfalling;
    private double coastalPopulation;

    public Cyclone(String eventID, String location, String timestamp, int level,int category, double windSpeed, double stormSurgeHeight,boolean isLandfalling, double coastalPopulation) {
        super(eventID, location, level , timestamp);

        if (category < 1 || category > 5) System.out.println("Invalid category. Must be between 1 and 5.");
        else this.category = category;

        if (windSpeed < 0) System.out.println("Wind speed cannot be negative.");
        else this.windSpeed = windSpeed;

        this.stormSurgeHeight = stormSurgeHeight;
        this.isLandfalling = isLandfalling;
        this.coastalPopulation = coastalPopulation;
    }

    @Override
    void activate() {

        this.severitylevel = this.category;

        // storm surge is the real killer adjust severity upward if severe
        if (this.stormSurgeHeight > 5) {
            this.severitylevel += 2;
            if (this.severitylevel >= 5) this.severitylevel = 5;
        } else if (this.stormSurgeHeight > 2) {
            this.severitylevel += 1;
            if (this.severitylevel >= 5) this.severitylevel = 5;
        }

        // same category cyclone hitting 5 million people vs 50,000
        // needs a completely different scale of response
        if (this.coastalPopulation > 2.0) {
            this.severitylevel += 1;
            if (this.severitylevel >= 5) this.severitylevel = 5;
        }

        // dispatch based on final severity
        if (this.severitylevel >= 1) {
            this.danger = true;
            this.rescueTeamRequired = true;
            this.requiredResponderType.add("MedicalUnit");
        }

        if (this.severitylevel >= 2) {
            this.rescueTeamRequired = true;
            this.requiredResponderType.add("RescueTeam");
        }

        if (this.severitylevel >= 3) {
            this.requiredResponderType.add("AirUnit");
        }

        if (this.severitylevel >= 4) {
            this.requiredResponderType.add("FireBrigade");
        }

        // helicopters cannot safely operate above 120 km/h winds
        if (this.windSpeed > 120) {
            this.requiredResponderType.remove("AirUnit");
            System.out.println("Warning: Wind speed exceeds 120 km/h. " + "AirUnit deployment suspended until winds subside.");
        }

        // landfall changes response mode entirely
        if (this.isLandfalling) {
            // immediate rescue over preparation
            if (!this.requiredResponderType.contains("RescueTeam")) {
                this.requiredResponderType.add("RescueTeam");
            }
            System.out.println("Cyclone has caused a landfall. " + "Switching to immediate rescue mode.");
        }
    }

    @Override
    void generateReport() {
        this.report = ("Event ID: %s | Cyclone in %s | Category: %d | Wind Speed: %.1f km/h | Storm Surge: %.1f m | Coastal Population at risk: %.2f million | Landfalling: %s | Severity: %d/5 | Time: %s").formatted(
                        this.eventID, this.location, this.category, this.windSpeed, this.stormSurgeHeight, this.coastalPopulation, this.isLandfalling ? "Yes" : "No", this.severitylevel, this.Timestamp);

        if (this.rescueTeamRequired) {
            this.report += " | Responders required: " + String.join(", ", this.requiredResponderType);
        } else {
            this.report += " | Monitoring only. No immediate rescue required.";
        }

        System.out.println("Cyclone report generated successfully.");
    }

    // getters
    public int getCategory() {
        return category;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getStormSurgeHeight() {
        return stormSurgeHeight;
    }

    public boolean isLandfalling() {
        return isLandfalling;
    }

    public double getCoastalPopulation() {
        return coastalPopulation;
    }
}
