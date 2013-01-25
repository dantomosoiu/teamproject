/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.math.Vector3f;
import goal.ExitGoal;
import java.util.ArrayList;
import goal.Goal;
/**
 *
 * @author 1003819k
 */
/**Consider replacing this with a Factory desgin pattern to allow for swapping
 * in of behavioural models with minimal coupling*/
public abstract class BehaviourModel {
    private static ArrayList<Goal> goals = new ArrayList<Goal>();
    private static ArrayList<ExitGoal> exits = new ArrayList<ExitGoal>();
    
    public static void percieveDecideAct(Person person){
        ArrayList<Goal> visibleGoals = perceive(person);
        
    }
            
            
    private static ArrayList<Goal> perceive(Person person){
        ArrayList<Goal> visibleGoals = new ArrayList<Goal>();
        for(Goal g : goals){
            if(person.isInLineOfSight(g.getLocation())){
                visibleGoals.add(g);
                System.out.println(g.getLocation().toString()); //debugging line
            }
        }
        return visibleGoals;
    }
    
    

    public static ArrayList<Goal> getGoals() {
        return goals;
    }

    public static void setGoals(ArrayList<Goal> goals) {
        BehaviourModel.goals = goals;
    }

    public static ArrayList<ExitGoal> getExits() {
        return exits;
    }

    public static void setExits(ArrayList<ExitGoal> exits) {
        BehaviourModel.exits = exits;
    }
    
    public static Vector3f nearestExit(Vector3f personlocation){
         //possibly improve efficiency for larger amounts of exits
        Vector3f closeExit = exits.get(0).getLocation();
        for(ExitGoal exit: exits){
            if(personlocation.distance(exit.getLocation()) < personlocation.distance(closeExit)){
                closeExit = exit.getLocation();
            }
        }
        return closeExit;
    }

   

   
    
    
    
}
