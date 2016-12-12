package ReinforcementLearning;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author group22
 */
public class Race {

    private boolean finished;
    private Integer[][][] policy;
    private Car car;
    private Track t;
    private char[][] track;
    private ArrayList<Integer[]> startingLine = new ArrayList<>();
    private int crashCount = 0;

    /**
     * Constructor for race requires a policy, car, and track
     * @param policy 
     * @param car
     * @param t 
     */
    public Race(Integer[][][] policy, Car car, Track t) {
        this.policy = policy;
        this.car = car;
        this.t = t;
        this.track = t.getTrack();
    }

    /**
     * Starts the race
     */
    public void start() {
        setStart();
        setCar();
        this.finished = false;
//        car.setCrashBehavior(new BackToBeginning());
        race();
    }

    /**
     * set the car at a random starting location
     */
    private void setCar() {
        // Randomize position on starting line

        Random r = new Random();
        int index = r.nextInt(startingLine.size());

        // set car at selected start
        car.y0 = startingLine.get(index)[0];
        car.yt = startingLine.get(index)[0];
        car.x0 = startingLine.get(index)[1];
        car.xt = startingLine.get(index)[1];
    }

    /**
     * Race logic
     */
    private void race() {
        // init score
        double score = 0;
        
        while (!finished) {
//            printTrack();
            // decrease score per time step
            score -= 0.1;
            // make movement fail 20% of the time
            double r = Math.random();
            // extract policy at xt, yt
            if (r < 0.8) {
                car.accelY(policy[car.yt][car.xt][0]);
                car.accelX(policy[car.yt][car.xt][1]);
            }
            // evaluate vector and update car if possible
            vectorCheck();
            if (this.finished) {
                printTrack();
                System.out.println("Score: " + score + "Crashes: " + crashCount);
            }
        }
    }

    /**
     * vectorChecker is a big ugly beast of a method. It checks each increment
     * of a vector with a directional magnitude bias and looks for 'F' and '#'.
     */
    private void vectorCheck() {
        // create temp variables for new position of the car
        int y_pos = car.yt;
        int x_pos = car.xt;
        int y_speed = car.y_speed;
        int x_speed = car.x_speed;

        // check if off the track
        if (y_pos + y_speed >= track.length || y_pos + y_speed < 0
                || x_pos + x_speed >= track[0].length || x_pos + x_speed < 0) {
            // update car position and crash
            this.car.updatePosition();
            this.car.carCrash();
            this.crashCount++;
            // check if crossed the finish line
        } else {
            // directional magnitude bias to determine crash
            // if below condition holds y cannot be zero
            if (Math.abs(car.y_speed) > Math.abs(car.x_speed)) {
                // go y first and check positions of the vector
                // check which direction to iterate
                if (y_speed > 0) {
                    // process first part of vector step
                    y_pos++;
                    y_speed--;
                    // see if this move won the race...
                    if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                            || x_pos < 0 || track[y_pos][x_pos] == '#') {
                        this.car.updatePosition();
                        this.car.carCrash();
                        this.crashCount++;
                    } else if (track[y_pos][x_pos] == 'F') {
                        this.finished = true;
                    }
                    // repeat until y direction exhausted
                    while (y_speed > 0) {
                        y_pos++;
                        y_speed--;
                        if (x_speed != 0) {
                            // pos x
                            if (x_speed > 0) {
                                x_pos++;
                                x_speed--;
                                // neg x
                            } else {
                                x_pos--;
                                x_speed++;
                            }
                        }
                        // check destination for crash, finish, etc.
                        if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                                || x_pos < 0 || track[y_pos][x_pos] == '#') {
                            this.car.updatePosition();
                            this.car.carCrash();
                            this.crashCount++;
                        } else if (track[y_pos][x_pos] == 'F') {
                            this.finished = true;
                        }
                    }
                    // otherwise y is negative
                } else {
                    // process first part of vector step
                    y_pos--;
                    y_speed++;
                    // see if this move won the race...
                    if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                            || x_pos < 0 || track[y_pos][x_pos] == '#') {
                        this.car.updatePosition();
                        this.car.carCrash();
                        this.crashCount++;
                    }
                    if (track[y_pos][x_pos] == 'F') {
                        this.finished = true;
                    }
                    // repeat until y direction exhausted
                    while (y_speed < 0) {
                        y_pos--;
                        y_speed++;
                        if (x_speed != 0) {
                            // pos x
                            if (x_speed > 0) {
                                x_pos--;
                                x_speed++;
                                // neg x
                            } else {
                                x_pos++;
                                x_speed--;
                            }
                        }
                        // check destination for crash, finish, etc.
                        if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                                || x_pos < 0 || track[y_pos][x_pos] == '#') {
                            this.car.updatePosition();
                            this.car.carCrash();
                            this.crashCount++;
                        } else if (track[y_pos][x_pos] == 'F') {
                            this.finished = true;
                        }
                    }
                }
            } else if (Math.abs(car.y_speed) < Math.abs(car.x_speed)) {
                // go x first and check positions of the vector
                if (x_speed > 0) {
                    // process first part of vector step
                    x_pos++;
                    x_speed--;
                    // see if this move won the race...a
                    if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                            || x_pos < 0 || track[y_pos][x_pos] == '#') {
                        this.car.updatePosition();
                        this.car.carCrash();
                        this.crashCount++;
                    } else if (track[y_pos][x_pos] == 'F') {
                        this.finished = true;
                    }
                    // repeat until x direction exhausted
                    while (x_speed > 0) {
                        x_pos++;
                        x_speed--;
                        if (y_speed != 0) {
                            // pos x
                            if (y_speed > 0) {
                                y_pos++;
                                y_speed--;
                                // neg x
                            } else {
                                y_pos--;
                                y_speed++;
                            }
                        }
                        // check destination for crash, finish, etc.
                        if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                                || x_pos < 0 || track[y_pos][x_pos] == '#') {
                            this.car.updatePosition();
                            this.car.carCrash();
                            this.crashCount++;
                        } else if (track[y_pos][x_pos] == 'F') {
                            this.finished = true;
                        }
                    }
                    // otherwise x is negative
                } else {
                    // process first part of vector step
                    x_pos--;
                    x_speed++;
                    // see if this move won the race...
                    if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                            || x_pos < 0 || track[y_pos][x_pos] == '#') {
                        this.car.updatePosition();
                        this.car.carCrash();
                        this.crashCount++;
                    }
                    if (track[y_pos][x_pos] == 'F') {
                        this.finished = true;
                    }
                    // repeat until y direction exhausted
                    while (x_speed < 0) {
                        x_pos--;
                        x_speed++;
                        if (y_speed != 0) {
                            // pos x
                            if (y_speed > 0) {
                                y_pos--;
                                y_speed++;
                                // neg x
                            } else {
                                y_pos++;
                                y_speed--;
                            }
                        }
                        // check destination for crash, finish, etc.
                        if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                                || x_pos < 0 || track[y_pos][x_pos] == '#') {
                            this.car.updatePosition();
                            this.car.carCrash();
                            this.crashCount++;
                        } else if (track[y_pos][x_pos] == 'F') {
                            this.finished = true;
                        }
                    }
                }
            } else {
                // abs(y) = abs(x) -- either 0 check is fine
                while (Math.abs(y_speed) > 0) {
                    // x > 0
                    if (x_speed > 0) {
                        x_pos++;
                        x_speed--;
                    } // x < 0
                    else {
                        x_pos--;
                        x_speed++;
                    }
                    // y > 0 
                    if (y_speed > 0) {
                        y_pos++;
                        y_speed--;
                    } // y < 0
                    else {
                        y_pos--;
                        y_speed++;
                    }
                    if (y_pos >= track.length || y_pos < 0 || x_pos >= track[0].length
                            || x_pos < 0 || track[y_pos][x_pos] == '#') {
                        this.car.updatePosition();
                        this.car.carCrash();
                        this.crashCount++;
                    } else if (track[y_pos][x_pos] == 'F') {
                        this.finished = true;
                    }
                }
            }
        }
        car.updatePosition();
    }

    /**
     * Find and add coordinates for the starting positions of the track to an
     * array list
     */
    private void setStart() {
        Integer[] s1 = new Integer[2];
        Integer[] s2 = new Integer[2];
        Integer[] s3 = new Integer[2];
        Integer[] s4 = new Integer[2];

        int count = 1;

        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 'S') {
                    switch (count) {
                        case 1:
                            s1[0] = i;
                            s1[1] = j;
                            startingLine.add(s1);
                            count++;
                            break;
                        case 2:
                            s2[0] = i;
                            s2[1] = j;
                            startingLine.add(s2);
                            count++;
                            break;
                        case 3:
                            s3[0] = i;
                            s3[1] = j;
                            startingLine.add(s3);
                            count++;
                            break;
                        case 4:
                            s4[0] = i;
                            s4[1] = j;
                            startingLine.add(s4);
                            count++;
                            break;
                    }
                }
            }
        }
    }
    
    /**
     * print the track with the car position on it to show position at time t
     */
    private void printTrack() {
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (car.yt == i && car.xt == j) {
                    System.out.print('c');
                } else {
                    System.out.print(track[i][j]);
                }
            }
            System.out.println();
        }
    }
}
