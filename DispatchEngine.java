import java.util.ArrayList;

public class DispatchEngine {

    public void dispatch(DisasterEvent disaster, Responder responder) {
        System.out.println("Dispatching " + responder.name +
                " to " + disaster.getEventID());

        responder.deployStatus = true;
        Responder.totalDeployed++;

        responder.respond(disaster);
    }

    public void dispatch(DisasterEvent disaster, ArrayList<Responder> responders) {
        for (Responder r : responders) {
            dispatch(disaster, r);
        }
    }

    public static void prioritize(ArrayList<DisasterEvent> disasters) {
        disasters.sort((a, b) -> b.getSeveritylevel() - a.getSeveritylevel());
    }
}