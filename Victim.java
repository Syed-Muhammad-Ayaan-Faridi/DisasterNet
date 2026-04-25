public class Victim {

    private String victimID;
    private String name;
    private int age;
    private String disasterLocation;
    private Victim.Status status;

    // reference to this victim's medical record
    // created lazily — only when medical data is added
    private MedicalRecord medicalRecord;

    public Victim(String victimID, String name, int age, String disasterLocation) {

        if (age < 0) {
            System.out.println("Age cannot be negative.");
        } else {
            this.age = age;
        }

        this.victimID         = victimID;
        this.name             = name;
        this.disasterLocation = disasterLocation;
        this.status           = Victim.Status.MISSING; // default on registration
        this.medicalRecord    = null; // no medical data yet
    }

    // ─────────────────────────────────────────
    // STATIC NESTED ENUM
    // static — does not need a Victim instance
    // Victim.Status.RESCUED works without any object
    // ─────────────────────────────────────────
    public enum Status {
        MISSING,
        RESCUED,
        CRITICAL,
        DECEASED,
        EVACUATED
    }

    // NON-STATIC INNER CLASS
    // non-static — belongs to one specific Victim
    // can access victimID and name directly
    // without any getter because it is inside Victim
    public class MedicalRecord {

        private String bloodType;
        private String[] injuries;
        private String treatmentLog;
        private boolean requiresHospital;
        private String assignedMedicalUnit;

        public MedicalRecord(String bloodType, String[] injuries, boolean requiresHospital) {

            this.bloodType          = bloodType;
            this.injuries           = injuries;
            this.requiresHospital   = requiresHospital;
            this.treatmentLog       = "Record created for: " + name;
            this.assignedMedicalUnit = "NONE";
        }

        public void addTreatmentEntry(String entry) {
            this.treatmentLog += " | " + entry;
        }

        // assign a medical unit to this victim
        public void assignUnit(String unitName) {
            this.assignedMedicalUnit = unitName;
            this.addTreatmentEntry("Assigned to: " + unitName);
        }

        // generate medical summary
        public String getMedicalSummary() {
            StringBuilder injuryList = new StringBuilder();
            for (String injury : injuries) {
                injuryList.append(injury).append(", ");
            }
            return ("Victim: %s (ID: %s) | Blood Type: %s | " + "Injuries: %s | Hospital Required: %s | " + "Assigned Unit: %s | Log: %s") .formatted(name, victimID, this.bloodType, injuryList.toString(),     this.requiresHospital ? "Yes" : "No",this.assignedMedicalUnit, this.treatmentLog);
        }

        // getters
        public String getBloodType()          { return bloodType; }
        public String[] getInjuries()         { return injuries; }
        public String getTreatmentLog()       { return treatmentLog; }
        public boolean isRequiresHospital()   { return requiresHospital; }
        public String getAssignedMedicalUnit(){ return assignedMedicalUnit; }
    }

    // create medical record for this victim
    // returns the created record so caller can populate it
    public MedicalRecord createMedicalRecord(String bloodType, String[] injuries, boolean requiresHospital) {
        this.medicalRecord = new MedicalRecord(bloodType, injuries, requiresHospital);
        return this.medicalRecord;
    }

    // get existing medical record
    public MedicalRecord getMedicalRecord() {
        if (this.medicalRecord == null) {
            System.out.println("No medical record exists for " + this.name + " yet.");
        }
        return this.medicalRecord;
    }

    // update victim status
    public void updateStatus(Victim.Status newStatus) {
        System.out.println("Victim " + this.name + " status updated: " + this.status + " → " + newStatus);
        this.status = newStatus;
    }

    // generate a one line log entry for ReportManager
    public String generateLog() {
        return ("Victim ID: %s | Name: %s | Age: %d | " + "Location: %s | Status: %s | Medical Record: %s") .formatted( this.victimID, this.name, this.age, this.disasterLocation, this.status, this.medicalRecord != null ? "Yes" : "No" );
    }

    // getters
    public String getVictimID()         { return victimID; }
    public String getName()             { return name; }
    public int getAge()                 { return age; }
    public String getDisasterLocation() { return disasterLocation; }
    public Victim.Status getStatus()    { return status; }
}