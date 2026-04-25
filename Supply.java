public abstract class Supply {

    protected String supplyName;
    protected double quantity;
    protected double unitWeight;
    protected String expiryDate;

    protected Supply(String supplyName, double quantity, double unitWeight, String expiryDate) {
        this.supplyName  = supplyName;
        this.quantity    = quantity;
        this.unitWeight  = unitWeight;
        this.expiryDate  = expiryDate;
    }

    public abstract String getSupplyType();

    // getters
    public String getSupplyName() {
        return supplyName; 
    }
    public double getQuantity()   { 
        return quantity; 
    }
    public double getUnitWeight() { 
        return unitWeight; 
    }
    public String getExpiryDate() { 
        return expiryDate; 
    }
}