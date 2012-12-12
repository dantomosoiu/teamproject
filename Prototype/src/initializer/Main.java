package initializer;

import com.bulletphysics.collision.shapes.TriangleShape;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.VertexBuffer;
import jme3tools.navmesh.NavMesh;
import jme3tools.navmesh.util.NavMeshGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import population.Person;
import population.Population;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

	boolean wireframe;
	Material mat;
	boolean solid = false;
	public static NavMesh shipNM;
	public static Node root;
	public static Population population;
	private boolean update;
	public static int updateTimes = 0;
	private Geometry geom;

	public static Geometry[] AgentGeometries;

	public static void main(String[] args) {
		Main app = new Main();
                app.setShowSettings(false);
		app.start();

	}

	public void go() {

		this.population = new Population(rootNode, shipNM, this);
		int populationSize = 1;
		AgentGeometries = new Geometry[populationSize];
		population.populate(populationSize);

		//population.evacuate();
	}


	@Override
	public void simpleInitApp() {

		flyCam.setDragToRotate(true);
		
		inputManager.addMapping("evac", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addListener(actionListener, "evac");
		
		inputManager.addMapping("play", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addListener(actionListener, "play");

		Logger.getLogger("").setLevel(Level.SEVERE);
		//cam.setLocation(new Vector3f(-4f, 1.1f, 4f));
		//cam.lookAtDirection(new Vector3f(0f, 1f, 0f), new Vector3f(0f, 1f, 0f));
		Spatial ship = assetManager.loadModel("Models/Room/Room.j3o");

		Node node = (Node) ship;
		Node chil1 = (Node) node.getChildren().get(2);
		Geometry chil = (Geometry) chil1.getChildren().get(0);



		Mesh shipMesh = chil.getMesh();
		shipNM = new NavMesh();

		NavMeshGenerator generator = new NavMeshGenerator();

		Mesh optimisedMesh = generator.optimize(shipMesh);

		shipNM.loadFromMesh(optimisedMesh);

		Node navMeshHolder = new Node();
		rootNode.attachChild(navMeshHolder);



		node = rootNode;


		for (int i = 0; i < shipNM.getNumCells(); i++) {
			TriangleShape tr;
			Vector3f v0 = shipNM.getCell(i).getVertex(0);
			Vector3f v1 = shipNM.getCell(i).getVertex(1);
			Vector3f v2 = shipNM.getCell(i).getVertex(2);

			Mesh lineMesh = new Mesh();
			lineMesh.setMode(Mesh.Mode.Lines);
			lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v0.x, v0.y, v0.z, v1.x, v1.y, v1.z});
			lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
			lineMesh.updateBound();
			lineMesh.updateCounts();
			Geometry lineGeometry = new Geometry("line", lineMesh);
			Material lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			lineGeometry.setMaterial(lineMaterial);
			navMeshHolder.attachChild(lineGeometry);

			lineMesh = new Mesh();
			lineMesh.setMode(Mesh.Mode.Lines);
			lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v0.x, v0.y, v0.z, v2.x, v2.y, v2.z});
			lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
			lineMesh.updateBound();
			lineMesh.updateCounts();
			lineGeometry = new Geometry("line", lineMesh);
			lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			lineGeometry.setMaterial(lineMaterial);
			navMeshHolder.attachChild(lineGeometry);

			lineMesh = new Mesh();
			lineMesh.setMode(Mesh.Mode.Lines);
			lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v2.x, v2.y, v2.z, v1.x, v1.y, v1.z});
			lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
			lineMesh.updateBound();
			lineMesh.updateCounts();
			lineGeometry = new Geometry("line", lineMesh);
			lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			lineGeometry.setMaterial(lineMaterial);
			navMeshHolder.attachChild(lineGeometry);



			guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
			BitmapText helloText = new BitmapText(guiFont, false);
			helloText.setSize(0.05f);
			helloText.setText(v0.toString());
			helloText.setLocalTranslation(v0.x, v0.y, v0.z);
			rootNode.attachChild(helloText);

			helloText = new BitmapText(guiFont, false);
			helloText.setSize(0.05f);
			helloText.setText(v1.toString());
			helloText.setLocalTranslation(v1.x, v1.y, v1.z);
			rootNode.attachChild(helloText);

			helloText = new BitmapText(guiFont, false);
			helloText.setSize(0.05f);
			helloText.setText(v2.toString());
			helloText.setLocalTranslation(v2.x, v2.y, v2.z);
			rootNode.attachChild(helloText);

		}

		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText helloText = new BitmapText(guiFont, false);
		helloText.setSize(0.1f);
		helloText.setText("ORIGIN");
		helloText.setLocalTranslation(0,0,0);
		rootNode.attachChild(helloText);



		/*
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("WAITED!!!!!!!!!!!!!!!!!!!!");
        Thread.yield();
		 */

		this.population = new Population(rootNode, shipNM, this);
		int populationSize = 10;
		AgentGeometries = new Geometry[populationSize];
		population.populate(populationSize);

		update = true;
		//population.evacuate();



		//System.out.println(chil1.getChildren().size());
		//mat = chil.getMaterial();
		//rootNode.attachChild(chil);


		//TESTBOX
/*
		Box box = new Box(Vector3f.ZERO, 0.2f, 0.2f, 0.2f);
		geom = new Geometry("Box", box);
		Material mat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Red);
		geom.setMaterial(mat);
		rootNode.attachChild(geom);

		start = geom.getLocalTranslation();
*/
	}

	/*
	private boolean shouldMove = false;
	private float count = 0;
	private Vector3f start;
	private Vector3f end = new Vector3f (0f, 1f, 0f);
	 */
	@Override
	public void simpleUpdate(float tpf) {
		//Random r = new Random();
		//updatePosition(new Vector3f(r.nextInt(shipNM.getNumCells()), r.nextInt(shipNM.getNumCells()), r.nextInt(shipNM.getNumCells())));
		//System.out.println("UPDATE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n\n\n");
/*
		if (shouldMove) {
			count += tpf/1000; //this will make it take 10 seconds to reach the end, from the the start
			Vector3f newLocation = FastMath.interpolateLinear(count, start, end);
			geom.setLocalTranslation(newLocation);
			if(count>= 1) //then you reached the end
				shouldMove= false;
		}
*/
	}

//	public void updatePosition(Vector3f pos) {
//
//
//		if (update && updateTimes != 9) {
//			Person p = new Person(shipNM, rootNode, this);
//			p.getGeom().setLocalTranslation(pos);
//			p.getGeom().getMaterial();
//			updateTimes++;
//		}
//	}

	@Override
	public void simpleRender(RenderManager rm) {
		//TODO: add render code
		//rm.setTimer(getTimer());
	}
	private ActionListener actionListener = new ActionListener() {
		@Override
		public void onAction(String name, boolean pressed, float tpf) {
			// toggle wireframe
			if (name.equals("evac") && !pressed) {
				//shouldMove = true;
				population.evacuate();
				//for (int i = 0; i < 100; i++)
				//	System.out.println("doing something");
			}
			// else ... other input tests.
			if (name.equals("play") && !pressed) {
				population.play();
			}
		}
	};
}

