/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import jme3tools.navmesh.NavMesh;

/**
 *
 * @author michael
 */
public class Person implements Runnable{
    /**Owning SimpleApplication*/
    private SimpleApplication simp;
    /**Owning Node*/
    private com.jme3.scene.Node rootNode;
    
    /**Provides visual representation of a person*/
    private Spatial person;
    private Material mat1;
    
    /**NavMesh on which routes are planned*/
    private NavMesh navmesh;
    
    /**Path for movement of visual representation*/
    private MotionPath motionpath;
    /**Control for movement of visual representation*/
    private MotionEvent motionControl;
    
    /**Attributes of a person*/
    private Vector3f initialLocation;
    private float speed;
    private float stress;
    
   
    public Person( SimpleApplication simp,com.jme3.scene.Node rootNode,NavMesh navmesh, Vector3f initialLocation, float speed){
        this.simp = simp;
        this.rootNode = rootNode;
        this.navmesh = navmesh;
        this.initialLocation = initialLocation;
        this.speed = speed;
        //Create Visual Representation
        person = simp.getAssetManager().loadModel("Models/Ninja/Ninja.mesh.xml");
        Material mat_default = new Material(simp.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
        person.setMaterial(mat_default);
        person.scale(0.002f);
        person.setLocalTranslation(initialLocation);
        mat1 = new Material(simp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        rootNode.attachChild(person);
        
        //setup motion control
        
    }
    
      public float calculateMotionTime(float speedUnitsPerSecond, MotionPath motionpath){
        float distance = motionpath.getLength(); //get the distance of the motionpath
        float time = distance / speedUnitsPerSecond; // t=d/v ... Produces time which movement should occur for
        return time;
    }
      
      @Override
      public void run(){
           Vector3f goal = BehaviourModel.nearestExit(initialLocation);
           PersonNavmeshRoutePlanner routeplan = new PersonNavmeshRoutePlanner(navmesh,initialLocation,goal);
       
            if(!routeplan.computePath(goal)){
               return;
            }
            while(!routeplan.isAtGoalWaypoint()){
                 Vector3f oldPosition = new Vector3f(routeplan.getCurrentPos3d());
                 routeplan.planPathToWaypoint(Population.DISTANCEBETWEENMOTIONWAYPOINTS);
                 Vector3f newPosition = new Vector3f(routeplan.getCurrentPos3d());
                 
                Mesh lineMesh = new Mesh();
                lineMesh.setMode(Mesh.Mode.Lines);
                lineMesh.setLineWidth(5f);
                lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{oldPosition.x, oldPosition.y, oldPosition.z, newPosition.x, newPosition.y, newPosition.z});
                lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
                lineMesh.updateBound();
                lineMesh.updateCounts();
                Geometry lineGeometry = new Geometry("line", lineMesh);
            
                lineGeometry.setMaterial(mat1);
                rootNode.attachChild(lineGeometry);
            }
            motionpath = routeplan.getMotionpath();
            motionpath.addListener(new PersonMovementListener(this));
            float time = calculateMotionTime(speed,motionpath);
           
             motionControl = new MotionEvent(person, motionpath);
            motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
           motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
           motionControl.setInitialDuration(time);
           motionControl.setSpeed(1);//note this is not the same as the movement speed for the person
  
           
      }
      
      public void play() {
    	motionControl.play();
    }

    public Spatial getPerson() {
        return person;
    }
      
}
