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
 * Top level class in inheritance hierarchy for goals
 */
public abstract class Goal { 
    private Vector3f location;

    public Goal(Vector3f location) {
        this.location = location;
    }
    
    public Vector3f getLocation() {
        return location;    
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }
    
    
}
