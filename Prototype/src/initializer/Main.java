package initializer;

import com.bulletphysics.collision.shapes.TriangleShape;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import jme3tools.navmesh.NavMesh;
import jme3tools.navmesh.util.NavMeshGenerator;
import population.Population;


/**
 * coco test2
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
        
        private static mainWindow window;
        private static JmeCanvasContext ctx;
        
        
        private static void createComponents(){
            
              AppSettings settings = new AppSettings(true);
              settings.setWidth(800);
              settings.setHeight(600);
              
              window = new mainWindow();

              // create new canvas application
              Main canvasApplication = new Main();
              canvasApplication.setShowSettings(false);
              canvasApplication.setSettings(settings);
              canvasApplication.createCanvas(); // create canvas!
              ctx = (JmeCanvasContext) canvasApplication.getContext();
              ctx.setSystemListener(canvasApplication);
              Dimension dim = new Dimension(800, 580);
              ctx.getCanvas().setSize(dim);
              
              GridBagConstraints c = new GridBagConstraints();
              c.fill = GridBagConstraints.HORIZONTAL;
              c.weightx = c.weighty = 1.0;
              c.gridwidth = 2;
              c.insets = new Insets(10, 0, 0, 8);
              c.gridx = c.gridy = 0;
              
              window.setContainer(ctx.getCanvas(), c);
              
              window.setVisible(true);
        }

	public static void main(String[] args) {
            
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              // create new JME appsettings
              createComponents();
              
            }
          });
            

	}

	public void go() {
                try{
                    population = new Population(rootNode, shipNM, this);
                }catch(Exception e){}
                int populationSize = 1;
		AgentGeometries = new Geometry[populationSize];
		population.populate(populationSize);

		//population.evacuate();
	}


	@Override
	public void simpleInitApp() {

		flyCam.setDragToRotate(true);
                this.setDisplayStatView(false); // to hide the statistics 
		
		inputManager.addMapping("evac", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addListener(actionListener, "evac");
		
		inputManager.addMapping("play", new KeyTrigger(KeyInput.KEY_P));
		inputManager.addListener(actionListener, "play");

                this.flyCam.setMoveSpeed(this.flyCam.getMoveSpeed()*10);
                
		Logger.getLogger("").setLevel(Level.SEVERE);
		//cam.setLocation(new Vector3f(-4f, 1.1f, 4f));
		//cam.lookAtDirection(new Vector3f(0f, 1f, 0f), new Vector3f(0f, 1f, 0f));
		Spatial ship = assetManager.loadModel("Models/FlatModel/FlatModel.j3o");

		Node node = (Node) ship;
		Node chil1 = (Node) node.getChildren().get(0);
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

                window.loadDone();

                try{
                    this.population = new Population(rootNode, shipNM, this);
                }catch(Exception e){}
		int populationSize = 20;
		
		AgentGeometries = new Geometry[populationSize];
		population.populate(populationSize);

		update = true;
		
	}

	
	@Override
	public void simpleUpdate(float tpf) {
		
	}


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

