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

import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;
import java.util.EventObject;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import java.awt.Insets;
import java.awt.Font;
import javax.swing.BorderFactory; 
import javax.swing.border.TitledBorder;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import com.jme3.input.event.KeyInputEvent;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * coco test2
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

	boolean wireframe;
	Material mat;
	boolean solid = false;
        static int m = 0, s = 0;
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
        private static JButton populateBut;
        private static JPanel container, leftPanel, loadingPanel, bottomPanel;
        private static JmeCanvasContext ctx;
        private static JTextField numPeople, peopleEvacuated, timeElapsed;
        private static GridBagConstraints c;
        private static Timer timer;
        
        private static int populationSize; 
        //private static KeyInputEvent k = new KeyInputEvent(KeyInput.KEY_E, 'E', true, false);
        
        
        private static void createComponents(){
            
            AppSettings settings = new AppSettings(true);
              settings.setWidth(800);
              settings.setHeight(600);
              
              window = new JFrame("Evacuation Simulator -- Team L");
              window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              window.addWindowListener(new WindowAdapter(){
                  public void windowClosing(WindowEvent e){
                      timer.stop();
                  }
              });
              
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

              container = new JPanel(new GridBagLayout());
              bottomPanel = new JPanel(new FlowLayout());
              c = new GridBagConstraints();
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
              playPauseBut = new JButton(playIcon);
              playPauseBut.setToolTipText("Evacuate");
              playPauseBut.addActionListener(new java.awt.event.ActionListener(){
                  public void actionPerformed(ActionEvent e){
                      if (playPauseBut.getIcon().toString().equals("Play")){
                          population.play();
                          playPauseBut.setIcon(pauseIcon);
                          playPauseBut.setToolTipText("Pause");
                      }else{
                          playPauseBut.setIcon(playIcon);
                          playPauseBut.setToolTipText("Evacuate");
                      }
                  }
              });
              stopBut = new JButton(stopIcon);
              stopBut.setToolTipText("Stop");
              helpBut = new JButton(helpIcon);
              helpBut.setToolTipText("Help");
              
              populateBut = new JButton(new ImageIcon("images/populate.jpg", "Populate"));
              populateBut.setToolTipText("Populate");
              populateBut.addActionListener(new java.awt.event.ActionListener(){
                  public void actionPerformed(ActionEvent e){
                      population.evacuate();
//                      simp.getInputManager().simulateEvent(new KeyInputEvent(KeyInput.KEY_E, 'E', true, true));
//                      evacuateBut.setEnabled(true);
//                      container.remove(bottomPanel);
//                      container.add(loadingPanel, c);
//                      container.updateUI();
                  }
              });
              
              bottomPanel.add(populateBut);
              bottomPanel.add(playPauseBut);
              bottomPanel.add(stopBut);
              
              loadingPanel = new JPanel(new GridLayout());
              JProgressBar progressBar = new JProgressBar();
              progressBar.setValue(25);
              progressBar.setStringPainted(true);
              progressBar.setBorder(BorderFactory.createTitledBorder("Calculating paths..."));
              loadingPanel.add(progressBar);
              
              timer = new Timer(1000, new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    s++;
                    if (s == 60){
                        m++;
                        s = 0;
                    }
                    timeElapsed.setText(String.format("%02d", m) + ":" + String.format("%02d", s));
                    if (population.isDone()) {
                        timer.stop();
                    }
                    
                }    
              });
              
              JPanel statusPanel = new JPanel(new GridLayout(3, 1));
              TitledBorder title = BorderFactory.createTitledBorder("System Status");
              statusPanel.setBorder(title);
              timeElapsed = new JTextField(4);
              timeElapsed.setForeground(Color.BLUE);
              timeElapsed.setHorizontalAlignment(JTextField.RIGHT);
              
              timeElapsed.setBorder(BorderFactory.createEtchedBorder());
              timeElapsed.setEditable(false);
              numPeople = new JTextField(4);

              numPeople.setForeground(Color.BLUE);
              numPeople.setHorizontalAlignment(JTextField.RIGHT);
              numPeople.setBorder(BorderFactory.createEtchedBorder());
              numPeople.setEditable(false);
              peopleEvacuated = new JTextField(4);
              
              peopleEvacuated.setForeground(Color.BLUE);
              peopleEvacuated.setHorizontalAlignment(JTextField.RIGHT);
              peopleEvacuated.setEditable(false);
              peopleEvacuated.setBorder(BorderFactory.createEtchedBorder());
              JPanel top = new JPanel(new FlowLayout());
              top.add(timeElapsed);
              top.add(new JLabel("time elapsed        "));
              JPanel middle = new JPanel(new FlowLayout());
              middle.add(numPeople);
              middle.add(new JLabel("number of people"));
              JPanel bottom = new JPanel(new FlowLayout());
              bottom.add(peopleEvacuated);
              bottom.add(new JLabel("people evacuated"));
              statusPanel.add(top);
              statusPanel.add(middle);
              statusPanel.add(bottom);
              
              JPanel logoPanel = new JPanel(new FlowLayout());
              JLabel logo = new JLabel(new ImageIcon("images/ship.jpg", "Logo"));
              JLabel label = new JLabel("Evacuation Simulator", JLabel.CENTER);
              label.setFont(new Font("DejaVu Sans", Font.BOLD, 35));
              logoPanel.add(logo);
              logoPanel.add(label);
              container.add(logoPanel, c);
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
              
              JPanel controlPanel = new JPanel(new FlowLayout());
              c.gridy = 4;
              
              title = BorderFactory.createTitledBorder("System Status");
              controlPanel.setBorder(title);
              JButton test = new JButton("test");
//              controlPanel.add(new JButton("test"));
//              controlPanel.add(test);
//              controlPanel.add(new JButton("XXX"));
              container.add(controlPanel, c);

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
        
        public static Population getPopulation(){
            return population;
        }


	@Override
	public void simpleInitApp() {

		flyCam.setDragToRotate(true);
                
                
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
                
		populationSize = 20;
		numPeople.setText("" + populationSize);
                timeElapsed.setText("00:00");
                peopleEvacuated.setText("" + 0);
                
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
        
        public static void updateStatus(){
            numPeople.setText("" + (populationSize - population.getNumberOfEvacuee() - 1));
            peopleEvacuated.setText("" + (population.getNumberOfEvacuee() + 1));
        }

	@Override
	public void simpleRender(RenderManager rm) {
		//TODO: add render code
		//rm.setTimer(getTimer());
	}
	private static ActionListener actionListener = new ActionListener() {
           
		@Override
		public void onAction(String name, boolean pressed, float tpf) {
			// toggle wireframe
			if (name.equals("evac") && !pressed) {
				//shouldMove = true;
				population.evacuate();
                                
//                                container.remove(bottomPanel);
//                                container.add(loadingPanel, c);
//                                container.updateUI();
				//for (int i = 0; i < 100; i++)
				//	System.out.println("doing something");
			}
			// else ... other input tests.
			if (name.equals("play") && !pressed) {
				population.play();
                                timer.start();
//                                container.remove(loadingPanel);
//                                container.add(bottomPanel, c);
//                                container.updateUI();
			}
		}
	};
}

class SettingDlg extends JDialog{
    
    static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    static final int FPS_INIT = 15;
    
    private Container container;
    
    // populationPanel components
    private JTabbedPane tabbedPane;
    private JTextField txtCategory;
    private JComboBox categoryCombo;
    private JButton addBut, removeBut;
    private JSpinner speedSpinner, stressSpinner, spaceSpinner, prioritySpinner;
    private JPanel generalPanel, populationPanel, distributionPanel;
    
    //distributionPanel components
    private JSlider sldDisabled, sldInfant, sldAthlete;
    private JTable table;
    private DistributionModel model;
    
    private GridBagConstraints c;
    
    
    
    public SettingDlg(){
        
        container = this.getContentPane();
        container.setLayout(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        c = new GridBagConstraints();
        
        createGeneralPanel();
        createPopulationPanel();
        createDistributionPanel();

        container.add(tabbedPane);
//        this.setLocationRelativeTo(null);
        this.setTitle("Advanced Settings");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        this.pack();
        this.setSize(500, 210);
        this.setResizable(false);
        this.setVisible(true);

    }
    
    private void createDistributionPanel(){
        
        distributionPanel = new JPanel(new GridLayout());
        
        model = new DistributionModel();
        table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        
        DistributionCellRenderer renderer = new DistributionCellRenderer();
        DistributionCellEditor editor = new DistributionCellEditor();
        TableColumn tableColumn = table.getColumnModel().getColumn(1);
        tableColumn.setCellRenderer(renderer);
        tableColumn.setCellEditor(editor);
        
        

        JScrollPane scrollPane = new JScrollPane(table);
        distributionPanel.add(scrollPane);
        
        tabbedPane.addTab("Distribution", distributionPanel);
    }
    
    private void createGeneralPanel(){
        generalPanel = new JPanel(new GridBagLayout());
        generalPanel.add(new JButton("test"));
        tabbedPane.addTab("General", generalPanel);
    }
    
    private void createPopulationPanel(){
        populationPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(4, 0, 0, 8);
        c.anchor = c.LINE_END;
        JLabel label = new JLabel("Category: ");
        populationPanel.add(label, c);
        c.gridx = 1;
        categoryCombo = new JComboBox(new Object[]{"Atheletes", "test2asdfasfsdfewr", "test3"});
        populationPanel.add(categoryCombo, c);
        
        c.gridx = 2;
        removeBut = new JButton("Remove");
        populationPanel.add(removeBut, c);
        
        c.gridx = 0;
        c.gridy = 1;
        label = new JLabel("Speed: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        float value = 0.0f;
        float min = 0.0f;
        float max = 10.0f;
        float step = 0.1f;
        SpinnerNumberModel speedModel = new SpinnerNumberModel(value, min, max, step);
        speedSpinner = new JSpinner(speedModel);
        populationPanel.add(speedSpinner, c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("Initial Stress: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel stressModel = new SpinnerNumberModel(value, min, 100.0f, step);
        stressSpinner = new JSpinner(stressModel);
        populationPanel.add(stressSpinner, c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("Personal Space(m.): ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel spaceModel = new SpinnerNumberModel(value, min, 5.0f, step);
        spaceSpinner = new JSpinner(spaceModel);
        populationPanel.add(spaceSpinner, c);
        
        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("Social Priority: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel priorityModel = new SpinnerNumberModel(value, min, 1.0f, step);
        prioritySpinner = new JSpinner(priorityModel);
        populationPanel.add(prioritySpinner, c);
        
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(10, 0, 0, 0);
        txtCategory = new JTextField(10);
        populationPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridx = 1;
        populationPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridx = 2;
        populationPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);     
                
        c.insets = new Insets(4, 0, 0, 8);
        c.gridx = 0;
        c.gridy = 6;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("New Category: ");
        populationPanel.add(label, c);
        
        
        c.gridx = 1;
        c.gridy = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        populationPanel.add(txtCategory, c);
        
        c.gridx = 2;
        addBut = new JButton("Add");
        populationPanel.add(addBut, c);

        tabbedPane.addTab("Population", populationPanel);
 
    }
    
}

class DistributionCellEditor implements TableCellEditor {
    
    static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    
    private JSlider slider;
        
	
    public DistributionCellEditor() {
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(FPS_MIN);
        slider.setMaximum(FPS_MAX);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    public Object getCellEditorValue() {
        return slider.getValue();
    }
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
    public boolean stopCellEditing() {
        return true;
    }
    public void cancelCellEditing() {

    }
    public void addCellEditorListener(CellEditorListener l) {

    }
    public void removeCellEditorListener(CellEditorListener l) {
    }
    public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(FPS_MIN);
        slider.setMaximum(FPS_MAX);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue((Integer)value);
        
        return slider;
    }
}

class DistributionCellRenderer implements TableCellRenderer {

    static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    
    public JSlider slider;
    


    public DistributionCellRenderer() {
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(FPS_MIN);
        slider.setMaximum(FPS_MAX);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean selected, boolean focused, int row, int column) {
        slider.setValue((Integer)value);
        
        return slider;
    }

}

class Data{
    
    protected String category;
    protected int value;
    
    public Data(String cat, int val){
        this.category = cat;
        this.value = val;
    }
}

class DistributionModel extends AbstractTableModel {
	public ArrayList<Data> data;
	private static final String COL_NAMES[] = {"Category", "Value"};

	public DistributionModel() {
            data = new ArrayList<Data>();
            setData();
	}
	
	public void setData(){
            data.add(new Data("Disabled", 20));
            data.add(new Data("Infant", 10));
            data.add(new Data("Old", 40));
            data.add(new Data("Atheletes", 90));
            data.add(new Data("Teenager", 90));
	}

	public int getColumnCount() {
            return COL_NAMES.length;
	}

	public String getColumnName(int col) {
            return COL_NAMES[col];
	}

	public int getRowCount() {
            return data.size();
	}

	public Object getValueAt(int nRow, int nCol) {
            Data row = (Data) data.get(nRow);
            switch(nCol){
                case 0:
                    return row.category;
                case 1:
                    return row.value;
            }
            return "";
	}

	public boolean isCellEditable(int row, int col) {
            if (col == 1){
		return true;
            }
            return false;
	}

	public void setValueAt(Object value, int nRow, int nCol) {
            if (nRow < 0 || nRow >= getRowCount() || value == null)
                return;
            Data row = (Data) data.get(nRow);
            Data newData = (Data)value;
            row.category = (String)newData.category;
            row.value = (Integer)newData.value;
            fireTableCellUpdated(nRow, nCol);
	}

}












