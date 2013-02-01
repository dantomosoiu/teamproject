package initializer;

import com.bulletphysics.collision.shapes.TriangleShape;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import jme3tools.navmesh.NavMesh;
import jme3tools.navmesh.util.NavMeshGenerator;
import population.Population;

/**
 *
 */
public class Main extends SimpleApplication {
    
    //Settings
    public static Settings set;
    
    //boolean solid = false;
    static int m = 0, s = 0;
    public static NavMesh shipNM;
    public static Node root;
    public static Population population;
    public static int updateTimes = 0;
    public static Geometry[] AgentGeometries;
    private static mainWindow window;
    private static JmeCanvasContext ctx;
    private static int populationSize;
    private Node navMeshHolder;

    private static void createComponents() {
        //Creates a JFrame for the main window
        window = new mainWindow();

        // Creates JMonkey the canvas 
        Main canvasApplication = new Main();
        canvasApplication.setShowSettings(false);
        canvasApplication.createCanvas(); // create canvas!
        ctx = (JmeCanvasContext) canvasApplication.getContext();
        ctx.setSystemListener(canvasApplication);
        Dimension dim = new Dimension(800, 580);
        ctx.getCanvas().setSize(dim);
        //Sets the canvas in the panel on the JFrame
        window.setContainer(ctx.getCanvas());
        //Makes the window visible
        window.setVisible(true);
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //Load Settings
                set = new Settings();
                set.loadFromFile();
                //Creates components of GUI
                createComponents();
                window.update(set);
            }
        });

    }

    public static Population getPopulation() {
        return population;
    }

    @Override
    public void simpleInitApp() {
        
        this.setDisplayStatView(false);
        this.setDisplayFps(set.isShowFPS());
        
        //Sets cam mouse controls
        flyCam.setDragToRotate(true);

        inputManager.addMapping("play", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "play");

        this.flyCam.setMoveSpeed(set.getCamSpeed());

        Logger.getLogger("").setLevel(Level.SEVERE);

        Spatial ship = assetManager.loadModel(set.getModelLocation());

        Node node = (Node) ship;
        Node chil1 = (Node) node.getChildren().get(0);
        Geometry chil = (Geometry) chil1.getChildren().get(0);

        Mesh shipMesh = chil.getMesh();
        shipNM = new NavMesh();

        NavMeshGenerator generator = new NavMeshGenerator();

        Mesh optimisedMesh = generator.optimize(shipMesh);

        shipNM.loadFromMesh(optimisedMesh);

        if (set.isShowNavMesh()) {
            drawNavmesh();
        }

        guiFont = assetManager.loadFont(set.getGuiFont());
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.1f);
        helloText.setText("ORIGIN");
        helloText.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(helloText);




        try {
            population = new Population(rootNode, shipNM, this);
        } catch (Exception e) {
        }

        populationSize = set.getPopulationNumber();

        AgentGeometries = new Geometry[populationSize];
        population.populate(populationSize);

        window.loadDone();

    }
    
    public static void updateStatus() {
        window.update(set);
    }
    
    public void removeNavmesh() {
        rootNode.detachChild(navMeshHolder);
    }
    public void drawNavmesh() {
        navMeshHolder = new Node();
        rootNode.attachChild(navMeshHolder);
        //draws navMesh
        for (int i = 0; i < shipNM.getNumCells(); i++) {
            Vector3f v0 = shipNM.getCell(i).getVertex(0);
            Vector3f v1 = shipNM.getCell(i).getVertex(1);
            Vector3f v2 = shipNM.getCell(i).getVertex(2);
            Vector3f[][] vs = {{v0,v1}, {v2,v0}, {v1,v2}};
            
            guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

            for (Vector3f[] vt : vs) {
                Mesh lineMesh = new Mesh();
                lineMesh.setMode(Mesh.Mode.Lines);
                lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{vt[0].x, vt[0].y, vt[0].z, vt[1].x, vt[1].y, vt[1].z});
                lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
                lineMesh.updateBound();
                lineMesh.updateCounts();
                Geometry lineGeometry = new Geometry("line", lineMesh);
                Material lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                lineMaterial.setColor("Color", ColorRGBA.White);
                lineGeometry.setMaterial(lineMaterial);
                navMeshHolder.attachChild(lineGeometry);
                
                BitmapText helloText = new BitmapText(guiFont, false);
                helloText.setSize(0.05f);
                helloText.setText(vt[0].toString());
                helloText.setLocalTranslation(vt[0].x, vt[0].y, vt[0].z);
                navMeshHolder.attachChild(helloText);
            }
        }
    }

    private static ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("play") && !pressed) {
                population.evacuate();
                population.play();
                //timer.start();
            }
        }
    };
}
