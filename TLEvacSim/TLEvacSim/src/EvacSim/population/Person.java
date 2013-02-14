/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.population;

import EvacSim.EvacSim;
import EvacSim.goal.Goal;
import EvacSim.jme3tools.navmesh.NavMesh;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
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

/**
 *
 * @author michael
 */
public class Person implements Runnable{

    /**
     * Owning Node
     */
    private com.jme3.scene.Node rootNode;
    /**
     * Provides visual representation of a person
     */
    private boolean fin;
    private boolean start;
    
    private Spatial person;
    private Material mat1;
    /**
     * NavMesh on which routes are planned
     */
    private NavMesh navmesh;
    /**
     * Path for movement of visual representation
     */
    private MotionPath motionpath;
    /**
     * Control for movement of visual representation
     */
    private MotionEvent motionControl;
    /**
     * Attributes of a person
     */
    private Vector3f initialLocation;
    private Goal currentGoal;
    private float speed;
    private float stress;

    public Person(SimpleApplication simp, com.jme3.scene.Node rootNode, NavMesh navmesh, Vector3f initialLocation, float speed, Population p) {
        this.rootNode = rootNode;
        this.navmesh = navmesh;
        this.initialLocation = initialLocation;
        this.speed = speed;
        fin = false;

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
    
    public boolean isFin() {
        return fin;
    }
    public boolean isStart() {
        return start;
    }

    /**Calculates the time a Person should take to traverse a path at a given speed
     * @param speedUnitsPerSecond The Person's movement speed in metres per second
     * @param motionpath The MotionPath over which the person moves
     */
    public float calculateMotionTime(float speedUnitsPerSecond, MotionPath motionpath) {
        float distance = motionpath.getLength(); //get the distance of the motionpath
        float time = distance / speedUnitsPerSecond; // t=d/v ... Produces time which movement should occur for
        return time;
    }

    @Override
    public void run() {
        start = true;
        currentGoal = BehaviourModel.nearestExit(initialLocation);
        PersonNavmeshRoutePlanner routeplan = new PersonNavmeshRoutePlanner(navmesh, initialLocation, currentGoal.getLocation());

        if (!routeplan.computePath(currentGoal.getLocation())) {
            System.err.println("Could not Compute path to exit");
            return;
        }
        System.err.println("Starting from " + initialLocation.toString());
        while (!routeplan.isAtGoalWaypoint()) {
            Vector3f oldPosition = new Vector3f(routeplan.getCurrentPos3d());
            System.err.println("Currently at " + oldPosition.toString());
            routeplan.planPathToWaypoint(Population.DISTANCEBETWEENMOTIONWAYPOINTS);
            Vector3f newPosition = new Vector3f(routeplan.getCurrentPos3d());
            System.err.println("Added " + newPosition.toString());

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
        System.err.println("Finished motion path!" + (motionpath.isCycle() ? " (cycled)" : ""));


        float time = calculateMotionTime(speed, motionpath);

        motionControl = new MotionEvent(person, motionpath);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
        motionControl.setInitialDuration(time);
        motionControl.setSpeed(1);//note this is not the same as the movement speed for the person
        motionControl.addListener(new CinematicEventListener(){
            public void onPlay(CinematicEvent e){}
            public void onPause(CinematicEvent e){}
            public void onStop(CinematicEvent e){
                fin = true;
                EvacSim.updateStatus();
                EvacSim.getPopulation().updateNumberOfPeople();
            }
        });
    }
    
    
    public void play() {
        if (motionControl != null) {
            motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
            motionControl.setRotation(new Quaternion(new float[]{0, 135, 0}));
            motionControl.play();
        }
    }

    public Spatial getPerson() {
        return person;
    }

    public float getSpeed() {
        return speed;
    }

    public float getStress() {
        return stress;
    }

    public NavMesh getNavmesh() {
        return navmesh;
    }

    public Goal getCurrentGoal() {
        return currentGoal;
    }

}
