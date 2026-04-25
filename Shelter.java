public class Shelter extends Supply {

    private int capacityPerUnit;

    public Shelter(String supplyName, double quantity, double unitWeight, String expiryDate, int capacityPerUnit) {
        super(supplyName, quantity, unitWeight, expiryDate);

        if (capacityPerUnit <= 0) {
            System.out.println("Capacity per unit must be greater than 0.");
        } else {
            this.capacityPerUnit = capacityPerUnit;
        }
    }

    @Override
    public String getSupplyType() {
        return "Shelter";
    }

    public int getCapacityPerUnit() { 
        return capacityPerUnit; 
    }
}