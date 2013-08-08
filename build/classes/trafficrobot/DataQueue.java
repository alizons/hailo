package trafficrobot;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.JSONObject;
/**
 *
 * @author aliknematov
 */
public class DataQueue {
    
    private static DataQueue instance = null;
    private List<List<String>>storedData;
    private Map<String, List<String>> dt = null; 
    private List<IReceiver> components = new ArrayList<IReceiver>();
    
    
    protected DataQueue() {
        
    }
    
    public static DataQueue getInstance() {
        if(instance == null) {
            instance = new DataQueue();
        }
        return instance;
    }
    
    public void setComponent(IReceiver component) {
        this.components.add(component);
    }
    
    /**
     * Method for stopropoganating data from robots
     * @param data TrafficInfo
     */
    public void propoganateData(TrafficInfo data) {
        System.out.println(data.getRobotId());
        JSONObject json = new JSONObject();
        json.put("robot_id", data.getRobotId());
        json.put("speed", data.getSpeed());
        json.put("time", data.getTime());
        json.put("traffic_status", data.getTrafficStatus());
        json.put("station", data.getStation());

        for (IReceiver component : this.components) {
            component.receieveMessage(json.toString());
        }
    }
}
