package ReinforcementLearning;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *  
 * @author Group 22
 */
public class RacingSimulator {
    
    public static void main (String[] args) throws FileNotFoundException, IOException {
        
        // nothing fancy yet, just make some tracks and try one at a time
        Track l_track = new Track("L-track.txt");
        Track o_track = new Track("O-track.txt");
        Track r_track = new Track("R-track.txt");
        
        /*
            To start a new race:
                1. Instantiate a new car subclass (ex. Racecar)
                2. Create a track from a file
                3. Run <car>.carRace(<track>)
        */
        // need to decide on starting location and how to handle
        Car racecar = new Racecar(0,0);
        racecar.carRace(l_track);
        racecar.carRace(o_track);
        racecar.carRace(r_track);
        
        /*
            To switch Race type
                1. Set <car>.setRaceBehavior(<RaceBehavior>)
                2. Run <car>.carRace(<track>)
        */
//        racecar.setRaceBehavior(new QLearning());
//        racecar.carRace(l_track);
//        racecar.carRace(o_track);
//        racecar.carRace(r_track);
        /*
            To change Crash type
                1. Set <car>.setCrashBehavior(<CrashBehavior>)
                2. Run <car>.carRace(<track>)
        */
        racecar.setCrashBehavior(new BackToBeginning());
        racecar.carRace(r_track);
        
        /*
            One more time through to test BacktoBeginning
        */
//        racecar.setRaceBehavior(new ValueIteration());
//        racecar.carRace(r_track);
    }
}
