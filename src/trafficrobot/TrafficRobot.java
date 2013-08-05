package trafficrobot;

/**
 *
 * @author aliknematov
 */
public class TrafficRobot {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws java.io.IOException {
        
        DataQueue dataQueue = DataQueue.getInstance();
        AnalyticsReceiver receiver = new AnalyticsReceiver();
        dataQueue.setComponent(receiver);
        
        int speed = 50;
        if (args.length > 0) {
            speed = Integer.parseInt(args[0]);
        }
        
        new RobotCar(5937, speed);
        new RobotCar(6043, speed);
    }
}
