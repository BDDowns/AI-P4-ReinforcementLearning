package ReinforcementLearning;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Group 22
 */
public class ValueIteration implements RaceBehavior {

    //Track tk;
    static char[][] track;
    double[][] value;
    double[][] value_1;
    Track t;
    Car c;
    boolean close;
    ArrayList<Integer[]> actions = new ArrayList<>();

    @Override
    public void race(Car c, Track t) {
        this.t = t;
        track = t.getTrack();
        value = new double[track.length][track[0].length];
        this.c = c;
        place_Values();
        //setNearFinish();
        setActions();
        do_Calc(value);
    }

    public double[][] place_Values() {
        for (int i = 0; i < track.length; i++) {
            System.out.println();
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 'F') {
                    value[i][j] = 1;
                }
                if (track[i][j] == '#') {
                    value[i][j] = -1;
                }
                if (track[i][j] == '.') {
                    value[i][j] = 0;
                }
                if (track[i][j] == 'S') {
                    value[i][j] = 0;
                }
                System.out.print(value[i][j] + " ");
            }

        }
        return value;
    }

    public void setNearFinish() {
        int y, x;
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track.length; j++) {
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
    public void do_Calc(double[][] value) {
        double delta;
        double[][] value_1 = new double[value.length][value[0].length];
        cloneArray(value, value_1);
        double epsilon = 0.001;
        double discount = 0.75;

        do {
            cloneArray(value_1, value);
            delta = 0;
            for (int i = 0; i < value.length; i++) {
                for (int j = 0; j < value[0].length; j++) {
                    if (track[i][j] == '.') {
                        // compute utility based on utility of neighbors
                        value_1[i][j] = value[i][j] + getMaxActionUtility(i, j);
                        if (Math.abs(value_1[i][j] - value[i][j]) > delta) {
                            delta = Math.abs(value_1[i][j] - value[i][j]);
                        }
                    }
                }
            }
            printValues();
        } while (delta > (epsilon * (1 - discount) / discount));

    }

    // return max Sigma(s1) P(s1 | s, a) * U[s1]
    public double getMaxActionUtility(int i, int j) {
        Integer[] move;
        double max = -1;
        for (int k = 0; k < actions.size(); k++) {
            // make sure not out of bounds
            move = actions.get(k);
            max = Math.max(max, 0.8 * value[i + move[0]][j + move[1]]
                    + 0.2 * value[i][j]);
        }
        return max;
    }

    public void printValues() {
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[0].length; j++) {
                if (track[i][j] == '.' || track[i][j] == 'F') {
                    System.out.format("%3d ", (int)value[i][j]);
                }
            }
            System.out.println();
        }
    }
    
    public void cloneArray(double[][] original, double[][] target) {
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                target[i][j] = original[i][j];
            }
        }
    }
}
