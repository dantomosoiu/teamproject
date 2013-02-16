/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.population;

import Init.Settings.Settings;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;

/**
 *
 * @author michael
 * Listens for a Person crossing a MotionPath waypoint and updates the navmesh position.
 */
public class PersonMovementListener implements MotionPathListener{
    Person person;
    PersonMovementListener(Person p){
        person = p;
    }
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
       if (Settings.get().getPrintEv()) System.out.println("Person pos:" + person.getPerson().getLocalTranslation());
        
    }
    
}
