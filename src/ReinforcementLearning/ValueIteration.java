package ReinforcementLearning;

import java.util.ArrayList;

/**
 *
 * @author Group 22
 */
public class ValueIteration implements RaceBehavior {

    //Track tk;
    static char[][] track;
    static double[][] value;
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
        setActions();
        do_Calc(value);
    }

    public double[][] place_Values() {
        for (int i = 0; i < track.length; i++) {
            System.out.println();
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 'F') {
                    value[i][j] = 100;
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
        double utility = 0;
        double delta = 0;
        double utility_1 = 0;
        double epsilon = 0.5;
        double discount = 0.5;

        do {
            utility = utility_1;
            delta = 0;
            for (int i = 0; i < value.length; i++) {
                for (int j = 0; j < value[0].length; j++) {
                    if (track[i][j] == '.') {
                        // compute utility based on utility of neighbors
                        utility_1 = value[i][j] + getMaxActionUtility(i, j);
                        if (Math.abs(utility_1 - utility) > delta) {
                            delta = Math.abs(utility_1 - utility);
                        }
                    }
                }
            }
        } while (delta > (epsilon * (1 - discount) / discount));

    }

    // return max Sigma(s1) P(s1 | s, a) * U[s1]
    public double getMaxActionUtility(int i, int j) {
        Integer[] move;
        double max = -1000;
        for (int k = 0; k < actions.size(); k++) {
            // make sure not out of bounds
            move = actions.get(k);
            max = Math.max(max, 0.8 * value[i + move[0]][j + move[1]]
                    + 0.2 * value[i][j]);
        }
        return max;
    }

}
