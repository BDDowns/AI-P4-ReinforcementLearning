package ReinforcementLearning;

import java.util.ArrayList;

/**
 *
 * @author group22
 */
public class Race {
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
        setStart();
    }
    
    public void start() {
        setCar();
        race();
    }
    
    private void setCar() {
        
    }
    
    private void race() {
        
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
