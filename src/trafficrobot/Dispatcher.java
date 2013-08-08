package trafficrobot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;



import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;



/**
 *
 * @author aliknematov
 */
public class Dispatcher {
    
    private int robotId;
    private List data;
    private List<List<String>> tubeData = null;
    private TrafficInfo tr;
    
    public final static int TRAFFIC_STATUS_HEAVY = 1;
    public final static int TRAFFIC_STATUS_MODERATE = 2;
    public final static int TRAFFIC_STATUS_LIGHT = 3;
    private Set<String> visitedTubes = null; 
    
    public final static int shutDownHour = 8;
    public final static int shutDownMinutes = 10;
    
    public void setRobotId(int id){
        this.robotId = id;
    };
    
    /**
     * This method will be a starting point for every robot.
     * We will receieve the data from CSV file depending on robot id and save it locally
     * @return void
     */
    public void initRobotProcess() {
        try {
            this.data = this.parseCsv(Integer.toString(robotId) + ".csv");
            this.tubeData = this.parseCsv("tube.csv");
            this.tr = new TrafficInfo();
            this.visitedTubes = new HashSet<String>();
        }
        catch (IOException e) {
            System.out.print(e);
        }
    }
    
    /**
     * This method will return the data for every robot movement.
     * Robot should decide which data he needs to receive depending on his current index/position
     * 
     * @param startIndex int
     * @param endIndex int
     * @return  List
     */
    public List getData(int startIndex, int endIndex) {
        return data.subList(startIndex, endIndex);
    }
    
    /**
     * This method will return the data for tube locations
     * @return List
     */
    public List getTubeData() {
        return tubeData;
    }
    /**
     * Robot messages logger
     * @param robotId int
     * @param message string
     */
    public void message(int robotId, String message) {
        System.out.println(Integer.toString(robotId) + ":" + message);
    }
    
    
    public int getShutDownHour() {
        return shutDownHour;
    }
    
    public int shutDownMinutes() {
        return shutDownMinutes;
    }
    
    public void reportOnTraffic(int robotId, int speed, int trafficStatus,String time, String station) {
        this.tr.setRobotId(robotId);
        this.tr.setSpeed(speed);
        this.tr.setTime(time);
        this.tr.setSattion(station);
        this.tr.setTrafficStatus(trafficStatus);
        this.tr.save();
    }
    
    
    /**
    * This method will parse csv files and return the data as a list
    * @param fileName string
    */
    public List parseCsv(String fileName) throws IOException {
        CsvListReader csvReader = new CsvListReader(new InputStreamReader(
        Dispatcher.class.getClassLoader().getResourceAsStream("trafficrobot/" + fileName)),
        CsvPreference.STANDARD_PREFERENCE);
        List<String> rowAsTokens;
        List<List<String>> res = new ArrayList();
        while ((rowAsTokens = csvReader.read()) != null) {
            List<String> l = new ArrayList();
            //res.addAll(rowAsTokens);
            for (String token : rowAsTokens) {
                l.add(token);
            }
            res.add(l);
        }
        return res;
    }
    
    /**
    * This method will calculate travel time in minutes based on robot speed
    * @param lat1 double
    * @param lon1 double
    * @param lat2 double
    * @param lon2 double
    * @return double 
    */
   public double getTravelTime(double lat1, double lon1, double lat2, double lon2, int speed) {
       double distance = this.distance(lat1, lon1, lat2, lon2, "K");
       return distance * 60 / speed;
   }
   
   /**
    * This method will check if robot position is wihin 350 m from tube station
    * @param robotLat double
    * @param robotLon double
    * @param time string
    * @return boolean
    */
   public String isInTubeDistanceRange(double robotLat, double robotLon, String time) {
       double tubeLat = 0;
       double tubeLon = 0;
       double distance = 0;
       for (List<String> l : this.tubeData) {
            tubeLat =  Double.parseDouble(l.get(1));
            tubeLon =  Double.parseDouble(l.get(2));
            distance = this.distance(robotLat, robotLon, tubeLat, tubeLon, "K");
            if (distance <= 0.350 && !visitedTubes.contains(l.get(0))) {
                visitedTubes.add(l.get(0));
                return l.get(0);
            }
       }
       return "";
   }
   
   /**
    * This method will calculate a distance between 2 points
    * @param lat1 double
    * @param lon1 double
    * @param lat2 double
    * @param lon2 double
    * @param unit string
    * @return double
    */
   private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      if (unit == "K") {
        dist = dist * 1.609344;
      } else if (unit == "N") {
        dist = dist * 0.8684;
      }
      return (dist);
    }

    /**
     * This method converts decimal degrees to radians
     * @param deg double
     * @return double
     */
    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }

    /**
     * This method converts radians to decimal degrees
     * @param rad double
     * @return double
     */
    private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
     
    
     
     

}
