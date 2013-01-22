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
public class DoorGoal extends Goal{
    private  int noOfPeople = 0;
    private  Vector3f normal;
    private  Vector3f sideA;
    private  Vector3f sideB;
    private  float clearance;
    
    public DoorGoal(Vector3f location,Vector3f n ,float c) {
        super(location);
        normal = n.normalize();
        sideA = location.add(normal.mult(c));
        sideB = location.subtract(normal.mult(c));
        
    }
    
      
    public Vector3f registerWithGoal(Person p){
         noOfPeople++;
         Vector3f personPos = p.getCurrentPos3d();
         if(personPos.distance(sideA) < personPos.distance(sideB))
             return sideB;
         else
             return sideA;
         
    }
}
