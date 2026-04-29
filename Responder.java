// parent of all rescue units 
public abstract class Responder {
    protected final int responderID;
    protected String name;
    protected boolean deploymentStatus; // true = active
    private String[] skills;
    protected static int totalDeployed = 0;
    protected String currentZone;

    Responder(int responderID, String name, int numberOfSkills) {
        this.responderID = responderID;
        this.name = name;
        this.skills = new String[numberOfSkills];
    }

    abstract void respond(DisasterEvent eve); // because a medical unit and a fire brigade respond very differently

    // Getters
    public String[] getSkills() {
        return skills;
    }

    public boolean getDeploymentStatus() {
        return this.deploymentStatus;
    }

    public int getResponderID() {
        return responderID;
    }

    public String getName() {
        return name;
    }

    public static int getTotalDeployed() {
        return totalDeployed;
    }

    public String getCurrentZone() {
        return currentZone;
    }

    // Setter
    public void setDeploymentStatus(boolean newStatus) {
        if (newStatus && !this.deploymentStatus) {
            Responder.totalDeployed++;
        } else if (!newStatus && this.deploymentStatus) {
            Responder.totalDeployed--;
        }
        this.deploymentStatus = newStatus;
    }

    public void setSkill(int index, String skill) {
        if (index >= 0 && index < skills.length) {
            skills[index] = skill;
        } else {
            System.out.println("Invalid skill index.");
        }
    }
}
