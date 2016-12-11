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
    ArrayList<Integer[]> actions = new ArrayList<>();
    final double alpha = 0.01;
    final double lamda = 0.01;
    final double gamma = 0.1;
    final double epsilon = 0.1;

    @Override
    public void race(Car c, Track t) {
        // Print the starting track and initialize all the states
        // for racing
        printTrack(t);
        setInitialStates(t);
        setActions();
        move(c, t);
        //printStates();
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
                System.out.print(this.states[i][j].getQvalue() + " ");
            }
            System.out.println("");
        }

    }

    public void move(Car car, Track t) {
        // place car arbitrarily on S and print track
        // then begin SARSA
        t.getTrack()[car.x0][car.y0] = 'C';
        printTrack(t);

        implementSarsa(car, t);
        printStates();

    }

    public Integer[] getRandomAction() {
        Random r = new Random();
        int l = 0;
        int h = 3;
        int rand = r.nextInt(h - l) + l;

        return actions.get(rand);
    }

    public void implementSarsa(Car car, Track t) {
        ArrayList<State> path = new ArrayList<>();
        ArrayList<Double> qValues = new ArrayList<>();
        State newState;
        State state;
        double reward;
        double currentQ, nextQ, finalQ;
        boolean done = false;
        // Initialize with first action/state. This uses the speed vector later
        Integer[] currentState = new Integer[2];
        currentState[0] = car.xt;
        currentState[1] = car.yt;

        states[car.xt][car.yt].setQvalue(0.5);
        qValues.add(0.5);
        state = states[car.xt][car.yt];
        Integer[] action = getRandomAction();

        path.add(states[car.xt][car.yt]);
        // While we have yet to reach 'F' use SARSA to continue to search
        while (!done) {
            newState = nextState(currentState, action);
            path.add(newState);
            t.getTrack()[state.xLoc][state.yLoc] = '.';
            t.getTrack()[newState.xLoc][newState.yLoc] = 'C';
            printTrack(t);
            reward = newState.getReward();

            Integer[] action2 = newAction(newState, state);
            currentQ = state.getQvalue();
            nextQ = newState.getQvalue();
            finalQ = currentQ + alpha * (reward + gamma * nextQ - currentQ);
            qValues.add(finalQ);
            newState.setQvalue(finalQ);
            if (reward == -1) {
                //crash();
                System.out.println("crash");
                state.setQvalue(finalQ + reward);
                newState.setQvalue(reward);
                break;
            } else {
                // update stateReward
                state = newState;
                currentState[0] = state.xLoc;
                currentState[1] = state.yLoc;
                action = action2;
                //if final state done == true
                if (reward == 100) {
                    System.out.println("finish");
                    break;
                }

            }
        }
    }

    public Integer[] newAction(State s, State previous) {
        double rand = Math.random();
        if (rand < .25) {
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

            if(newX < this.states.length && newY < this.states[0].length){
                 move = this.states[newX][newY].getReward();
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
