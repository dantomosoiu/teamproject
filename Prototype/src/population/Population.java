/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import jme3tools.navmesh.NavMesh;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/*
 *
 * @author michael, tony, dan
 */
public class Population implements Runnable {

    public static Vector3f GOAL = new Vector3f(1f, 0f, 1f);
    private SimpleApplication simp;
    private Person people[];
    private Thread peopleThreads[];
    private com.jme3.scene.Node rootNode;
    private NavMesh mesh;
    private ArrayList<LinkedList<Person>> neighbourList;
    private BoundaryComparator bComp = new BoundaryComparator();

    public Population(com.jme3.scene.Node rootNode, NavMesh mesh, SimpleApplication simp) {
        this.mesh = mesh;
        this.simp = simp;
        this.rootNode = rootNode;
    }

    public void populate(int popNumber) {
        people = new Person[popNumber];
        peopleThreads = new Thread[popNumber];
        for (int i = 0; i < popNumber; i++) {
           //people[i] = new Person(mesh, rootNode, simp, new Vector3f(FastMath.nextRandomInt(-3, 3) + FastMath.nextRandomFloat(), 0, FastMath.nextRandomInt(-3, 3) + FastMath.nextRandomFloat()));
           people[i] = new Person(mesh, rootNode, simp, new Vector3f(0,0,0));
           peopleThreads[i] = new Thread(people[i]);
        }
        neighbourList = new ArrayList<LinkedList<Person>>(popNumber);
        //refreshNeighbourList();
    }

   
    public void evacuate() {
        for (int i = 0; i < peopleThreads.length; i++) {
            peopleThreads[i].run();
        }

        /* for(int i = 0; i < peopleThreads.length; i++){
         try{
         peopleThreads[i].join();
         }catch(InterruptedException e){return ;}
         }*/
    }

    public void play() {
        for (int i = 0; i < people.length; i++) {
            people[i].play();
        }
    }

    public void update(float tpf) {
    }

    public void run() {
    }
}
