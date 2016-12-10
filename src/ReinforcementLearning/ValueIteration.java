package ReinforcementLearning;

/**
 *
 * @author Group 22
 */
public class ValueIteration implements RaceBehavior {
    
    //Track tk;
    static char[][] track;
    static double[][] value;
    Track t;
    boolean close;
    
    @Override
    public void race(Car c, Track t) {
        this.t = t;
       track = t.getTrack();
       value = new double[track.length][track[0].length];
       
       place_Values();
       do_Calc(value);
    }
    
    public double[][] place_Values(){
        for(int i = 0; i < track.length; i ++){
            System.out.println();
            for(int j = 0; j < track[0].length; j ++){
                if(track[i][j] == 'F'){
                    value[i][j] = 100;
                }
                if(track[i][j] == '#'){
                    value[i][j] = -1;
                }
                if(track[i][j] == '.'){
                    value[i][j] = 0;
                }
                if(track[i][j] == 'S'){
                    value[i][j] = 0;
                }
                System.out.print(value[i][j] + " ");
            }
            
       }
        return value;
    }
    //view page 652 in AI book for formula
    public void do_Calc(double [][] value){
       while(close){ 
        
        for(int i = 0; i < value.length; i ++){
            for(int j = 0; j < value[0].length; j ++){
                if(i != 0){
                  double l = value[i-1][j];
                }
                if(i != value.length){
                    double r = value[i+1][j];
                }
                if( j != 0){
                    double d = value[i][j-1];
                }
                
                if(j != value[0].length){
                double u = value[i][j+1];
                }
                if(value[i][j] >= 0){
                    
                }
            }
        }
       }
    }
    
}
