/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import java.util.ArrayList;
import goal.Goal;
/**
 *
 * @author 1003819k
 */
public abstract class BehaviourModel {
    private static ArrayList<Goal> goals;
    
    public static void percieveDecideAct(Person person){
        ArrayList<Goal> visibleGoals = scan(person);
        
    }
            
            
    private static ArrayList<Goal> scan(Person person){
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
    
    
    
}
