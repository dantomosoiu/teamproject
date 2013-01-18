/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import goal.Goal;
/**
 *
 * @author 1003819k
 */
public abstract class BehaviourModel {
    private static ArrayList<Goal> goals;
    
    public static void perceiveDecideAct(Person person){
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

    private static Vector3f decide(ArrayList<Goal> goals){
        return Vector3f.ZERO;
    }
    
    private void act(Person person, Vector3f goalPosition){
        person.moveTo(goalPosition, 0.01f);
    }
    public static ArrayList<Goal> getGoals() {
        return goals;
    }

    public static void setGoals(ArrayList<Goal> goals) {
        BehaviourModel.goals = goals;
    }
    
    
    
}
