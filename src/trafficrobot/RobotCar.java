package trafficrobot;

import java.util.List;
import java.util.Set;
import java.util.Random;
import java.util.HashSet;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;


/**
 *
 * @author aliknematov
 */
public class RobotCar implements Runnable{
   
   private int startIndex = 0;
   private int endIndex = 9;
   private int robotId = 0;
   private boolean shutdown = false;
   private Dispatcher dispatcher;
   private int shutdownTimeHour = 0;
   private int shutdownTimeMinutes = 0;
   private Calendar cal;
   private int speed;
   
   
   
   //private final static int CONSTANT_ROBOT_SPEED = 100; //20 km/h
   Thread t;
   
   /**
    * Conctructor
    * @param robotId int
    */
   public RobotCar(int robotId, int speed, Dispatcher dispatcher) {
       //this.visitedTubes = new HashSet<String>();
       this.robotId = robotId;
       this.speed = speed;
       this.dispatcher = dispatcher;
       t = new Thread(this, Integer.toString(robotId));
       t.start();
   }
   
   @Override
   public void run() {
        dispatcher.setRobotId(this.robotId);
        dispatcher.initRobotProcess();
        cal = Calendar.getInstance();
        this.shutdownTimeHour = dispatcher.getShutDownHour();
        this.shutdownTimeMinutes = dispatcher.shutDownMinutes();
        
        /*
         * Main loop for processing robot movements
         */
        outerloop:
        while (shutdown != true) {
            try {
                /*
                 * getting data portion from dispatcher
                 */
                List<List<String>> dt = dispatcher.getData(this.startIndex, this.endIndex);
                int index = 0;
                /*
                 * iterating through data received from dispatcher
                 */ 
                for (List<String> l : dt) {
                    /*
                     * trying to get next element of list to calculate the time for travveling from point A to point B
                     * If IndexOutOfBoundsException thrown - breaking the loop and trying to receive new data from dispatcher
                     */
                    try {
                        index = index + 1;
                        List<String> next = dt.get(index);
                        this.travel(Double.parseDouble(l.get(1)), Double.parseDouble(l.get(2)), Double.parseDouble(next.get(1)), Double.parseDouble(next.get(2)));
                    }
                    catch (IndexOutOfBoundsException e) {
                        log("Do not have any travel points anymore, requesting next data", false);
                        break;
                    }
                    /*
                     * check if the time is in range of a shutdown time
                     * If so - exiting main loop and terminating robot
                     */
                    if (this.checkOnShutdown(l.get(3))) {
                        log("Current time is " + l.get(3) + " - shutting down...", false);
                        break outerloop;
                    }
                    
                    /*
                     * if in range of tube - report on traffic situation
                     */
                    
                    String station = dispatcher.isInTubeDistanceRange(Double.parseDouble(l.get(1)), Double.parseDouble(l.get(2)), l.get(3));
                    if (!station.isEmpty()) {
                        Random r = new Random();
                        int rn = r.nextInt(3-1) + 1;
                        dispatcher.reportOnTraffic(robotId, this.speed, rn, l.get(3), station);
                        log("-------------------------------------------------------------------------------------", true);
                        log("In a range of " + station + " station, traffic status is " + rn + ", time is " + l.get(3), true);
                        log("-------------------------------------------------------------------------------------", true);
                    }
                }
                this.startIndex = this.startIndex + 10;
                this.endIndex = this.endIndex + 10;

            }
            catch (IndexOutOfBoundsException e) {
                break outerloop;
            }
        }
   }
   
   
   
   /**
    * This method will take care about the travel time between point A and B
    * @param lat1 double
    * @param lon1 double
    * @param lat2 double
    * @param lon2 double
    */
   private void travel(double lat1, double lon1, double lat2, double lon2) {
        double travelTime = dispatcher.getTravelTime(lat1, lon1, lat2, lon2, this.speed);
        int timeToSleep = (int)(travelTime * 60 * 1000);
        log("Travelling from " + Double.toString(lat1) + " " + Double.toString(lon1) + " to " +  Double.toString(lat2) + " " + Double.toString(lon2), false);
        log("Will take about " + timeToSleep / 1000 + "seconds with a speed " + this.speed + "km/h", false);
        try{
            Thread.currentThread().sleep(timeToSleep);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
   }
   
   
   /**
    * This method will send a log message from robot to dispatcher
    * @param message String
    */
   private void log(String message, boolean important) {
       if (TrafficRobot.verboseLog == true) {
           dispatcher.message(this.robotId, message);
       }
       else {
           if (important == true) {
               dispatcher.message(this.robotId, message);
           }
       }
    }
   
   
    /**
     * This method 
     * @param datetime
     * @return 
     */
    public boolean checkOnShutdown(String datetime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            Date date = dateFormat.parse(datetime);
            cal.setTime(date);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            if (hour >=shutdownTimeHour && minutes >=shutdownTimeMinutes) {
                log(datetime + " Time to sleep, shutting down....", true);
                return true;
            }
        }
        catch (ParseException e) {
            return true;
        }
        return false;
    }
}
