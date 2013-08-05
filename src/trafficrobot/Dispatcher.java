package trafficrobot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;



import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;



/**
 *
 * @author aliknematov
 */
public class Dispatcher {
    
    private int robotId;
    private List data;
    private List tubeData;
    private TrafficInfo tr;
    
    public final static int TRAFFIC_STATUS_HEAVY = 1;
    public final static int TRAFFIC_STATUS_MODERATE = 2;
    public final static int TRAFFIC_STATUS_LIGHT = 3;
    
    
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

}
