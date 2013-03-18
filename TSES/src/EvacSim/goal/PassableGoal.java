/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.goal;

import EvacSim.population.PersonNavmeshRoutePlanner;
import com.jme3.math.Vector3f;

/**
 *
 * @author michael
 */
public abstract class PassableGoal extends Goal {
    /**
     *
     */
    public static float QUEUEINGDISTANCE = 1f;
    private float clearance;
    private int noOfPeopleQueueing;
   
    PassableGoal(Vector3f location,float clearance ){
        super(location);
        this.clearance = clearance;
        noOfPeopleQueueing = 0;
    }
    
    /**
     *
     * @param p
     * @return
     */
    public boolean registerWithGoal(PersonNavmeshRoutePlanner p){
      if(isInQueueingProximity(p)){
          noOfPeopleQueueing++;
          return true;
      }
      return false;
    }

    /**
     *
     * @return
     */
    public int getNoOfPeopleQueueing() {
        return noOfPeopleQueueing;
    }

    /**
     *
     * @return
     */
    public float getClearance() {
        return clearance;
    }
    
    
    /**
     *
     * @param p
     * @return
     */
    public boolean isInQueueingProximity(PersonNavmeshRoutePlanner p){
        if(p.isInLineOfSight(this.getLocation()) && (p.getPosition().distance(this.getLocation()) <= QUEUEINGDISTANCE)){
            return true;
        }
        return false;
    }
    
    /**
     *
     */
    public void incrementNoOfPeopleQueueing(){
        noOfPeopleQueueing++;
    }
    
    /**
     *
     */
    public void decrementNoOfPeopleQueueing(){
        noOfPeopleQueueing--;
    }
}
