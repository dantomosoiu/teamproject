/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.population;

import EvacSim.goal.Goal;
import Init.Settings.Settings;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 * Listens for a Person crossing a MotionPath waypoint and updates the navmesh position.
 */
public class PersonMovementListener implements MotionPathListener{
    Person person;
    int waypointsBetweenDecisions;
    
    PersonMovementListener(Person p, int waypointsBetweenDecisions){
        person = p;
        this.waypointsBetweenDecisions = waypointsBetweenDecisions;
    }
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
       if(wayPointIndex % waypointsBetweenDecisions == 0){
           try {
               Goal g = BehaviourModel.perceiveDecideAct(person);
               person.changeGoal(g);
               
           }
           catch (Exception e) {
               person.isFin();
           }
       }
        
    }
    
}
