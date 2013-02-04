/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import goal.DoorGoal;
import jme3tools.navmesh.NavMesh;
import goal.ExitGoal;
import goal.Goal;
import java.util.ArrayList;
/**
 *
 * @author 1003819k
 */
/**Consider replacing this with a Factory desgin pattern to allow for swapping
 * in of behavioural models with minimal coupling*/
public abstract class BehaviourModel {
    static float STRESSOFFSET = 30f;
    private static ArrayList<Goal> goals = new ArrayList<Goal>();
    private static ArrayList<ExitGoal> exits = new ArrayList<ExitGoal>();
    
    public static void percieveDecideAct(Person person){
        PersonNavmeshRoutePlanner personOnNavmesh = new PersonNavmeshRoutePlanner(person.getNavmesh(),person.getPerson().getLocalTranslation());
        //Check if person is moveing through an exit - if so return 
        ArrayList<Goal> visibleGoals = perceive(personOnNavmesh);
        ArrayList<ExitGoal>  visibleExits = new ArrayList<ExitGoal>();
        
        //seperate goals
        
        for(int i = 0; i < visibleGoals.size(); i++){
            Goal g = visibleGoals.get(i);
            if(g instanceof ExitGoal){
                visibleExits.add((ExitGoal) g);
            }
        }
        
        Goal target = decide(person,personOnNavmesh,visibleExits.toArray(new ExitGoal[visibleExits.size()]));
        
    }
        
        
        
    
    
      private static ArrayList<Goal> perceive(PersonNavmeshRoutePlanner person){
        ArrayList<Goal> visibleGoals = new ArrayList<Goal>();
        for(Goal g : goals){
            if(person.isInLineOfSight(g.getLocation())){
                visibleGoals.add(g);
                System.out.println(g.getLocation().toString()); //debugging line
            }
        }
        return visibleGoals;
    }
    
      private static Goal decide(Person person,PersonNavmeshRoutePlanner p, ExitGoal[] exits /*, DoorGoal[] doors*/){
          Goal target = null;
          NavMesh navmesh = p.getNavmesh();
          float closestGoalDistance;
          int maxPeopleAtGoal;
          boolean stressed = isStressed(person);
          
          
          if(exits.length > 0){ //at least one exit in view
           /*Consider exits first: if a person is stressed they will naturally pick the
           * exit with the most people queing at it (Social Proof Theory).
           * Otherwise they will pick the closest exit.
           */
            ExitGoal targetExit = exits[0];
            if(stressed){
              for(int i = 1; i<exits.length; i++){
                  if(exits[i].getNoOfPeopleQueueing() > targetExit.getNoOfPeopleQueueing()){
                      targetExit = exits[i]; //note cast to type Goal
                  }
              }
              return target;
            }else{
                Vector3f personLocation = p.getCurrentPos3d();
                for(int i = 1; i <exits.length; i++){
                    if(personLocation.distance(exits[i].getLocation()) < personLocation.distance(targetExit.getLocation())){
                        targetExit = exits[i];
                    }
                }
            }
            target = targetExit; //note cast to type Goal
          }
        return target;
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
    
    public static Goal nearestExit(Vector3f personlocation){
         //possibly improve efficiency for larger amounts of exits
        Goal closeExit = exits.get(0);
        for(ExitGoal exit: exits){
            if(personlocation.distance(exit.getLocation()) < personlocation.distance(closeExit.getLocation())){
                closeExit = exit;
            }
        }
        return closeExit;
    }

    private static boolean isStressed(Person p){
        //calculate a random 'breaking point' for over which a person will make a stressed decision
        float monteCarloRange = FastMath.nextRandomFloat() * 100 + STRESSOFFSET; 
        //if the person's stress is below the threshold
        if(p.getStress() <= monteCarloRange)
            return false;
        return true;
    }
   

   
    
    
    
}
