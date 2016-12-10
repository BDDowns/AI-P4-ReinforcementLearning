/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ReinforcementLearning;

import java.util.ArrayList;

/**
 *
 * @author Austin
 */
public class State {

    int xLoc, yLoc;
    Integer reward;
    Integer[] action = new Integer[2];
    ArrayList<Integer[]> finalStates = new ArrayList<>();

    // Need to set rewards based of distance to finish line I think...
    // Not sure though. If this is done should be straigh forward to fiinish
    public State(int x, int y, char c, Track t) {
        this.xLoc = x;
        this.yLoc = y;
        this.reward = setReward(c);
    }

    public Integer[] getLocation() {
        Integer[] location = new Integer[2];
        location[0] = this.xLoc;
        location[1] = this.yLoc;
        
        return location;
    }
    
    public Integer getReward() {
        return this.reward;
    }
    
    private int setReward(char c) {
        
        switch (c) {
            case '#' : {
                return 0;
            }
            case 'F' : {
                return 5;
            }
            case '.' : {
                return 2;
            }
            case 'S' : {
                return 1;
            }
        }
        
        return 1;
    }
    
    public void setAction(Integer[] a){
        this.action = a;
    }
    
    public Integer[] getAction() {
        return this.action;
    }
}
