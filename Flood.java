public class Flood extends DisasterEvent {
    private double waterLevel; // in meters
    private double affectedArea; // in squared km
    private boolean isDamBreak;
    private boolean isCoastalArea;
    private boolean evacuationOrdered;

    Flood(String eventID, String location, int severitylevel, String Timestamp, double waterLevel, double affectedArea,
            boolean coastalArea, boolean DamBreak) {
        super(eventID, location, severitylevel, Timestamp);
        this.waterLevel = waterLevel;
        this.affectedArea = affectedArea;
        this.isCoastalArea = coastalArea;
        this.isDamBreak = DamBreak;
        this.evacuationOrdered = false;
    }

    @Override
    void activate() {
        if (this.waterLevel > 0 && this.waterLevel <= 1) { // ankle to knee level
            this.severitylevel = 1;
        } else if (this.waterLevel > 1 && this.waterLevel <= 2) { // waist level and vehicle swimming
            this.severitylevel = 2;
        } else if (this.waterLevel > 2 && this.waterLevel <= 4) { // above head level
            this.severitylevel = 3;
        } else if (this.waterLevel > 4 && this.waterLevel <= 7) { // above double/triple storey buildings
            this.severitylevel = 4;
        } else if (this.waterLevel > 7) { // entire district submerged
            this.severitylevel = 5;
        } else {
            System.out.println("Invalid water level");
        }
        // Update severity level based on Area affected in square kilometer
        if (this.affectedArea > 50) {
            // massive scale flood so update severity up by 1
            this.severitylevel += 1;
            if(this.severitylevel > 5) this.severitylevel = 5;
        } else if (this.affectedArea > 20) {
            // large scale flood so no severity change but add extra AirUnit as more than 1 teamw required
            this.requiredResponderType.add("AirUnit");
        }

        if (this.isDamBreak) {
            this.severitylevel += 2;
            if (this.severitylevel > 5)
                this.severitylevel = 5;
        }
        if (this.isCoastalArea) {
            this.severitylevel += 1;
            if (this.severitylevel > 5)
                this.severitylevel = 5;
        }
        // activations based on severity level
        if (this.severitylevel >= 2) {
            this.danger = true;
            this.rescueTeamRequired = true;
            this.requiredResponderType.add("RescueTeam");
        }
        if (this.severitylevel >= 3) {
            this.requiredResponderType.add("MedicalUnit");
            this.requiredResponderType.add("AirUnit"); // boats and helicopters critical for floods
        }
        if (this.severitylevel >= 4) {
            this.requiredResponderType.add("FireBrigade"); // pump out water, rescue from rooftops
        }
        if (this.severitylevel == 5 || this.isDamBreak) {
            // mass evacuation needed — all units
            this.evacuationOrdered = true;
        }
    }

    @Override
    void generateReport() {
        if (this.rescueTeamRequired) {
            this.report = "Event ID: %s | Flood reported in %s | Water Depth (in meters): %f | Affected Area (in square km): %f at Time: %s. Is it Coastal Area: %b | Dam Break reported : %b | Evacuation Ordered: %b . The priority level is %d/5. Reponders required are:".formatted(this.eventID,this.location,this.waterLevel,this.affectedArea,this.Timestamp, this.isCoastalArea,this.isDamBreak,this.evacuationOrdered,this.severitylevel);
            for (String string : requiredResponderType) {
                this.report += string + " ";
            }
        } else {
            this.report = "Event ID: %s | Flood reported in %s | Water Depth (in meters): %f | Affected Area (in square km): %f at Time: %s. Is it Coastal Area: %b | Dam Break reported : %b | Evacuation Ordered: %b . The priority level is %d/5.".formatted(this.eventID,this.location,this.waterLevel,this.affectedArea,this.Timestamp, this.isCoastalArea,this.isDamBreak,this.evacuationOrdered,this.severitylevel);
        }
        System.out.println("Report generated Successfully");
    }
    // Getters 
    public double getAffectedArea() {
        return affectedArea;
    }
    public double getWaterLevel() {
        return waterLevel;
    }
    public boolean getDamBreak(){
        return this.isDamBreak;
    }
    public boolean getCoastalArea(){
        return this.isCoastalArea;
    }
    public boolean getevacuationOrder(){
        return this.evacuationOrdered;
    }
    //Setters 
    public void setDamBreak(boolean isDamBreak) {
        this.isDamBreak = isDamBreak;
    }
    public void setAffectedArea(double affectedArea) {
        this.affectedArea = affectedArea;
    }
    public void setWaterLevel(double waterLevel) {
        this.waterLevel = waterLevel;
    }
}
