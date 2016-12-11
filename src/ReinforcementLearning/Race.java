package ReinforcementLearning;

/**
 *
 * @author group22
 */
public class Race {
    private Integer[][][] policy;
    private Car car;
    private Track track;
    
    public Race (Integer[][][] policy, Car car, Track track) {
        this.policy = policy;
        this.car = car;
        this.track = track;
    }
    
    public void start() {
        
    }
}
