package ReinforcementLearning;

import java.util.ArrayList;

/**
 *
 * @author Group 22
 */
public class SARSA implements RaceBehavior {

    State[][] states;
    ArrayList<Integer[]> actions = new ArrayList<>();

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
        // 0 index is x, 1 index is y

        Integer[] n = new Integer[2];
        n[0] = 1;
        n[1] = 0;
        this.actions.add(n);
        Integer[] e = new Integer[2];
        e[0] = 0;
        e[1] = 1;
        this.actions.add(e);
        Integer[] ne = new Integer[2];
        ne[0] = 1;
        ne[1] = 1;
        this.actions.add(ne);
        Integer[] se = new Integer[2];
        se[0] = -1;
        se[1] = 1;
        this.actions.add(se);
        Integer[] s = new Integer[2];
        s[0] = -1;
        s[1] = 0;
        this.actions.add(s);
        Integer[] sw = new Integer[2];
        sw[0] = -1;
        sw[1] = -1;
        this.actions.add(sw);
        Integer[] w = new Integer[2];
        s[0] = -1;
        s[1] = 0;
        this.actions.add(s);
        Integer[] nw = new Integer[2];
        nw[0] = -1;
        nw[1] = 1;
        this.actions.add(nw);

    }

    public void setInitialStates(Track t) {
        // loop through every state, setting a reward for each possible state
        // the rewards acts as a Q-Value Q(s,a)
        this.states = new State[t.getTrack().length][t.getTrack()[0].length];
        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                this.states[i][j] = new State(i, j, t.getTrack()[i][j], t);
            }
        }
    }

    public void printStates() {
        for (int i = 0; i < this.states.length; i++) {
            for (int j = 0; j < this.states[0].length; j++) {
                System.out.print(this.states[i][j].getReward());
            }
            System.out.println("");
        }

    }

    public void move(Car car, Track t) {
        // place car arbitrarily on S and print track
        // then begin SARSA
        t.getTrack()[car.x0][car.y0] = 'C';
        printTrack(t);
//        car.accelY(1);
//        car.updatePosition();
//        t.getTrack()[car.xt][car.yt] = 'C';
//        printTrack(t);
        implementSarsa(car, t);

    }

    public void implementSarsa(Car car, Track t) {
        State newState;
        double reward;
        double currentQ, nextQ, finalQ;
        boolean done = false;
        // Initialize with first action/state. This uses the speed vector later
        Integer[] currentState = new Integer[2];
        currentState[0] = car.xt;
        currentState[1] = car.yt;

        Integer[] action = getAction(car);

        // While we have yet to reach 'F' use SARSA to continue to search
        while (!done) {
            newState = nextState(currentState, action);
            reward = newState.getReward();
            
            Integer[] action2 = newAction(newState);
            
            // currentQ = getQ(currentState, action)
            // nextQ = getQ(nextState, action2)
            // finalQ = currentQ + alpha * (reward + gamma * nextQ - currentQ)
            // update stateReward
            
            //state = newState
            //action = newaction
            
            //if final state done == true
            
            break;
        }
    }
    
    public Integer[] newAction(State s){
        Integer[] a = new Integer[2];
        // need to handle acceleration
        
        for(int i = 0; i < actions.size(); i++){
            //find new action
        }
        
        return a;
    }

    public Integer[] getAction(Car car) {
        Integer[] a = new Integer[2];
        double max = 0;

        for (int i = 0; i < this.actions.size(); i++) {
            Integer newX = car.xt + actions.get(i)[0];
            Integer newY = car.yt + actions.get(i)[1];
            int move = this.states[newX][newY].getReward();
            if (move > max) {
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

        return this.states[newX][newY];
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
