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
 * @author michael, tony, dan
 */
public class Population{


	public static Vector3f GOAL = new Vector3f(1f,0f,1f);
	private SimpleApplication simp;
	private Person people[];
	private Thread peopleThreads[];
	private com.jme3.scene.Node rootNode;
	private NavMesh mesh;

	public Population(com.jme3.scene.Node rootNode, NavMesh mesh, SimpleApplication simp){
		this.mesh = mesh;
		this.simp = simp;
		this.rootNode = rootNode;
	}
	public void populate(int popNumber){
		people = new Person[popNumber];
		peopleThreads = new Thread[popNumber];
		for(int i = 0; i<popNumber; i++){
			people[i] = new Person(mesh, rootNode, simp);
			peopleThreads[i] = new Thread(people[i]);
		}
	}

	public void evacuate(){
		for(int i = 0; i < peopleThreads.length; i++){
			peopleThreads[i].run();
		}

		/* for(int i = 0; i < peopleThreads.length; i++){
            try{
               peopleThreads[i].join();
            }catch(InterruptedException e){return ;}
        }*/
	}

	public void play() {
		for(int i = 0; i < people.length; i++) {
			people[i].play();
		}
	}
	
	public void update(float tpf){
		//for(int i = 0; i < peopleThreads.length; i++){
		//    people[i].update(tpf);
		//}
	}

}
