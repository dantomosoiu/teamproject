package jme3tools.navmesh;

import jme3tools.navmesh.Path.Waypoint;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class NavMeshPathfinder {

    private NavMesh navMesh;
    private Path path = new Path();

    public Path getPath() {
        return path;
    }
    private float entityRadius;

    private Vector2f currentPos = new Vector2f();
    private Vector3f currentPos3d = new Vector3f();
    private Cell currentCell;

    private Vector2f goalPos;
    private Vector3f goalPos3d;
    private Cell goalCell;

    private Waypoint nextWaypoint;

    public NavMeshPathfinder(NavMesh navMesh){
        this.navMesh = navMesh;
    }

    public Vector3f getPosition() {
        return currentPos3d;
    }

    public void setPosition(Vector3f position) {
        this.currentPos3d.set(position);
        this.currentPos.set(currentPos3d.x, currentPos3d.z);
    }

    public float getEntityRadius() {
        return entityRadius;
    }

    public void setEntityRadius(float entityRadius) {
        this.entityRadius = entityRadius;
    }

    public synchronized Vector3f warp(Vector3f newPos){
        Vector3f newPos2d = new Vector3f(newPos.x, 0, newPos.z);
        currentCell = navMesh.findClosestCell(newPos2d);
        currentPos3d.set(navMesh.snapPointToCell(currentCell, newPos2d));
        currentPos3d.setY(newPos.getY());
        currentPos.set(currentPos3d.getX(), currentPos3d.getZ());
        return currentPos3d;
    }

    public synchronized boolean computePath(Vector3f goal){
        goalPos3d = goal;
        goalPos = new Vector2f(goalPos3d.getX(), goalPos3d.getZ());
        Vector3f goalPos2d = new Vector3f(goalPos.getX(), 0, goalPos.getY());
        goalCell = navMesh.findClosestCell(goalPos2d);
        boolean result = navMesh.buildNavigationPath(path, currentCell, currentPos3d, goalCell, goalPos3d, entityRadius);
        if (!result){
            goalPos = null;
            goalCell = null;
            return false;
        }
        nextWaypoint = path.getFirst();
        
        return true;
    }

    public synchronized void clearPath(){
        path.clear();
        goalPos = null;
        goalCell = null;
        nextWaypoint = null;
    }

    public synchronized Vector3f getWaypointPosition(){
        return nextWaypoint.getPosition();
    }

    public synchronized Vector3f getDirectionToWaypoint(){
        Vector3f waypt = nextWaypoint.getPosition();
        return waypt.subtract(currentPos3d).normalizeLocal();
    }

    public synchronized float getDistanceToWaypoint(){
        return currentPos3d.distance(nextWaypoint.getPosition());
    }
    
    public synchronized Vector3f onMove(Vector3f moveVec){
        if (moveVec.equals(Vector3f.ZERO))
            return currentPos3d;

        Vector3f newPos2d = new Vector3f(currentPos3d);
        newPos2d.addLocal(moveVec);
        newPos2d.setY(0);

        Vector3f currentPos2d = new Vector3f(currentPos3d);
        currentPos2d.setY(0);

        Cell nextCell = navMesh.resolveMotionOnMesh(currentPos2d, currentCell, newPos2d, newPos2d);
        currentCell = nextCell;
        newPos2d.setY(currentPos3d.getY());
        return newPos2d;
    }

    public synchronized boolean isAtGoalWaypoint(){
        return nextWaypoint == path.getLast();
    }

    public synchronized void gotoToNextWaypoint(){
        nextWaypoint = path.getFurthestVisibleWayPoint(nextWaypoint);
        Vector3f waypt = nextWaypoint.getPosition();
        currentPos3d.setX(waypt.getX());
        currentPos3d.setZ(waypt.getZ());
        currentPos.set(waypt.getX(), waypt.getZ());
        currentCell = nextWaypoint.getCell();
    }

    //EXTENDED_MICHAEL : Added is line of sight method
    public synchronized boolean isInLineOfSight(Vector3f point){
        return navMesh.isInLineOfSight(currentCell,currentPos3d, point);
    }
    //EXTENDED-MICHAEL : Added getters setters for variables

    public NavMesh getNavMesh() {
        return navMesh;
    }

    public synchronized Vector2f getCurrentPos() {
        return currentPos;
    }

    public synchronized void setCurrentPos(Vector2f currentPos) {
        this.currentPos = currentPos;
    }

    public synchronized Vector3f getCurrentPos3d() {
        return currentPos3d;
    }

    public synchronized void setCurrentPos3d(Vector3f currentPos3d) {
        this.currentPos3d = currentPos3d;
    }

    public synchronized Cell getCurrentCell() {
        return currentCell;
    }

    public synchronized void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public synchronized Vector2f getGoalPos() {
        return goalPos;
    }

    public synchronized void setGoalPos(Vector2f goalPos) {
        this.goalPos = goalPos;
    }

    public synchronized Vector3f getGoalPos3d() {
        return goalPos3d;
    }

    public synchronized void setGoalPos3d(Vector3f goalPos3d) {
        this.goalPos3d = goalPos3d;
    }

    public synchronized Cell getGoalCell() {
        return goalCell;
    }

    public synchronized void setGoalCell(Cell goalCell) {
        this.goalCell = goalCell;
    }

    public synchronized Waypoint getNextWaypoint() {
        return nextWaypoint;
    }

    public synchronized void setNextWaypoint(Waypoint nextWaypoint) {
        this.nextWaypoint = nextWaypoint;
    }
    
}
