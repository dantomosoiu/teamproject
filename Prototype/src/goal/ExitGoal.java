/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goal;

import com.jme3.math.Vector3f;
import population.Person;

/**
 *
 * @author 1003819k
 */
public class ExitGoal extends Goal {
    float clearance;

    public ExitGoal( Vector3f location, float clearance) {
        super(location);
        this.clearance = clearance;
    }

    @Override
    public Vector3f registerWithGoal(Person p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
  

    
    
    
    
}
