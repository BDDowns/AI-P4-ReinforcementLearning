package ReinforcementLearning;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Group 22
 */
public class SARSA implements RaceBehavior {

    // states is our training matrix, populated with q values via SARSA
    State[][] states;
    State[][] previousStates;

    ArrayList<Integer[]> actions = new ArrayList<>();
    // tunable parameters
    final double alpha = 0.01;
    final double lamda = 0.01;
    final double gamma = 0.1;
    final double epsilon = 0.12;

    @Override
    public void race(Car c, Track t) {
        // Print the starting track and initialize all the states
        // for racing
        printTrack(t);
        setInitialStates(t);
        setPreviousStates(t);
        setActions();
        move(c, t);
        printStates();

        //beginTimeTrials
        System.out.println("");
        startRace(c, t);
    }

    public void startRace(Car c, Track t) {
        //Starting position
        c.x0 = 5;
        c.y0 = 1;
        c.xt = 5;
        c.yt = 1;
        double reward = 0;
        t.getTrack()[c.x0][c.y0] = 'C';
        printTrack(t);

        while (reward != 100) {
            reward = this.states[c.xt][c.yt].getReward();
            State current = this.states[c.xt][c.yt];
            this.states[c.xt][c.yt].setVisited();
            Integer[] action = getRaceAction(c, current, t);
            System.out.println(action[0]);
            System.out.println(action[1]);

            Integer newX = action[0] + c.xt;
            Integer newY = action[1] + c.yt;
            t.getTrack()[c.xt][c.yt] = '.';

            c.xt = newX;
            c.yt = newY;

            t.getTrack()[c.xt][c.yt] = 'C';
            printTrack(t);
        }

    }

    public Integer[] getRaceAction(Car c, State current, Track t) {
//        double rand = Math.random();
//        if (rand < epsilon) {
//            return getRandomAction();
//        }
// change to 20% error later

        Integer[] a = new Integer[2];
        a[0] = 0;
        a[1] = 0;

        double max = -999;
        double move;
        // need to handle acceleration // e-greedy too

        for (int i = 0; i < actions.size(); i++) {
            //find new action
            Integer newX = c.xt + actions.get(i)[0];
            Integer newY = c.yt + actions.get(i)[1];
            move = this.states[newX][newY].getQvalue();
            // make sure we aren't moving backwards
            if (!this.states[newX][newY].getVisited()) {
                if (move > max) {
                    if (t.getTrack()[newX][newY] != '#') {
                        max = move;
                        a[0] = actions.get(i)[0];
                        a[1] = actions.get(i)[1];
                    }
                }
            }
        }

        return a;
    }

    public void setPreviousStates(Track t) {

        Integer[] finalPos = getFinalPosition(t);
        // loop through every state, setting a reward for each possible state
        // the rewards acts as a Q-Value Q(s,a)
        this.previousStates = new State[t.getTrack().length][t.getTrack()[0].length];
        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                this.previousStates[i][j] = new State(i, j, t.getTrack()[i][j], t, finalPos);
            }
        }
    }

    // set possible actions 
    public void setActions() {
        // 0 index is x, 1 index is y. the printed graph is inverted I think
        Integer[] n = new Integer[2];
        n[0] = 1;
        n[1] = 0;
        this.actions.add(n);
        Integer[] e = new Integer[2];
        e[0] = 0;
        e[1] = 1;
        this.actions.add(e);
        Integer[] s = new Integer[2];
        s[0] = -1;
        s[1] = 0;
        this.actions.add(s);
        Integer[] w = new Integer[2];
        w[0] = 0;
        w[1] = -1;
        this.actions.add(w);
    }

    // get the final position to help with Q-value
    public Integer[] getFinalPosition(Track t) {
        Integer[] finalPos = new Integer[2];
        boolean found = false;

        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                if (t.getTrack()[i][j] == 'F' && !found) {
                    found = true;
                    finalPos[0] = i;
                    finalPos[1] = j;
                }
            }
        }
        return finalPos;
    }

    public void setInitialStates(Track t) {

        Integer[] finalPos = getFinalPosition(t);
        // loop through every state, setting a reward for each possible state
        // the rewards acts as a Q-Value Q(s,a)
        this.states = new State[t.getTrack().length][t.getTrack()[0].length];
        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                this.states[i][j] = new State(i, j, t.getTrack()[i][j], t, finalPos);
            }
        }
    }

    public void printStates() {
        for (int i = 0; i < this.states.length; i++) {
            for (int j = 0; j < this.states[0].length; j++) {
                System.out.printf("%.2f", this.states[i][j].getQvalue());
                System.out.print(" ");

                //System.out.print(this.states[i][j].getQvalue() + " ");
            }
            System.out.println("");
        }

    }

    public void move(Car car, Track t) {
        // place car arbitrarily on S and print track
        // then begin SARSA
        t.getTrack()[car.x0][car.y0] = 'C';
        //printTrack(t);

        implementSarsa(car, t);
        // printStates();

    }

    public Integer[] getRandomAction() {
        Random r = new Random();
        int l = 0;
        int h = 3;
        int rand = r.nextInt(h - l) + l;

        return actions.get(rand);
    }

    public void implementSarsa(Car car, Track t) {
        State newState;
        State state;
        double reward;
        double currentQ, nextQ, finalQ;
        boolean done = false;
        int d = 0;
        // Initialize with first action/state. This uses the speed vector later
        Integer[] currentState = new Integer[2];
        currentState[0] = car.xt;
        currentState[1] = car.yt;

        // states[car.xt][car.yt].setQvalue(0.05);
        //qValues.add(0.5);
        state = states[car.xt][car.yt];
        State startingState = state;
        Integer[] action = getRandomAction();

        // While we have yet to reach 'F' use SARSA to continue to search
        while (!done) {
            done = isDone(t);
            newState = nextState(currentState, action);
            t.getTrack()[state.xLoc][state.yLoc] = '.';
            t.getTrack()[newState.xLoc][newState.yLoc] = 'C';
            // printTrack(t);
            reward = newState.getReward();

            Integer[] action2 = newAction(newState, state);

            currentQ = state.getQvalue();
            nextQ = newState.getQvalue();
            finalQ = currentQ + alpha * (reward + gamma * nextQ - currentQ);

            newState.setQvalue(finalQ);

            if (reward == -10) {
                t.getTrack()[state.xLoc][state.yLoc] = '.';
                t.getTrack()[newState.xLoc][newState.yLoc] = '#';
                state.setQvalue(finalQ);
                newState.setQvalue(reward);
                newState = startingState;
            } else if (reward == 100) {
                // We found a path the the finish. move backwards from original start and continue
                newState = moveBackwards(startingState, t);
                startingState = newState;

            }

            state = newState;
            currentState[0] = state.xLoc;
            currentState[1] = state.yLoc;
            action = action2;

        }
    }

    public boolean isDone(Track t) {

        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                if (t.getTrack()[i][j] == 'S') {
                    return false;
                }
            }
        }

        return true;
    }

    public State moveBackwards(State s, Track t) {
        Random r = new Random();
        int l = -3;
        int h = 3;
        int rand = r.nextInt(h - l) + l;
        int yrand = r.nextInt(h - l) + l;

        Integer newX = s.xLoc + rand;
        Integer newY = s.yLoc + yrand;

        if (newX > 0 && newY > 0) {
            if (newX < t.getTrack().length && newY < t.getTrack()[0].length) {
                if (t.getTrack()[newX][newY] == '.' || t.getTrack()[newX][newY] == 'S') {
                    return this.states[newX][newY];
                }
            }
        }
        return s;
    }

    // e-greedy action choice
    public Integer[] newAction(State s, State previous) {
        double rand = Math.random();
        if (rand < epsilon) {
            return getRandomAction();
        }

        Integer[] a = new Integer[2];
        a[0] = s.xLoc;
        a[1] = s.yLoc;
        double max = -999;
        double move = -999;
        // need to handle acceleration // e-greedy too

        for (int i = 0; i < actions.size(); i++) {
            //find new action
            Integer newX = s.xLoc + actions.get(i)[0];
            Integer newY = s.yLoc + actions.get(i)[1];

            if (newX < this.states.length && newY < this.states[0].length) {
                move = this.states[newX][newY].getQvalue();
            }
            // make sure we aren't moving backwards
            if (move < max) {
                max = move;
                a[0] = actions.get(i)[0];
                a[1] = actions.get(i)[1];
            }
        }

        return a;
    }

    public State nextState(Integer[] currentState, Integer[] action) {
        Integer newX = currentState[0] + action[0];
        Integer newY = currentState[1] + action[1];
        if (newX < this.states.length && newY < this.states[0].length) {
            return this.states[newX][newY];
        } else {
            return this.states[currentState[0]][currentState[1]];
        }
    }

    public void printTrack(Track t) {
        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                System.out.print(t.getTrack()[i][j]);
            }
            System.out.println("");
        }
    }
}
