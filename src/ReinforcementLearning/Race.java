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
    private ArrayList<Integer[]> startingLine;
    
    public Race (Integer[][][] policy, Car car, Track t) {
        this.policy = policy;
        this.car = car;
        this.t = t;
        this.track = t.getTrack();
    }
    
    public void start() {
        setStart();
        setCar();
        this.finished = false;
        race();
    }
    
    private void setCar() {
        // Randomize position on starting line
        
        Random r =  new Random();
        int index = r.nextInt(startingLine.size());
        
        // set car at selected start
        car.y0 = startingLine.get(index)[0];
        car.x0 = startingLine.get(index)[1];
    }
    
    private void race() {
        // init score
        double score = 0;
        
        while (!finished) {
            // decrease score per time step
            score -= 0.1;
            // extract policy at xt, yt
            car.accelY(policy[car.yt][car.xt][0]);
            car.accelX(policy[car.yt][car.xt][1]);
            
            // update car with last accel
            car.updatePosition();
            
            // check for crash condition
            if (crashCheck()) {
                car.carCrash();
            }
        }
    }
    
    private boolean crashCheck() {
        return false;
    }
    
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
                            s2[0] = j;
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
                            s4[0] = j;
                            startingLine.add(s4);
                            count++;
                            break;
                    }
                }
            }
        }
    }
    
    
    
    private void printTrack() {
        
    }
}
