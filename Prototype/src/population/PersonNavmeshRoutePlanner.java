package population;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import jme3tools.navmesh.*;

/**
 *
 * @author michael
 */
public class PersonNavmeshRoutePlanner extends NavMeshPathfinder {
    private Vector3f initialLocation;
    private Vector3f goalLocation;
    MotionPath motionpath;
    
    public PersonNavmeshRoutePlanner(NavMesh navmesh, Vector3f location){
        super(navmesh);
        this.initialLocation = location;
        warp(initialLocation);
        this.goalLocation = null;
    }
    public PersonNavmeshRoutePlanner(NavMesh navmesh, Vector3f initialLocation, Vector3f goalLocation){
        super(navmesh);
        this.initialLocation = initialLocation;
        this.goalLocation = goalLocation;
        warp(initialLocation);
        motionpath = new MotionPath();
    }
    
    public MotionPath createMovementPath(float distanceBetweenWaypoints){
        motionpath = new MotionPath();
        motionpath.addWayPoint(this.getPosition());
        if(!computePath(goalLocation)){
            return null;
        }
        while(!isAtGoalWaypoint()){
            planPathToWaypoint(distanceBetweenWaypoints);
        }
        motionpath.setCurveTension(0.0f);
        return motionpath;
    }
    
     public void planPathToWaypoint(float moveDistance) {
        //find furthest visible navmesh waypoint from current position and set this to the next waypoint
    	setNextWaypoint(this.getPath().getFurthestVisibleWayPoint(getNextWaypoint())); 
    	Vector3f unitVector; 
        //while the move distance is less than the remaining distance to the next navmesh waypoint
    	while (moveDistance < getDistanceToWaypoint()) { 
                //System.err.println(getDistanceToWaypoint());
    		unitVector = getDirectionToWaypoint(); //obtain unit vector directed from current pos to waypoint
                //update the logical representation of position first
    		warp(this.getPosition().add(unitVector.mult(moveDistance))); //scale this vector by the move distance and move along this vector
    		//now update the visual representation by adding a waypoint to the motion control path
                for (int i = 0; i< motionpath.getNbWayPoints(); i++){
                    if(motionpath.getWayPoint(i).equals(this.getPosition())){
                        motionpath.setCycle(true);
                        break;
                    }
                }
                if(motionpath.isCycle()){
                    System.err.println("Cycle detected!");
                    return;
                }
                motionpath.addWayPoint(this.getPosition());
        }
        warp(this.getNextWaypoint().getPosition());
    	motionpath.addWayPoint(this.getPosition());
    }

    public MotionPath getMotionpath() {
        return motionpath;
    }
     
    @Override
    public boolean isInLineOfSight(Vector3f point){
        return super.isInLineOfSight(point);
    }
     
    public NavMesh getNavmesh(){
        return super.getNavMesh();
    }
   
}
