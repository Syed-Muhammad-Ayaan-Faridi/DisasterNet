//generic-imp for marks
import java.util.ArrayList;

public class ResourcePool<T> {

    private ArrayList<T> resources = new ArrayList<>();

    public void addResource(T item) {
        resources.add(item);
    }

    public ArrayList<T> getResources() {
        return resources;
    }
}