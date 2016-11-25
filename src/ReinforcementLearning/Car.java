package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public abstract class Car {

    // maximum speed of the car on the track
    protected int MAX_SPEED;

    // instance variables for the car
    
    // record starting position to be used for restart crash
    protected int x0, y0;
    // car normally wouldn't know where it is, but this one has GPS
    // int position for x, y at time t
    protected int xt, yt;
    // int speed in direction x, y at time t
    protected int x_speed, y_speed;

    // cars have behaviors for crashing and for racing, declare them here
    protected RaceBehavior raceBehavior;
    protected CrashBehavior crashBehavior;
    
    // track reference for crash search
    protected Track track;

    /**
     * The car constructor takes in the initializes a car object with 
     * an initial position set to given parameters
     *
     * @param x x-coordinate of the car at time t = 0
     * @param y y-coordinate of the car at time t = 0
     */
    public Car(int x, int y) {
        this.xt = x;
        this.yt = y;
        this.x0 = x;
        this.y0 = y;
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
    
    /**
     * carRace method starts a race with the current instance of the car and a 
     * track object to be passed as an argument
     * 
     * @param t a track object containing a track
     * @return true if the race completed
     */
    public boolean carRace(Track t) {
        this.track = t;
        this.raceBehavior.race(this, t);
        return true;
    }

    /**
     * carCrash crashes the instance of car called
     * @return true if car crash succeeded (sounds weird, eh?)
     */
    public boolean carCrash() {
        this.crashBehavior.crash(this);
        return true;
    }

    /**
     * setCrashBehavior allows for easy switching of the crash method by 
     * assigning a crash behavior for a given car instance or model by extending
     * car and creating new car subclasses. This allows for further
     * extension with different crash types and dynamic races.
     * 
     * @param cb CrashBehavior to be assigned to specified instance of Car
     * @return true for crash behavior applied
     */
    public boolean setCrashBehavior(CrashBehavior cb) {
        this.crashBehavior = cb;
        return true;
    }

    /**
     * setRaceBehavior allows for easy switching of race behavior for a specific
     * instance of a Car subclass. Makes testing the rest of the algorithm easier.
     * 
     * @param rb RaceBehavior to be assigned to the specified instance of a subclass of car
     * @return true for success
     */
    public boolean setRaceBehavior(RaceBehavior rb) {
        this.raceBehavior = rb;
        return true;
    }
}
