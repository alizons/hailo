/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficrobot;

/**
 *
 * @author aliknematov
 */
public class TrafficInfo {
    
    private int robotId;
    private String time;
    private int speed;
    private int trafficStatus;
    private String station;
    
    public int getRobotId() {
        return this.robotId;
    }
    
    public void setRobotId(int robotId) {
        this.robotId = robotId;
    }
    
    public String getTime() {
        return this.time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public int getTrafficStatus() {
        return this.trafficStatus;
    }
    
    public void setTrafficStatus(int trafficStatus) {
        this.trafficStatus = trafficStatus;
    }
    
    public String getStation() {
        return this.station;
    }
    
    public void setSattion(String station) {
        this.station = station;
    }
    
    
    public void save() {
        DataQueue dataQueue = DataQueue.getInstance();
        dataQueue.propoganateData(this);
    }
    
}
