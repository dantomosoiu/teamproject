/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goal;

import com.jme3.math.Vector3f;
import population.PersonNavmeshRoutePlanner;

/**
 *
 * @author michael
 */
public abstract class PassableGoal extends Goal {
    public static float QUEUEINGDISTANCE = 1f;
    private float clearance;
    private int noOfPeopleQueueing;
   
    PassableGoal(Vector3f location,float clearance ){
        super(location);
        this.clearance = clearance;
        noOfPeopleQueueing = 0;
    }
    
    public boolean registerWithGoal(PersonNavmeshRoutePlanner p){
      if(isInQueueingProximity(p)){
          noOfPeopleQueueing++;
          return true;
      }
      return false;
    }

    public int getNoOfPeopleQueueing() {
        return noOfPeopleQueueing;
    }

    public float getClearance() {
        return clearance;
    }
    
    
    public boolean isInQueueingProximity(PersonNavmeshRoutePlanner p){
        if(p.isInLineOfSight(this.getLocation()) && (p.getPosition().distance(this.getLocation()) <= QUEUEINGDISTANCE)){
            return true;
        }
        return false;
    }
    
    public void incrementNoOfPeopleQueueing(){
        noOfPeopleQueueing++;
    }
    
    public void decrementNoOfPeopleQueueing(){
        noOfPeopleQueueing--;
    }
}
