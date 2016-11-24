package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public class NearestLocation implements CrashBehavior {
 
    public void crash(Car c) {
        
        // search for nearest point of clear track
        // assuming car is still at the crash position
        
        // < code to go here if above met >
        
        // if we don't have to search exactly as described in the problem 
        // statement we could just subract the last known velocity vector
        // and set speed to 0 should put us back on the track
        c.xt -= c.x_speed;
        c.yt -= c.y_speed;
        c.x_speed = 0;
        c.y_speed = 0;
        
    }
}
