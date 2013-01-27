
    
    public Person(NavMesh navMesh, com.jme3.scene.Node rootNode1, SimpleApplication simp, Vector3f initialLocation) {
        //Initialize Variables
        super(navMesh);
        this.rootNode = rootNode1;
        this.simp = simp;
        this.initialLocation = initialLocation;
        this.setPosition(initialLocation);
        
        //Create new motion pat for visual representation
        path = new MotionPath();
        path.addWayPoint(initialLocation);

        //Create Visual Representation
        person = simp.getAssetManager().loadModel("Models/Ninja/Ninja.mesh.xml");
        Material mat_default = new Material( 
                    simp.getAssetManager(), "Common/MatDefs/Misc/ShowNormals.j3md");
        person.setMaterial(mat_default);
        person.scale(0.002f);
        person.setLocalTranslation(initialLocation);
        mat1 = new Material(simp.getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        rootNode.attachChild(person);
       
        //Build motionControl for visual representation 
        motionControl = new MotionEvent(person, path);
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Y));
//        motionControl.addListener(new CinematicEventListener(){
//            public void onPlay(CinematicEvent cinematic){}
//            public void onPause(CinematicEvent cinematic){}
//            public void onStop(CinematicEvent cinematic){
//                rootNode.detachChild(person);
//            }
//        });
        
        //Code for navmesh/visualisation syncronization of position
       // path.addListener(new PersonMovementListener(this));
    }

    /**Calculates to time in seconds that an object must take to move through
     * a motionPath at a given speed in graph units per second
     * @param speedUnitsPerSecond magnitude of distance by which the object should move down the motionPath, in units per second
     * @param path MotionPath to be move along
     * */
    public float calculateMotionTime(float speedUnitsPerSecond, MotionPath path){
        float distance = path.getLength(); //get the distance of the path
        float time = distance / speedUnitsPerSecond; // t=d/v ... Produces time which movement should occur for
        return time;
    }
	
    @Override
    public void run() {
        this.warp(initialLocation); //place person on the navmesh at initial location
       
        Vector3f goal = BehaviourModel.nearestExit(initialLocation);
      
        System.out.println("GOAL:"+goal);
        if (!computePath(goal) ){ //compute the path
            System.out.println("GOAL CANNOT BE REACHED"); // path cant be found
            return;
        }
        
        System.out.println("Starting to calculate path towards goal (" + goal.toString() + ")");
        while (!isAtGoalWaypoint()) { //while the person has nopt reached their goal
            Vector3f oldPosition = new Vector3f(this.getPosition());
            System.out.println("currently at " + oldPosition.toString());
            this.gotoToNextWaypoint(0.1f); // move toward the next waypoint 
            
            Vector3f newPosition = new Vector3f(this.getPosition());
            
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
        System.out.println("Finished");
        float speed = 1f; //in units per second
        float time = this.calculateMotionTime(speed, path);
       
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
    	path.addWayPoint(this.getPosition());
    }

    public void play() {
    	motionControl.play();
    }
    
    
}
