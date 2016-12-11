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
    double reward;
    Integer[] action = new Integer[2];
    ArrayList<Integer[]> finalStates = new ArrayList<>();
    Track track;
    Integer[] finalPos;
    double qValue = 0;
    boolean visited = false;

    // Need to set rewards based of distance to finish line I think...
    // Not sure though. If this is done should be straigh forward to fiinish
    public State(int x, int y, char c, Track t, Integer[] fp) {
        this.xLoc = x;
        this.yLoc = y;
        track = t;
        finalPos = fp;
        this.reward = setReward(c);
        
    }
    
    public void setVisited(){
        this.visited = true;
    }
    
    public boolean getVisited(){
        return this.visited; 
    }
    
    public void setQvalue(double q){
        this.qValue = q;
    }
    
    public double getQvalue(){
        return this.qValue;
    }

    public Integer[] getLocation() {
        Integer[] location = new Integer[2];
        location[0] = this.xLoc;
        location[1] = this.yLoc;
        
        return location;
    }
    
    public double getReward() {
        return this.reward;
    }
    
    private double setReward(char c) {
        
        switch (c) {
            case '#' : {
                return -5;
            }
            case 'F' : {
                return 100;
            }
            case '.' : {
                return 0;
            }
            case 'S' : {
                return 0;
            }
        }
        
        return 1;
    }
    
    public double distanceFromGoal(){
        double distance = Math.sqrt(Math.pow((this.finalPos[0]-this.xLoc),2) + Math.pow((this.finalPos[1]-this.yLoc),2));
        System.out.println(distance);
        return distance;
    }
    
    public void setAction(Integer[] a){
        this.action = a;
    }
    
    public Integer[] getAction() {
        return this.action;
    }
}
