/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.goal;

import EvacSim.population.PersonNavmeshRoutePlanner;
import com.jme3.math.Vector3f;

/**
 *
 * @author 1003819k
 */
public class ExitGoal extends PassableGoal {
    /**
     *
     * @param location
     * @param clearance
     */
    public ExitGoal( Vector3f location, float clearance) {
        super(location, clearance);
       
    }
    
    /**
     *
     * @param p
     * @return
     */
    public boolean isInExitProximity(PersonNavmeshRoutePlanner p){ //signifies when a person has exited
       if(p.isInLineOfSight(this.getLocation()) && (p.getPosition().distance(this.getLocation()) <= this.getClearance())){
            this.decrementNoOfPeopleQueueing();
            return true;
        }
        return false;
    }

    
    
    
    
  

    
    
    
    
}
