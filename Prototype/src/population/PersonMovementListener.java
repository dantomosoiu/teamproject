/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;

/**
 *
 * @author michael
 */
public class PersonMovementListener implements MotionPathListener{
    Person person;
    PersonMovementListener(Person p){
        person = p;
    }
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        person.warp(motionControl.getSpatial().getLocalTranslation());
        System.out.println("CURRENT waypoint: " + wayPointIndex);
    }
    
}
