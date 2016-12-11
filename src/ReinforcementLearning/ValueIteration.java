package ReinforcementLearning;

import java.util.ArrayList;

/**
 *
 * @author Group 22
 */
public class ValueIteration implements RaceBehavior {

    //Track tk;
    static char[][] track;
    double[][] value;
    double[][] value_1;
    double[][] reward;
    Integer[][][] policy;
    Track t;
    Car c;
    boolean close;
    ArrayList<Integer[]> actions = new ArrayList<>();

    @Override
    public void race(Car c, Track t) {
        this.t = t;
        track = t.getTrack();
        value = new double[track.length][track[0].length];
        value_1 = new double[track.length][track[0].length];
        reward = new double[track.length][track[0].length];
        policy = new Integer[track.length][track[0].length][2];
        this.c = c;
        place_Values();
        setNearFinish();
        setActions();
        do_Calc();
    }

    public double[][] place_Values() {
        for (int i = 0; i < track.length; i++) {
            System.out.println();
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 'F') {
                    value[i][j] = 1;
                    reward[i][j] = 5;
                }
                if (track[i][j] == '#') {
                    value[i][j] = -1;
                    reward[i][j] = -1;
                }
                if (track[i][j] == '.') {
                    value[i][j] = 0;
                    reward[i][j] = -0.1;
                }
                if (track[i][j] == 'S') {
                    value[i][j] = 0;
                }
//                System.out.print(value[i][j] + " ");
            }

        }
        return value;
    }

    public void setNearFinish() {
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 'F') {
                    if (track[i - 1][j] == '.') {
                        c.y0 = i - 1;
                        c.x0 = j;
                    } else if (track[i + 1][j] == '.') {
                        c.y0 = i + 1;
                        c.x0 = j;
                    } else if (track[i][j - 1] == '.') {
                        c.y0 = i;
                        c.x0 = j - 1;
                    } else {
                        c.y0 = i;
                        c.x0 = j + 1;
                    }
                }
            }
        }
    }

    // action list
    public void setActions() {
        // north goes up...
        Integer[] n = new Integer[2];
        n[0] = 1;
        n[1] = 0;
        this.actions.add(n);

        Integer[] nw = new Integer[2];
        nw[0] = 1;
        nw[1] = -1;
        this.actions.add(nw);

        Integer[] ne = new Integer[2];
        ne[0] = 1;
        ne[1] = 1;
        this.actions.add(ne);

        // south
        Integer[] s = new Integer[2];
        s[0] = -1;
        s[1] = 0;
        this.actions.add(s);

        Integer[] sw = new Integer[2];
        sw[0] = -1;
        sw[1] = -1;
        this.actions.add(sw);

        Integer[] se = new Integer[2];
        se[0] = -1;
        se[1] = 1;
        this.actions.add(se);

        Integer[] w = new Integer[2];
        w[0] = 0;
        w[1] = -1;
        this.actions.add(w);

        Integer[] e = new Integer[2];
        e[0] = 0;
        e[1] = 1;
        this.actions.add(e);

        Integer[] none = new Integer[2];
        none[0] = 0;
        none[1] = 0;
        this.actions.add(none);
    }

    //view page 652 in AI book for formula
    public void do_Calc() {
        double delta;
        cloneArray(this.value, value_1);
        double epsilon = 0.001;
        double discount = 1;

        do {
            cloneArray(value_1, this.value);
            delta = 0;
            for (int i = 0; i < track.length; i++) {
                for (int j = 0; j < track[0].length; j++) {
                    if (track[i][j] == '.') {
                        // compute utility based on utility of neighbors     
                        value_1[i][j] = reward[i][j] + discount * getMaxActionUtility(i, j);
                        if (Math.abs(value_1[i][j] - value[i][j]) > delta) {
                            delta = Math.abs(value_1[i][j] - value[i][j]);
                        }
                    }
                }
            }
//            printValues();
        } while (delta > (epsilon * (1 - discount) / discount));
        
        // print final array
        printValues();
        printMoves();
    }

    /**
     * getMaxActionUtility takes the cars current position and evaluates the 
     * reward with the most utility given position and velocity. The best action
     * for i, j is also updated in the actions array.
     * 
     * @param i y value for the cars position
     * @param j x value for the cars position
     * @return double value of the most utility 
     */
    public double getMaxActionUtility(int i, int j) {
        Integer[] move;
        // because java can't follow my logic of bestMove being initialized before
        // the else below...
        Integer[] bestMove = actions.get(0);
        boolean hasMove = false;
        double max = 0;
        for (int k = 0; k < actions.size(); k++) {
            // since velocity is taken into account we will gate access to areas
            // off the track
            move = actions.get(k);
            // evaluate payoff of action if an inbounds option exists
            if (c.y_speed + move[0] + i < track.length &&
                    c.y_speed + move[0] + i > 0 &&
                    c.x_speed + move[1] + j < track[0].length &&
                    c.x_speed + move[1] + j > 0) {
                // if in here the move doesn't go completely off the track
                // if first time in here set the best move to move that got us here
                if (!hasMove) {
                    bestMove = move;
                    max = calculateUtility(move, i, j);
                } else {
                    // we've already assigned a max and best move
                    // evaluate current bestMove against other move that got here
                    if (calculateUtility(move, i, j) > calculateUtility(bestMove, i, j)) {
                        bestMove = move;
                        policy[i][j] = bestMove;
                        max = calculateUtility(move, i, j);
                    }
                }
                // flag that there is a move available that doesn't crash car
                hasMove = true;           
            } 
        }
        if (hasMove) {
            return max;
        } else {
            c.carCrash();
            return -1; // utility of a wall
        }
    }

    public void printValues() {
        // print out the track in a better way
        for (int i = 0; i < this.value.length; i++) {
            for (int j = 0; j < this.value[0].length; j++) {
                System.out.format("%+3.2f ", this.value[i][j]);
            }
            System.out.println();
        }
    }
    
    public void printMoves() {
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == '.') {
                    System.out.format("Coordinate: (%d, %d) - Move: Y(%d), X(%d) "
                            ,i, j, policy[i][j][0], policy[i][j][1]);
                }
            }
            System.out.println();
        }
    }

    public void cloneArray(double[][] master, double[][] clone) {
        for (int i = 0; i < master.length; i++) {
            for (int j = 0; j < master[0].length; j++) {
                clone[i][j] = master[i][j];
            }
        }
    }
    
    public double calculateUtility(Integer[] move, int i, int j) {
        double probWorks = 0.8;
        
        double utility = (probWorks * value[i + c.y_speed + move[0]][j + c.x_speed + move[1]]) +
                            ((1- probWorks) * value[i + c.y_speed][j + c.x_speed]);
        return utility;
    }
}
