public class MedicalUnit extends Responder implements Deployable, Trackable, Reportable {

    private int doctorCount;
    private int nurseCount;
    private int bedCapacity;
    private boolean bloodBankAvailable;
    private boolean isMobile;
    private int medicineStock;

    public MedicalUnit(int responderID, String name, int numberOfSkills, int doctorCount, int nurseCount, int bedCapacity, boolean bloodBankAvailable, boolean isMobile, int medicineStock) {

        super(responderID, name, numberOfSkills);

        if (doctorCount < 0 || nurseCount < 0 || bedCapacity < 0) {
            System.out.println("Medical staff and bed counts cannot be negative.");
        } else {
            this.doctorCount  = doctorCount;
            this.nurseCount   = nurseCount;
            this.bedCapacity  = bedCapacity;
        }

        if (medicineStock < 0) {
            System.out.println("Medicine stock cannot be negative.");
        } else {
            this.medicineStock = medicineStock;
        }

        this.bloodBankAvailable = bloodBankAvailable;
        this.isMobile           = isMobile;
        this.currentZone        = "NONE";
    }

    @Override
    public void respond(DisasterEvent event) {

        if (this.deploymentStatus) {
            System.out.println(this.name + " is already deployed at " + this.currentZone + ". Cannot redeploy.");
            return;
        }

        // fixed hospitals cannot enter disaster zones
        if (!this.isMobile) {
            System.out.println(this.name + " is a fixed facility. " + "Receiving patients at base location instead" + " of entering zone.");
            this.setDeploymentStatus(true);
            this.currentZone = "BASE — receiving from " + event.getLocation();
            return;
        }

        // check if medicine stock is sufficient before deploying
        if (this.medicineStock < 20) {
            System.out.println("Warning: " + this.name +  " has low medicine stock (" +  this.medicineStock + " units). " + "Requesting restock before deployment.");
        }

        // calculate treatment capacity
        // each doctor handles roughly 10 patients per shift with nurse support
        int treatmentCapacity = this.doctorCount * 10 + this.nurseCount * 3;
        System.out.println(this.name + " responding to " + event.getLocation() + ". Treatment capacity: " + treatmentCapacity + " patients. Beds: " + this.bedCapacity + ".");

        if (this.bloodBankAvailable) {
            System.out.println("Blood bank available — trauma surgery capable.");
        }

        this.setDeploymentStatus(true);
        this.currentZone = event.getLocation();
    }

    // consume medicine during response
    public void consumeMedicine(int amount) {
        if (amount > this.medicineStock) {
            System.out.println("Insufficient medicine stock. " + "Only " + this.medicineStock + " units left.");
            this.medicineStock = 0;
        } else {
            this.medicineStock -= amount;
        }
    }

    // restock from ResourcePool
    public void restockMedicine(int amount) {
        this.medicineStock += amount;
        System.out.println(this.name + " restocked. Medicine stock: " + this.medicineStock + " units.");
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
        System.out.println(this.name + " recalled to base.");
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
        return ("MedicalUnit ID: %d | Name: %s | Doctors: %d | Nurses: %d" + " | Beds: %d | Blood Bank: %s | Mobile: %s | " + "Medicine Stock: %d | Zone: %s | Status: %s") .formatted( this.responderID, this.name, this.doctorCount, this.nurseCount, this.bedCapacity, this.bloodBankAvailable ? "Yes" : "No", this.isMobile ? "Yes" : "No", this.medicineStock, this.currentZone, this.getStatus() );
    }

    // getters
    public int getDoctorCount() { 
        return this.doctorCount; 
    }
    public int getNurseCount() { 
        return this.nurseCount; 
    }
    public int getBedCapacity()   {
        return this.bedCapacity; 
    }
    public boolean isBloodBankAvailable() {
        return this.bloodBankAvailable; 
    }
    public boolean isMobile()     { 
        return this.isMobile; 
    }
    public int getMedicineStock() { 
        return this.medicineStock; 
    }
    public String getCurrentZone() { 
        return this.currentZone; 
    }
}
