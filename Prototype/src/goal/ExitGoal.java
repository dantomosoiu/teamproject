/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goal;

import com.jme3.math.Vector3f;
import population.Person;
import population.PersonNavmeshRoutePlanner;

/**
 *
 * @author 1003819k
 */
public class ExitGoal extends PassableGoal {
    public ExitGoal( Vector3f location, float clearance) {
        super(location, clearance);
       
    }
    
    public boolean isInExitProximity(PersonNavmeshRoutePlanner p){ //signifies when a person has exited
       if(p.isInLineOfSight(this.getLocation()) && (p.getPosition().distance(this.getLocation()) <= this.getClearance())){
            this.decrementNoOfPeopleQueueing();
            return true;
        }
        return false;
    }

    
    
    
    
  

    
    
    
    
}
