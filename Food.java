public class Food extends Supply {

    private int caloriesPerUnit;

    public Food(String supplyName, double quantity, double unitWeight, String expiryDate, int caloriesPerUnit) {
        super(supplyName, quantity, unitWeight, expiryDate);
        if (caloriesPerUnit <= 0) {
            System.out.println("Calories per unit must be greater than 0.");
        } else {
            this.caloriesPerUnit = caloriesPerUnit;
        }
    }

    @Override
    public String getSupplyType() {
        return "Food";
    }

    public int getCaloriesPerUnit() {
        return caloriesPerUnit;
    }
}