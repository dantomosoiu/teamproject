/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.population;

import EvacSim.EvacSim;
import EvacSim.goal.Goal;
import EvacSim.jme3tools.navmesh.NavMesh;
import Init.Settings.PersonCategory;
import Init.Settings.Settings;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;

/**
 *
 * @author michael
 */
public class Person implements Runnable{

    /**
     *
     */
    public final int WAYPOINTSBETWEENDECISIONS = 50;

    /**
     * Provides visual representation of a person
    */
    
    //Handles for pausing the process

    private boolean fin;
    private boolean start;
    
    /**
     * Provides visual representation of a person
     */
    private Spatial person;
    private Material mat1;

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
    private PersonNavmeshRoutePlanner routeplan;
    private PersonMovementListener listener;
    
    EvacSim evs;
    Node routeGeometryHolder;
    Settings settings;
    NavMesh navmesh;

    /**
     *
     * @param evs
     * @param initialLocation
     * @param category
     * @param p
     */
    public Person(EvacSim evs, Vector3f initialLocation,PersonCategory category, Population p) {
        this.initialLocation = initialLocation;
        this.speed = category.generateSpeed();
        this.stress = category.generateStress();
        
        fin = false;
        this.evs = evs;
        settings = Settings.get();
        navmesh = settings.getNavMesh();
        routeGeometryHolder = new Node();
        this.listener = new PersonMovementListener(this,WAYPOINTSBETWEENDECISIONS);

        currentGoal = BehaviourModel.randomExit();
        routeplan = new PersonNavmeshRoutePlanner(navmesh, initialLocation, currentGoal.getLocation());
        
        //Calculate Path
        if (!routeplan.computePath(currentGoal.getLocation())) {
            System.err.println("Could not Compute path to exit");
            return;
        }
        Mesh lineMesh;
        if (settings.getPrintEv()) System.err.println("Starting from " + initialLocation.toString());
        while (!routeplan.isAtGoalWaypoint()) {
            Vector3f oldPosition = new Vector3f(routeplan.getCurrentPos3d());
            if (settings.getPrintEv()) System.err.println("Currently at " + oldPosition.toString());
            routeplan.planPathToWaypoint(Population.DISTANCEBETWEENMOTIONWAYPOINTS);
            Vector3f newPosition = new Vector3f(routeplan.getCurrentPos3d());
            if (settings.getPrintEv()) System.err.println("Added " + newPosition.toString());

            if (settings.showRoutes()) {
                lineMesh = new Mesh();
                lineMesh.setMode(Mesh.Mode.Lines);
                lineMesh.setLineWidth(5f);
                lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{oldPosition.x, oldPosition.y, oldPosition.z, newPosition.x, newPosition.y, newPosition.z});
                lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
                lineMesh.updateBound();
                lineMesh.updateCounts();
                Geometry lineGeometry = new Geometry("line", lineMesh);

                lineGeometry.setMaterial(mat1);
                routeGeometryHolder.attachChild(lineGeometry);
            }
        }

        motionpath = routeplan.getMotionpath();
        motionpath.addListener(new PersonMovementListener(this,WAYPOINTSBETWEENDECISIONS));
        
        //Create Visual Representation
        
        person = evs.getAssetManager().loadModel(settings.getPersonModelLocation());
        Material mat_default = new Material(evs.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
        person.setMaterial(mat_default);
        person.scale(0.002f);        
        
        person.setLocalTranslation(routeplan.getMotionpath().getWayPoint(0));
        mat1 = new Material(evs.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", settings.getCol(category.getColor()));
        evs.attachChild(person);

        //setup motion control

    }
    
    /**
     *
     * @return
     */
    public boolean isFin() {
        return fin;
    }
    /**
     *
     * @return
     */
    public boolean isStart() {
        return start;
    }

    /**Calculates the time a Person should take to traverse a path at a given speed
     * @param speedUnitsPerSecond The Person's movement speed in metres per second
     * @param motionpath The MotionPath over which the person moves
     * @return  
     */
    public float calculateMotionTime(float speedUnitsPerSecond, MotionPath motionpath) {
        float distance = motionpath.getLength(); //get the distance of the motionpath
        float time = distance / speedUnitsPerSecond; // t=d/v ... Produces time which movement should occur for
        return time;
    }

    /**
     *
     */
    @Override
    public void run() {
        start = true;
        currentGoal = BehaviourModel.randomExit();
        if(!this.buildMotionPath(currentGoal)){
            System.err.println("Invalid path specified.");
            return;
        }
        this.buildMotionControl();
        motionpath = routeplan.getMotionpath();
        motionpath.addListener(listener);

        routeGeometryHolder.setMaterial(mat1);
        evs.attachChild(routeGeometryHolder);
        
        if (settings.getPrintEv()) System.err.println("Finished motion path!" + (motionpath.isCycle() ? " (cycled)" : ""));
        

       
    }
    
    /**
     *
     */
    public void pause() {
        motionControl.pause();
    }
    
    
    /**
     *
     */
    public void play() {
        if (motionControl != null) {
            motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
            motionControl.setRotation(new Quaternion(new float[]{0, 135, 0}));
            motionControl.play();
        }
    }
    
    /**
     *
     */
    public void stop(){
        motionControl.stop();
    }

    /**
     *
     * @return
     */
    public Spatial getPerson() {
        return person;
    }

    /**
     *
     * @return
     */
    public float getSpeed() {
        return speed;
    }

    /**
     *
     * @return
     */
    public float getStress() {
        return stress;
    }

    /**
     *
     * @return
     */
    public Goal getCurrentGoal() {
        return currentGoal;
    }

    /**
     *
     * @param p
     */
    public void setMotionPath(MotionPath p){
        this.motionpath = p;
    }
    
    /**Builds the motion control for the Person, associating a listener and setting the control parameters.
     *
     */
    public void buildMotionControl(){
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
                settings.incNumEvac();
                evs.detachChild(person);
            }
        });
    }
    
    /**
     *Draws a visible line between two points; used in route generation.
     * @param oldPosition
     * @param newPosition
     */
    public void drawLine(Vector3f oldPosition, Vector3f newPosition){
            Mesh lineMesh = new Mesh();
            lineMesh.setMode(Mesh.Mode.Lines);
            lineMesh.setLineWidth(5f);
            lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{oldPosition.x, oldPosition.y, oldPosition.z, newPosition.x, newPosition.y, newPosition.z});
            lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
            lineMesh.updateBound();
            lineMesh.updateCounts();
            Geometry lineGeometry = new Geometry("line", lineMesh);
            lineGeometry.setMaterial(mat1);
            evs.attachChild(lineGeometry);
    }
    
    /**Builds MotionPath from current location to goal
     *
     * @param goal
     * @return
     */
    public boolean buildMotionPath(Goal goal){
        routeplan = new PersonNavmeshRoutePlanner(navmesh, initialLocation, currentGoal.getLocation());
        if (!routeplan.computePath(currentGoal.getLocation())) {
            if (settings.getPrintEv()) System.err.println("Could not Compute path to exit");
            return false;
        }
        if (settings.getPrintEv()) System.err.println("Starting from " + initialLocation.toString());
        while (!routeplan.isAtGoalWaypoint()) {
            Vector3f oldPosition = new Vector3f(routeplan.getCurrentPos3d());
            if (settings.getPrintEv()) System.err.println("Currently at " + oldPosition.toString());
            routeplan.planPathToWaypoint(Population.DISTANCEBETWEENMOTIONWAYPOINTS);
            Vector3f newPosition = new Vector3f(routeplan.getCurrentPos3d());
            if (settings.getPrintEv()) System.err.println("Added " + newPosition.toString());
            if(settings.showRoutes()){
                //drawLine(oldPosition,newPosition);
            }
        }
        
        return true;
    }
    /**
     *
     */
    public void pausePerson(){
       
    }
    /**
     *
     * @param g
     */
    protected synchronized void changeGoal(Goal g){
            this.currentGoal = g;
    }
}
