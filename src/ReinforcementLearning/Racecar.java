package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public class Racecar extends Car {

    
    public Racecar(int x, int y) {
        super(x, y);
        this.crashBehavior = new NearestLocation();
        this.raceBehavior = new ValueIteration();
        this.MAX_SPEED = 5;
    }
}
