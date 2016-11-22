package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public abstract class Car {

    // maximum speed of the car on the track
    protected int MAX_SPEED;

    // instance variables for the car
    // car normally wouldn't know where it is, but this one has GPS
    // int position for x, y at time t
    protected int xt, yt;
    // int speed in direction x, y at time t
    protected int x_speed, y_speed;

    // cars have behaviors for crashing and for racing, declare them here
    protected RaceBehavior raceBehavior;
    protected CrashBehavior crashBehavior;

    /**
     * The car constructor takes in the initial position of the car as arguments
     * and returns a car object with said position variables.
     *
     * @param x x-coordinate of the car at time t = 0
     * @param y y-coordinate of the car at time t = 0
     */
    public Car(int x, int y) {
        this.xt = x;
        this.yt = y;
    }
    /*
        Get/Set Methods
     */
    /**
     * Gets the integer value of x at time of call
     *
     * @return int x at time t
     */
    public int getX() {
        return this.xt;
    }

    /**
     * Gets the integer value of y at time of call
     *
     * @return int y at time t
     */
    public int getY() {
        return this.yt;
    }

    /**
     * update position is designed to be called at each time step to give a
     * current position of the car
     *
     * should this be part of the car or the simulation?
     */
    public void updatePosition() {
        this.xt += x_speed;
        this.yt += y_speed;
    }

    /**
     * Returns the integer value for the cars current velocity relative to the
     * x-axis
     *
     * @return
     */
    public int getXSpeed() {
        return x_speed;
    }

    /**
     * Returns the integer value for the cars current velocity relative to the
     * y-axis
     *
     * @return
     */
    public int getYSpeed() {
        return y_speed;
    }

    /* 
        My interpretation of the problem statement is that acceleration can add
        an element of {1,0,-1} to the current speed. The boolean is just incase
        that comes in handy.
     */
    /**
     * the accelX method takes an integer argument and increases the current
     * speed of the specified instance of car by that number up to the maximum
     * speed of the car
     *
     * @param n integer value to accelerate along the x-axis
     * @return true if speed change is possible, false if MAX_SPEED reached
     */
    public boolean accelX(int n) {
        if (Math.abs(x_speed) <= MAX_SPEED) {
            x_speed += n;
            return true;
        } else {
            return false;
        }
    }

    /**
     * the accelY method takes an integer argument and increases the current
     * speed of the specified instance of car by that number up to the maximum
     * speed of the car
     *
     * @param n integer value to accelerate along the y-axis
     * @return true if speed change is possible, false if MAX_SPEED reached
     */
    public boolean accelY(int n) {
        if (Math.abs(y_speed) <= MAX_SPEED) {
            y_speed += n;
            return true;
        } else {
            return false;
        }
    }

    /*
        Behaviors Section
     */
    public boolean carRace() {
        this.raceBehavior.race();
        return true;
    }

    public boolean carCrash() {
        this.crashBehavior.crash();
        return true;
    }

    public boolean setCrashBehavior(CrashBehavior cb) {
        this.crashBehavior = cb;
        return true;
    }

    public boolean setRaceBehavior(RaceBehavior rb) {
        this.raceBehavior = rb;
        return true;
    }
}
