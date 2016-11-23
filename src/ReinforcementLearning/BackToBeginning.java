package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public class BackToBeginning implements CrashBehavior {
    
    public void crash(Car c) {
        
        // reset the position of the car to the starting position
        c.xt = c.x0;
        c.yt = c.y0;
        // set speed of the car to 0
        c.x_speed = 0;
        c.y_speed = 0;
    }
}
