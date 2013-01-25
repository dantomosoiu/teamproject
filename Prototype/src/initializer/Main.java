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
import java.awt.event.ActionEvent;
import population.Person;
import population.Population;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JDialog;

import java.awt.GridLayout;
import java.awt.Container;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.BorderFactory; 
import javax.swing.border.TitledBorder;

import java.awt.Color;


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
        
        private static JFrame window;
        private static JButton settingBut;
        private static JButton helpBut;
        private static JButton playPauseBut;
        private static JButton stopBut;
//        private static JComboBox speedCombo;
        private static JSlider speedSlider;
        private static JPanel leftPanel;
        private static JmeCanvasContext ctx;
        private static JTextField peopleKilled, peopleEvacuated, timeElapsed;
        
        
        private static void createComponents(){
            
            AppSettings settings = new AppSettings(true);
              settings.setWidth(800);
              settings.setHeight(600);
              
              window = new JFrame("Evacuation Simulator -- Team L");
              window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              
              
              // create new canvas application
              Main canvasApplication = new Main();
              canvasApplication.setShowSettings(false);
              canvasApplication.setSettings(settings);
              canvasApplication.createCanvas(); // create canvas!
              ctx = (JmeCanvasContext) canvasApplication.getContext();
              ctx.setSystemListener(canvasApplication);
              Dimension dim = new Dimension(800, 580);
              ctx.getCanvas().setSize(dim);
              leftPanel = new JPanel();
              leftPanel.add(ctx.getCanvas());
              leftPanel.setBorder(BorderFactory.createMatteBorder(
                                    1, 1, 1, 1, Color.BLACK));

              JPanel container = new JPanel(new GridBagLayout());
              JPanel bottomPanel = new JPanel(new FlowLayout());
              JPanel rightPanel = new JPanel(new GridBagLayout());
              GridBagConstraints c = new GridBagConstraints();
              c.fill = GridBagConstraints.HORIZONTAL;
              c.weightx = 1.0;
              c.weighty = 1.0;
              c.gridwidth = 2;
              c.insets = new Insets(10, 0, 0, 8);
              c.gridx = 0;
              c.gridy = 0;
              
              
              final ImageIcon playIcon = new ImageIcon("images/play.jpg", "Play");
              final ImageIcon pauseIcon = new ImageIcon("images/pause.jpg", "Pause");
              final ImageIcon settingIcon = new ImageIcon("images/setting.jpg", "Setting");
              final ImageIcon helpIcon = new ImageIcon("images/help.jpg", "Help");
              final ImageIcon stopIcon = new ImageIcon("images/stop.jpg", "Stop");
              
              settingBut = new JButton(settingIcon);
              settingBut.setToolTipText("Setting");
              settingBut.addActionListener(new java.awt.event.ActionListener(){
                  public void actionPerformed(ActionEvent e){
                      java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            new SettingDlg();
                        }
                      });
                  }
                  
              });
//              speedCombo = new JComboBox(new Object[]{"X1", "X2", "X4", "X8"});
              speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
              speedSlider.setMajorTickSpacing(10);
//              speedSlider.setMinorTickSpacing(1);
              speedSlider.setPaintTicks(true);
              speedSlider.setPaintLabels(false);
              playPauseBut = new JButton(playIcon);
              playPauseBut.setToolTipText("Play");
              playPauseBut.addActionListener(new java.awt.event.ActionListener(){
                  public void actionPerformed(ActionEvent e){
                      if (playPauseBut.getIcon().toString().equals("Play")){
                          playPauseBut.setIcon(pauseIcon);
                          playPauseBut.setToolTipText("Pause");
                      }else{
                          playPauseBut.setIcon(playIcon);
                          playPauseBut.setToolTipText("Play");
                      }
                  }
              });
              stopBut = new JButton(stopIcon);
              stopBut.setToolTipText("Stop");
              helpBut = new JButton(helpIcon);
              helpBut.setToolTipText("Help");

              bottomPanel.add(playPauseBut);
              bottomPanel.add(stopBut);
              bottomPanel.add(speedSlider);
              
              JPanel statusPanel = new JPanel(new GridLayout(3, 1));
              TitledBorder title = BorderFactory.createTitledBorder("System Status");
              statusPanel.setBorder(title);
              timeElapsed = new JTextField(3);
              timeElapsed.setBorder(BorderFactory.createEtchedBorder());
              timeElapsed.setEnabled(false);
              peopleKilled = new JTextField(3);
              peopleKilled.setBorder(BorderFactory.createEtchedBorder());
              peopleKilled.setEnabled(false);
              peopleEvacuated = new JTextField(3);
              peopleEvacuated.setEnabled(false);
              peopleEvacuated.setBorder(BorderFactory.createEtchedBorder());
              JPanel top = new JPanel(new FlowLayout());
              top.add(timeElapsed);
              top.add(new JLabel("time elapsed        "));
              JPanel middle = new JPanel(new FlowLayout());
              middle.add(peopleKilled);
              middle.add(new JLabel("people killed         "));
              JPanel bottom = new JPanel(new FlowLayout());
              bottom.add(peopleEvacuated);
              bottom.add(new JLabel("people evacuated"));
              statusPanel.add(top);
              statusPanel.add(middle);
              statusPanel.add(bottom);
              
              JLabel label = new JLabel("Simulation Evacuator", JLabel.CENTER);
              label.setFont(new Font("DejaVu Sans", Font.BOLD, 35));
              
              container.add(label, c);
              c.gridwidth = 1;
              c.fill = GridBagConstraints.LINE_START;

              c.gridy = 1;
              c.gridheight = 4;
              c.weightx = 1.0;
              container.add(leftPanel, c);
              c.fill = GridBagConstraints.RELATIVE;
              c.weightx = 0.0;
              c.gridx = 1;
              c.gridheight = 1;
              c.weighty = 0.0;
              container.add(statusPanel, c);
              c.ipady = GridBagConstraints.NONE;
              c.gridy = 2;
              c.insets = new Insets(10, 10, 10, 10);
              container.add(settingBut, c);
              c.gridy = 3;
              container.add(helpBut, c);
              c.insets = new Insets(0, 0, 0, 0);

              c.gridy = 5;
              c.gridx = 0;
              container.add(bottomPanel, c);
              
              window.add(container);
              window.setResizable(false);
              window.pack();
              // Display Swing window including JME canvas!
              window.setVisible(true);
              window.setLocationRelativeTo(null);
              
        }

	public static void main(String[] args) {
            
            java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              // create new JME appsettings
              createComponents();
            }
          });
            
//		Main app = new Main();
//                app.setShowSettings(false);
//		app.start();

	}

	public void go() {
                try{
                    this.population = new Population(rootNode, shipNM, this);
                }catch(Exception e){}
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
                try{
                    this.population = new Population(rootNode, shipNM, this);
                }catch(Exception e){}
		
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

class SettingDlg extends JDialog{
    
    private JTextField noPeople;
    private JComboBox maxSpeedCombo;
    private JComboBox minSpeedCombo;
    private Container container;
    
    public SettingDlg(){
        
        container = this.getContentPane();
        container.setLayout(new GridLayout(3, 1));
        noPeople = new JTextField(5);
        maxSpeedCombo = new JComboBox(new Object[]{"X1", "X2", "X3"});
        minSpeedCombo = new JComboBox(new Object[]{"X1", "X2", "X3"});
        JLabel label = new JLabel("Number of People: ");
        JPanel top = new JPanel(new FlowLayout());
        top.add(label);
        top.add(noPeople);
        label = new JLabel("  Max Full Speed: ");
        JPanel middle = new JPanel(new FlowLayout());
        middle.add(label);
        middle.add(maxSpeedCombo);
        label = new JLabel("  Min Full Speed: ");
        JPanel bottom = new JPanel(new FlowLayout());
        bottom.add(label);
        bottom.add(minSpeedCombo);
        container.add(top);
        container.add(middle);
        container.add(bottom);
        this.setLocationRelativeTo(null);
        this.setTitle("Settings");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    }
    
}














