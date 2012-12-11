package population;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import jme3tools.navmesh.*;
import jme3tools.navmesh.Path.Waypoint;

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

    //Collision Variables
    private PersonCollisionControl collisionControl;
    
    public Person(NavMesh navMesh, com.jme3.scene.Node rootNode, SimpleApplication simp, Vector3f initialLocation) {
        super(navMesh);
        this.rootNode = rootNode;

        this.initialLocation = initialLocation;
        
        path = new MotionPath();
        path.addWayPoint(initialLocation);
        
        Box box = new Box(Vector3f.ZERO, 0.2f, 0f, 0.2f);
        geom = new Geometry("Box", box);
        mat1 = new Material(simp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat1);
        geom.setLocalTranslation(initialLocation);
        
        collisionControl = new PersonCollisionControl(new BoxCollisionShape(new Vector3f(0.1f,0f,0.1f)));
        geom.addControl(collisionControl);
        
        
        rootNode.attachChild(geom);
       
        this.simp = simp;
        
        
        motionControl = new MotionEvent(geom, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setInitialDuration(10f);
        motionControl.setSpeed(0.5f);
        
       
       
    }

	
	@Override
    public void run() {
        /*Procedure is as follows
         * WHILE not exited
         * scan()
         * decide()
         * move()
         */

        this.warp(initialLocation);

        if (!computePath(Population.GOAL)) {
            System.out.println("GOAL CANNOT BE REACHED"); // path cant be found
        }

      
        while (!isAtGoalWaypoint()) {
            Vector3f oldPosition = new Vector3f(this.getPosition());
            
            this.gotoToNextWaypoint(0.1f);
            
            Vector3f newPosition = new Vector3f(this.getPosition());
          

            
            Sphere circle = new Sphere(30, 30, 0.05f);
            Geometry sphereGeom = new Geometry("Sphere", circle);
            mat1.setColor("Color", ColorRGBA.Green);
            sphereGeom.setMaterial(mat1);
            sphereGeom.move(oldPosition);
            rootNode.attachChild(sphereGeom);
            
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
    }
    
    /**Moves person to next available waypoint in the navmesh path.
     * @param moveDistance scalar indicating the distance a person should move per step, in graph units.
     **/
    public void gotoToNextWaypoint(float moveDistance) {
        //find furthest visible navmesh waypoint from current position and set this to the next waypoint
    	setNextWaypoint(this.getPath().getFurthestVisibleWayPoint(getNextWaypoint())); 
    	Vector3f unitVector; 
        //while the move distance is less than the remaining distance to the next navmesh waypoint
    	while (moveDistance < getDistanceToWaypoint()) { 
    		unitVector = getDirectionToWaypoint(); //obtain unit vector directed from current pos to waypoint
                //update the logical representation of position first
    		warp(this.getPosition().add(unitVector.mult(moveDistance))); //scale this vector by the move distance and move along this vector
    		//now update the visual representation by adding a waypoint to the motion control path
                path.addWayPoint(this.getPosition());
    		
    	
        }
    	warp(this.getNextWaypoint().getPosition());
    	path.addWayPoint(this.getPosition());
    }

    public void play() {
    	motionControl.play();
    }
}

