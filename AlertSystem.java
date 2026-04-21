import java.util.ArrayList;

public class AlertSystem {

    static class AlertLog {
        String message;

        AlertLog(String message) {
            this.message = message;
        }

        public String toString() {
            return message;
        }
    }

    private static ArrayList<AlertLog> logs = new ArrayList<>();

    public void broadcast(String msg) {
        logs.add(new AlertLog(msg));
        System.out.println(msg);
    }

    public void broadcast(String msg, String location) {
        broadcast(msg + " - " + location);
    }

    public static ArrayList<AlertLog> getLogs() {
        return logs;
    }
}