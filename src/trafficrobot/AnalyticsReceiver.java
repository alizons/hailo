package trafficrobot;

import java.util.List;

/**
 *
 * @author aliknematov
 */
public class AnalyticsReceiver implements IReceiver {
    public void receieveMessage(String jsonData) {
        System.out.println("Json data received!" + jsonData);
    }
}
