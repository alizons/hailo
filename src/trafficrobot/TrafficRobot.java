package trafficrobot;

/**
 *
 * @author aliknematov
 */
public class TrafficRobot {
    
    public static boolean verboseLog = false;
    
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
        
        if (args.length > 1) {
            if (args[1].equals("1")) {
                verboseLog = true;
            }
        }
        
        new RobotCar(5937, speed, new Dispatcher());
        new RobotCar(6043, speed, new Dispatcher());
    }
}
