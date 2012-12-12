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
import com.jme3.scene.shape.Torus;
import jme3tools.navmesh.*;
import jme3tools.navmesh.Path.Waypoint;
import net.java.games.input.Component;

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
    private float surfaceArea = 1f;

    public float getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(float surfaceArea) {
        this.surfaceArea = surfaceArea;
    }
    
    /**
     * Returns a PersonBoundary representing the opening/closing position 
     * of the surface covered by the person on the specified axis.
     * @param axis Short representing the index of the axis (0=x, 1=y, 2=z).
     * @param opening The position out of which to get the boundary for 
     * (for example, for x axis, true is left and false is right).
     * @return resulted PersonBoundary instance.
     */
    public PersonBoundary getBoundary(short axis, boolean opening){
        PersonBoundary boundary = new PersonBoundary();
        boundary.person = this;
        boundary.opening = opening;
        boundary.axis = axis;
        switch(axis){
            case 0:
                if(opening){
                    boundary.position = this.getPosition().x - surfaceArea/2;
                } else {
                    boundary.position = this.getPosition().x + surfaceArea/2;
                }
                break;
            case 1:
                if(opening){
                    boundary.position = this.getPosition().y - surfaceArea/2;
                } else {
                    boundary.position = this.getPosition().y + surfaceArea/2;
                }
                break;
            case 2:
                if(opening){
                    boundary.position = this.getPosition().z - surfaceArea/2;
                } else {
                    boundary.position = this.getPosition().z + surfaceArea/2;
                }
                break;
        }
        return boundary;
    }

    //Collision Variables
    private PersonCollisionControl collisionControl;
    
    public Person(NavMesh navMesh, com.jme3.scene.Node rootNode, SimpleApplication simp, Vector3f initialLocation) {
        super(navMesh);
        this.rootNode = rootNode;

        this.initialLocation = initialLocation;
        
        this.setPosition(initialLocation);
        
        path = new MotionPath();
        path.addWayPoint(initialLocation);
        
        //Box box = new Box(Vector3f.ZERO, 0.2f, 0f, 0.2f);
        //Torus box = new Torus(3, 2, 1, 1);
        Sphere box = new Sphere(32, 32, 0.5f);
        geom = new Geometry("Box", box);
        mat1 = new Material(simp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        geom.setMaterial(mat1);
        geom.setLocalTranslation(initialLocation);
        //geom.scale(0.1f); //reduce size of shape
        
        collisionControl = new PersonCollisionControl(new BoxCollisionShape(new Vector3f(0.1f,0f,0.1f)));
        geom.addControl(collisionControl);
        
        
        rootNode.attachChild(geom);
       
        this.simp = simp;
        
        
        motionControl = new MotionEvent(geom, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
      
        //path.enableDebugShape(simp.getAssetManager(), rootNode);
      
    }

    public float calculateMotionTime(float speedUnitsPerSecond, MotionPath path){
        float distance = path.getLength();
        float time = distance / speedUnitsPerSecond;
        return time;
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
            mat1.setColor("Color", ColorRGBA.randomColor());
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
        float speed = 1f; //in units per second
        float time = this.calculateMotionTime(speed, path);
        motionControl.setInitialDuration(time);
        motionControl.setSpeed(1);
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
        path.setCurveTension(0.0f);
    }

    public void play() {
    	motionControl.play();
    }
    
    
}

