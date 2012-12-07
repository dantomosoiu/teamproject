/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import jme3tools.navmesh.NavMesh;
import com.jme3.scene.Node;

/*
 *
 * @author michael
 */
public class Population {
    public static Vector3f GOAL = new Vector3f(0f,0f,0f);
    private SimpleApplication simp;
    private Thread people[];
    private com.jme3.scene.Node rootNode;
    private NavMesh mesh;
    
    public Population(com.jme3.scene.Node rootNode, NavMesh mesh, SimpleApplication simp){
        this.mesh = mesh;
        this.simp = simp;
        this.rootNode = rootNode;
    }
    public void populate(int popNumber){
        people = new Thread[popNumber];
        for(int i = 0; i<popNumber; i++){
            people[i] = new Thread(new Person(mesh, rootNode, simp));
        }
    }
    
    public void evacuate(){
        for(int i = 0; i < people.length; i++){
            people[i].run();
        }
       /* for(int i = 0; i < people.length; i++){
            try{
               people[i].join();
            }catch(InterruptedException e){return ;}
        }*/
    }
}
