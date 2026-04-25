import java.util.ArrayList;
import java.util.List;

public class ResourcePool<T extends Supply> {
    private String poolName;
    private List<T> supplies;
    private double maxCapacity;
    private double currentCapacity;
    private static int totalPools = 0;

    public ResourcePool(String poolName, double maxCapacity) {

        if (maxCapacity <= 0) {
            System.out.println("Max capacity must be greater than 0.");
        } else {
            this.maxCapacity = maxCapacity;
        }

        this.poolName        = poolName;
        this.supplies        = new ArrayList<>();
        this.currentCapacity = 0;
        totalPools++;
    }

    // add supply to pool during restock
    public void restock(T supply, double quantity) {

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        // check if adding this quantity exceeds max capacity
        if (this.currentCapacity + quantity > this.maxCapacity) {
            System.out.println("Warning: Cannot restock " + quantity + " units. Exceeds max capacity of " + this.maxCapacity + ". Current: " + this.currentCapacity + ".");
            return;
        }

        this.supplies.add(supply);
        this.currentCapacity += quantity;
        System.out.println("Restocked " + quantity + " units of " + supply.getSupplyType() + " into " + this.poolName + ". Current stock: " + this.currentCapacity + ".");
    }

    // allocate supply to a requesting responder
    public boolean allocate(String supplyType, double quantity) {

        if (quantity <= 0) {
            System.out.println("Requested quantity must be greater than 0.");
            return false;
        }

        // check if enough stock exists
        if (this.currentCapacity < quantity) {
            System.out.println("Insufficient stock in " + this.poolName + ". Requested: " + quantity + ". Available: " + this.currentCapacity + ".");
            return false;
        }

        // check if supply type matches what this pool holds
        boolean typeFound = false;
        for (T supply : this.supplies) {
            if (supply.getSupplyType().equals(supplyType)) {
                typeFound = true;
                break;
            }
        }

        if (!typeFound) {
            System.out.println("Supply type '" + supplyType + "' not found in " + this.poolName + ".");
            return false;
        }

        this.currentCapacity -= quantity;
        System.out.println("Allocated " + quantity + " units of " + supplyType + " from " + this.poolName + ". Remaining stock: " + this.currentCapacity + ".");

        // warn if stock falls below 20 percent
        if (this.currentCapacity < this.maxCapacity * 0.2) {
            System.out.println("Critical Warning: " + this.poolName + " stock below 20%. Restock urgently.");
        }

        return true;
    }

    // check available stock without allocating
    public double getAvailable() {
        return this.currentCapacity;
    }

    // check if pool is critically low
    public boolean isCriticallyLow() {
        return this.currentCapacity < this.maxCapacity * 0.2;
    }

    // check if pool is completely empty
    public boolean isEmpty() {
        return this.currentCapacity == 0;
    }

    // get capacity as a percentage
    public double getCapacityPercentage() {
        return (this.currentCapacity / this.maxCapacity) * 100;
    }

    // generate log for ReportManager
    public String generateLog() {
        return ("ResourcePool: %s | Stock: %.1f / %.1f units | " + "Capacity: %.1f%% | Status: %s") .formatted( this.poolName, this.currentCapacity, this.maxCapacity, this.getCapacityPercentage(), this.isCriticallyLow() ? "CRITICAL" : "NORMAL" );
    }

    // static method — total pools created system wide
    public static int getTotalPools() {
        return totalPools;
    }

    // getters
    public String getPoolName()      { return poolName; }
    public double getMaxCapacity()   { return maxCapacity; }
    public List<T> getSupplies()     { return supplies; }
}