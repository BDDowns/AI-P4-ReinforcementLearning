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
    final double gamma = 0.2;
    final double epsilon = 0.9;

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
        startRace(c, t);
    }

    public void startRace(Car c, Track t) {
        //Starting position could be any S value
        c.x0 = 7;
        c.y0 = 2;
        c.xt = 7;
        c.yt = 2;
        //start at -1 since crossing finish line does not have a cost
        int cost = -1;
        char end = '.';
        int crash = 0;
        t.getTrack()[c.x0][c.y0] = 'C';
        printTrack(t);

        //while we havent found the finish line, keep following policy derrived by SARSA
        while (end != 'F') {
            end = t.getTrack()[c.xt][c.yt];
            State current = this.states[c.xt][c.yt];
            this.states[c.xt][c.yt].setVisited();
            Integer[] action = getRaceAction(c, current, t);

            Integer newX = action[0] + c.xt;
            Integer newY = action[1] + c.yt;

            // If new move is optimal, take it! 
           if (newX > 0 && newY > 0 && newX < this.states.length && newY < this.states[0].length) {
                end = t.getTrack()[newX][newY];
                if (t.getTrack()[newX][newY] != '#') {
                    t.getTrack()[c.xt][c.yt] = '.';

                    c.xt = newX;
                    c.yt = newY;
                    cost++;
                    t.getTrack()[c.xt][c.yt] = 'C';
                } else {
                    System.out.println("CRASH");
                    crash++;
                }
                printTrack(t);
            }
        }
        System.out.println("Cost: " + cost);
        System.out.println("Crash: " + crash);
    }

    public Integer[] getRaceAction(Car c, State current, Track t) {
        Integer[] velocity = new Integer[2];
        velocity[0] = c.x_speed;
        velocity[1] = c.y_speed;

        // 20% of random move else apply current velocity and find optimal move
        double rand = Math.random();
        if (rand < .2) {
            Integer[] ra = getRandomAction();
            Integer x = ra[0] + velocity[0];
            Integer y = ra[1] + velocity[1];
            if (x > 0 && y > 0 && x + c.xt < this.states.length && y + c.yt < this.states[0].length) {
                ra[0] = x;
                ra[1] = y;
            }
            System.out.println("Velocity <" + c.x_speed + "," + c.y_speed + ">");
            return ra;
        }

        Integer[] a = new Integer[2];
        a[0] = 0;
        a[1] = 0;

        double max = -999;
        double move;

        // two disgusting loops to look for all possible moves considering current velocity and possible velocities
        for (int i = 0; i < actions.size(); i++) {
            //find new action based off current velocity
            Integer newX = c.xt + actions.get(i)[0] + velocity[0];
            Integer newY = c.yt + actions.get(i)[1] + velocity[1];
            if (newX > 0 && newX < this.states.length && newY > 0 && newY < this.states[0].length) {

                move = this.states[newX][newY].getQvalue();
                // make sure we aren't moving backwards
                if (!this.states[newX][newY].getVisited()) {
                    if (move > max) {
                        if (t.getTrack()[newX][newY] != '#') {
                            max = move;
                            a[0] = actions.get(i)[0] + velocity[0];
                            a[1] = actions.get(i)[1] + velocity[1];
                        }
                    }
                }
            }
        }
        for (int xmove = -1; xmove < 2; xmove++) {
            for (int ymove = -1; ymove < 2; ymove++) {
                for (int i = 0; i < actions.size(); i++) {
                    //find new action based off change in velocity velocity
                    Integer newX = c.xt + actions.get(i)[0] + velocity[0] + xmove;
                    Integer newY = c.yt + actions.get(i)[1] + velocity[1] + ymove;
                    if (newX > 0 && newX < this.states.length && newY > 0 && newY < this.states[0].length) {
                        move = this.states[newX][newY].getQvalue();
                        // make sure we aren't moving backwards
                        if (!this.states[newX][newY].getVisited()) {
                            if (move > max) {
                                if (t.getTrack()[newX][newY] != '#') {
                                    if (c.x_speed < 5 && c.x_speed > -5) {
                                        c.x_speed += xmove;
                                    }
                                    if (c.y_speed < 5 && c.y_speed > -5) {
                                        c.y_speed += ymove;
                                    }
                                    max = move;
                                    a[0] = actions.get(i)[0] + c.x_speed;
                                    a[1] = actions.get(i)[1] + c.y_speed;
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Velocity: <" + c.x_speed + "," + c.y_speed + ">");
        return a;
    }

    public void setPreviousStates(Track t) {

        // loop through every state, setting a reward for each possible state
        // the rewards helps determine Q-Value Q(s,a)
        this.previousStates = new State[t.getTrack().length][t.getTrack()[0].length];
        for (int i = 0; i < t.getTrack().length; i++) {
            for (int j = 0; j < t.getTrack()[0].length; j++) {
                this.previousStates[i][j] = new State(i, j, t.getTrack()[i][j], t);
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
                System.out.print(this.states[i][j].getQvalue());
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
        implementSarsa(car, t);

    }

    public Integer[] getRandomAction() {
        Random r = new Random();
        int l = 0;
        int h = 4;
        int rand = r.nextInt(h - l) + l;

        return actions.get(rand);
    }

    public void implementSarsa(Car car, Track t) {
        State newState;
        State state;
        double reward;
        double currentQ, nextQ, finalQ;
        boolean done = false;
        int thresh = 0;
        // Initialize with first action/state.
        Integer[] currentState = new Integer[2];
        currentState[0] = car.xt;
        currentState[1] = car.yt;

        state = states[car.xt][car.yt];
        states[car.xt][car.yt].setQvalue(.5);
        State startingState = state;
        Integer[] action = getRandomAction();

        // While we have yet to reach 'F' use SARSA to continue to search
        while (!done) {
            done = isDone(t);
            thresh++;
            newState = nextState(currentState, action);

            t.getTrack()[state.xLoc][state.yLoc] = '.';
            char character = t.getTrack()[newState.xLoc][newState.yLoc];

            // If we reach the final state, we have a working path, backup and restart
            if (character == 'F') {
                t.getTrack()[newState.xLoc][newState.yLoc] = 'F';
            } else {
                t.getTrack()[newState.xLoc][newState.yLoc] = 'C';
            }

            // Get values for SARA, Q(s',a') and Q(s,a)
            reward = newState.getReward();
            Integer[] action2 = newAction(newState, state);
            currentQ = state.getQvalue();
            nextQ = newState.getQvalue();
            
            // Actual SARSA calculation with Q(s,a) and Q(s',a')
            finalQ = currentQ + alpha * (reward + gamma * nextQ - currentQ);

            // update value
            newState.setQvalue(finalQ);

            // if we crashed, we need to update the Qvalue accordingly
            if (reward == -10) {
                t.getTrack()[state.xLoc][state.yLoc] = '.';
                t.getTrack()[newState.xLoc][newState.yLoc] = '#';
                state.setQvalue(finalQ);
                newState.setQvalue(reward);
                newState = startingState;
                if (thresh >= 999) {
                    thresh = 0;
                    Random r = new Random();
                    int rand = r.nextInt(states.length - 0);
                    int yrand = r.nextInt(states[0].length - 0);
                    if (t.getTrack()[rand][yrand] == '.') {
                        newState = states[rand][yrand];
                    }
                }
            } else if (character == 'F') {
                // We found a path the the finish. move backwards from original start and continue
                state.setQvalue(finalQ);
                newState.setQvalue(reward);
                newState = moveBackwards(startingState, t);
                startingState = newState;

            }

            // log the move and continue
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

    // randomly move backwards and continue SARSA
    public State moveBackwards(State s, Track t) {
        Random r = new Random();
        int l = -2;
        int h = 2;

        while (true) {
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
        }
    }

    // e-greedy action choice
    public Integer[] newAction(State s, State previous) {
        // Greedily choose, or if rand is lower than epsilon choose at random. Higher epsilon encourages exploration
        double rand = Math.random();
        if (rand < epsilon) {
            return getRandomAction();
        }

        Integer[] a = new Integer[2];
        a[0] = s.xLoc;
        a[1] = s.yLoc;
        double max = -999;
        double move = -999;

        for (int i = 0; i < actions.size(); i++) {
            //find new action
            Integer newX = s.xLoc + actions.get(i)[0];
            Integer newY = s.yLoc + actions.get(i)[1];
            if (newX < this.states.length && newY < this.states[0].length && newX > 0 && newY > 0) {
                move = this.states[newX][newY].getQvalue();
            }
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
