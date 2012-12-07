package population;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import jme3tools.navmesh.*;
import jme3tools.navmesh.NavMeshPathfinder;
import jme3tools.navmesh.Path.Waypoint;


/**
 * Base class representing a single person within the simulation's population.
 */
public class Person extends NavMeshPathfinder implements Runnable {

    /**
     * The maximum number of grid nodes this person can move per turn
     */
    private int speed;
    private Geometry geom;
    private SimpleApplication simp;
    private com.jme3.scene.Node rootNode;
    private Vector3f initialLocation;
    private Material mat1;
    private int positionOnPath = 0; //stores index of waypoint in current path 

    public Person(NavMesh navMesh, com.jme3.scene.Node rootNode, SimpleApplication simp) {
        super(navMesh);
        this.rootNode = rootNode;

        initialLocation = new Vector3f(-4f, 0, 4);

        Box box = new Box(initialLocation, 0.2f, 0f, 0.2f);
        geom = new Geometry("Box", box);
        mat1 = new Material(simp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat1);
        rootNode.attachChild(geom);
    }

    /*Locomotion Methods*/
    private void moveForward() {
    }

    private void pivotLeft() {
    }

    private void pivotRight() {
    }

    private void sideStepLeft() {
    }

    private void sideStepRight() {
    }

    /*Scan forward area for goal nodes*/
    private void scan() {
    }

    ;
	
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
        
        Path p = this.getPath();
        for(Path.Waypoint w : p){
            System.out.println(w.toString() + " - ");
        }

        if (isAtGoalWaypoint()) {
            System.out.println("AT GOAL");
        }
        while (!isAtGoalWaypoint()) {
            System.out.println("CURRENT LOCATION:" + this.getPosition().toString() + "\n\n");
            Vector3f oldPosition = new Vector3f(this.getPosition());
            
            this.gotoToNextWaypoint();
            geom.move(new Vector3f(this.getPosition().x - oldPosition.x, this.getPosition().y - oldPosition.y, this.getPosition().z - oldPosition.z));
            
            Vector3f newPosition = new Vector3f(this.getPosition());
            
            Sphere circle = new Sphere(30, 30, 0.05f);
            Geometry sphereGeom = new Geometry("Sphere", circle);
            mat1.setColor("Color", ColorRGBA.Red);
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
           
    

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Geometry getGeom() {
        return geom;
    }
}
