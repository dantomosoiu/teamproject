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
import com.jme3.scene.shape.Sphere;
import jme3tools.navmesh.*;

/**
 * Base class representing a single person within the simulation's population.
 */
public class Person extends NavMeshPathfinder implements Runnable {
    /**Provides visual representation of a person*/
    private Geometry geom;
    private SimpleApplication simp;
    private com.jme3.scene.Node rootNode;
    /**Spawn location*/
    private Vector3f initialLocation;
    private Material mat1;
    /**Index of current navmesh waypoint on the path*/
    private int positionOnPath = 0; //stores index of waypoint in current path
    /**Path for movement of visual representation*/
    private MotionPath path;
    /**Control for movement of visual representation*/
    private MotionEvent motionControl;
  

    public Person(NavMesh navMesh, com.jme3.scene.Node rootNode, SimpleApplication simp, Vector3f initialLocation) {
        super(navMesh);
        this.rootNode = rootNode;
        this.simp = simp;
        this.initialLocation = initialLocation;
        this.warp(initialLocation);
        
        //Initialise spatial 
        Sphere box = new Sphere(32, 32, 0.05f);
        geom = new Geometry("Box", box);
        mat1 = new Material(simp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        geom.setMaterial(mat1);
        geom.setLocalTranslation(initialLocation);
        rootNode.attachChild(geom);
     }

    public float calculateMotionTime(float speedUnitsPerSecond, MotionPath path){
        float distance = path.getLength();
        float time = distance / speedUnitsPerSecond;
        return time;
    }
	
  
    public void moveTo(Vector3f goalPosition, float distanceBetweenWaypoints){
        if (!computePath(goalPosition)) {
            System.out.println("GOAL CANNOT BE REACHED"); // path cant be found
        }
        System.out.println("START: " + this.getCurrentPos3d());
        /*We can automatically go to the last waypoint: since this is a goal we already know it is in view*/
        setNextWaypoint(this.getPath().getLast()); 
        path = new MotionPath(); //create new motionpath to this point
        path.addWayPoint(this.getPosition());
        Vector3f unitVector; 
        unitVector = getDirectionToWaypoint(); //obtain unit vector directed from current pos to waypoint
        Vector3f targetPosition = this.getCurrentPos3d();
        Vector3f moveVector = unitVector.mult(distanceBetweenWaypoints);
        float distanceToGoal = this.getCurrentPos3d().distance(goalPosition);
        while(distanceToGoal > distanceBetweenWaypoints){
                targetPosition = targetPosition.add(moveVector); //calculate position to move to 
                path.addWayPoint(targetPosition);
                System.out.println("WAYPOINT: "+ targetPosition);
                distanceToGoal -= distanceBetweenWaypoints;
        }
        path.addWayPoint(goalPosition);
       
        motionControl = new MotionEvent(geom, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setTime(calculateMotionTime(0.0001f,path));
        path.addListener( new PersonMovementListener(this));
    }
	@Override
    public void run() {
        moveTo(new Vector3f(2,0,-1),0.1f);
        //motionControl.play();
    }
    

    public void play() {
    	motionControl.play();
    }
    
    
}

