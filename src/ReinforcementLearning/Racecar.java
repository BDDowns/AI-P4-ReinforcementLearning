package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public class Racecar extends Car {

    /**
     * This Racecar constructor initializes a Racecar to the base configuration 
     * of the assignment statement with a crash behavior that sets the car at 
     * the nearest open track location and race behavior of value iteration.
     * 
     * The car initial speed in both the x and y coordinate directions is 0.
     * 
     * The maximum speed of a Racecar is +/- 5.
     * 
     * @param x starting coordinate on the x-axis at initialization
     * @param y starting coordinate on the y-axis at initialization
     */
    public Racecar(int x, int y) {
        super(x, y);
        this.crashBehavior = new NearestLocation();
        this.raceBehavior = new ValueIteration();
        this.x_speed = 0;
        this.y_speed = 0;
        this.MAX_SPEED = 5;
    }
}
