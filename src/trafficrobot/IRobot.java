/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficrobot;

/**
 *
 * @author alizons.nematovs
 */
public interface IRobot {
    
    public void travel(double lat1, double lon1, double lat2, double lon2);
    public void log(String message, boolean important);
    public boolean checkOnShutdown(String datetime);
    
}
