package EvacSim;

import EvacSim.jme3tools.navmesh.NavMesh;
import EvacSim.jme3tools.navmesh.util.NavMeshGenerator;
import EvacSim.population.Population;
import GUI.Components.SidePanel;
import Init.Settings.CamLoc;
import Init.Settings.Settings;
import com.jme3.app.SimpleApplication;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for Simulator. Superclass contains the update loop.
 * 
 * @author Hector Grebbell
 * @author Dan Tomosoiu
 */
public class EvacSim extends SimpleApplication {
    
    private static Settings appSettings;//Settings
    private static Population population;//Current Population
    private BitmapText origin;//Holder for origin text
    private boolean running;//Indicates whether the simulation is playing or not
    private String buttonCam;//Stores any movement instructions
    private BitmapText routing;//Holder for routing display text
    private Node hullNode;//Holder Node for display model

    /**
     *Initialises the Simulator
     * 
     * @param set Settings to use
     */
    public EvacSim(Settings set) {
        appSettings = set;
        running = false;
    }
    
    //Adds any camera movement to the update loop
    @Override
    public void update() {
        super.update();
        if (buttonCam != null) flyCam.onAnalog(buttonCam, 0.02f, 0);
    }

    /**
     * Called on simulator startup. Finishes Initialisation.
     */
    @Override
    public void simpleInitApp() {
        buttonCam = null;//The camera should initially be stationary
        this.setPauseOnLostFocus(false);//Since the simulator is to be placed in a Swing GUI we need it to keep playing when buttons are pressed
        this.setDisplayStatView(false); //Hides debug info
        this.setDisplayFps(appSettings.isShowFPS()); //Depending on settings hides FPS
        flyCam.setDragToRotate(true); //Sets cam mouse controls
        routing = new BitmapText(guiFont, false);   //Text for displaying a notification when routing takes place
        routing.setSize(guiFont.getCharSet().getRenderedSize()*1.2f);      // font size
        routing.setColor(ColorRGBA.White);                             // font color
        routing.setText("Routing. Please Wait...");            // the text
        int add = 0;//offset for routing text
        if (appSettings.isShowFPS()) add = 30;
        routing.setLocalTranslation(0, add + routing.getLineHeight(), 0); // position
        flyCam.setMoveSpeed(appSettings.getCamSpeed()); //Sets Cam speed

        Logger.getLogger("").setLevel(Level.SEVERE);//Reduces unneeded debug info

        if ((appSettings.getNavMesh()) == null) {//If the navmesh isnt saved draw it!
            drawNM();
        }
        else {//otherwise load it.
            appSettings.setNMHolder((Node)assetManager.loadModel("Settings/navMeshNode.j3o"));
        }
        
        //Load in the display model
        hullNode = (Node) assetManager.loadModel(appSettings.getModelLocation() + "ShowModel.j3o");
        
        //show what the settings tell us to
        showNavMesh();
        
        //draw origin text
        guiFont = assetManager.loadFont(appSettings.getGuiFont());
        origin = new BitmapText(guiFont, false);
        origin.setSize(0.1f);
        origin.setText("ORIGIN");
        origin.setLocalTranslation(0, 0, 0);
        if (appSettings.showOrigin()) {
            rootNode.attachChild(origin);
        }

        //create the population
        population = new Population(this);
        population.populate();

    }
    
    /**
     *Attaches spatials to the rootnode in a threadsafe manner.
     * 
     * @param N The spatial to attach to the rootnode
     */
    public void attachChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                rootNode.attachChild(N);
                return null;
            }
        });
    }
    /**
     *Attaches spatials to the GUI node in a threadsafe manner.
     * 
     * @param N The spatial to attach to the guiNode
     */
    public void attachGUIChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                guiNode.attachChild(N);
                return null;
            }
        });
    }
    
    /**
     * Sets up camera movement
     * @param s the movement instruction
     */
    public void moveCamC(String s) {
        if (s == null) buttonCam = null;
        else if (buttonCam == null) buttonCam = s;
        else buttonCam = null;
    }

    /**
     *Detaches spatials from the root node in a threadsafe manner.
     * 
     * @param N The spatial to detach to the rootNode
     */
    public void detachChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                rootNode.detachChild(N);
                return null;
            }
        });
    }
    /**
     *Detaches spatials from the gui node in a threadsafe manner.
     * 
     * @param N The spatial to detach to the guiNode
     */
    public void detachGUIChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                guiNode.detachChild(N);
                return null;
            }
        });
    }
    
    /**
     * Allows the camera to be moved instantaneously to a new position
     * @param c The location to move the camera to
     */
    public void moveCam(final CamLoc c) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                cam.setLocation(c.loc);
                cam.setRotation(c.rot);
                return null;
            }
        });
    }
    
    /**
     * Generates routes for the population
     */
    public void route() {
        attachGUIChild(routing);
        population.evacuate();
        detachGUIChild(routing);
    }
    /**
     * Plays/pauses the simulation
     */
    public void evacuate() {
        if (running == false) {
            population.play();
            running = true;
        }
        else {
            running = false;
            population.stopSim();
        }
    }
    
    /**
     * Allows updates due to settings
     * @param n Indicates what to do
     */
    public void restartSim(final int n) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                if (n == 0) {
                    SidePanel.reset();
                    rootNode.detachAllChildren();
                    simpleInitApp();
                }
                else if (n==1) {
                    if (appSettings.showOrigin()) rootNode.attachChild(origin);
                    else rootNode.detachChild(origin);
                }
                else if (n==2) {
                    setDisplayFps(appSettings.isShowFPS());
                }
                else if (n==3) {
                    drawNM();
                    showNavMesh();
                }
                return null;
            }
        });
    }
    
    /**
     * called after a change to settings.
     * Will show or hide the navmesh and display model as appropriate.
     */
    public void showNavMesh() {
        if (appSettings.isShowNavMesh() && !rootNode.hasChild(appSettings.getNMHolder())) {
            attachChild(appSettings.getNMHolder());
            
        }
        else if (!appSettings.isShowNavMesh() && rootNode.hasChild(appSettings.getNMHolder())) {
            detachChild(appSettings.getNMHolder());
        }
        if (appSettings.isShowHullFarSide()) {
            attachChild(hullNode);
            
        }
        else if (!appSettings.isShowHullFarSide() && rootNode.hasChild(hullNode)) {
            detachChild(hullNode);
        }

    }

    /**
     * Accessor for isDone method in population
     * @return
     */
    public boolean isDone() {
        return population.isDone();
    }
    
    /**
     *draws a navmesh from the model and saves it into settings.
     */
    public void drawNM() {
        //Loads Model
            Spatial ship = assetManager.loadModel(appSettings.getModelLocation() + "NavModel.j3o");
            Node node = (Node) ship;
            Node chil1 = (Node) node.getChildren().get(0);
            Geometry chil = (Geometry) chil1.getChildren().get(0);

            Mesh shipMesh = chil.getMesh();
            NavMesh shipNM = new NavMesh();

            NavMeshGenerator generator = new NavMeshGenerator();

            Mesh optimisedMesh = generator.optimize(shipMesh);

            shipNM.loadFromMesh(optimisedMesh);
            appSettings.saveNavMesh(shipNM);
            
            Node nmH = new Node();
            Node nmCH = new Node();
            
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
                    lineMaterial.setColor("Color", appSettings.getNavMeshColorC());
                    lineGeometry.setMaterial(lineMaterial);
                    nmH.attachChild(lineGeometry);

                    BitmapText helloText = new BitmapText(guiFont, false);
                    helloText.setSize(0.05f);
                    helloText.setText(vt[0].toString());
                    helloText.setLocalTranslation(vt[0].x, vt[0].y, vt[0].z);
                    nmCH.attachChild(helloText);

                }
            }
            appSettings.setNMHolder(nmH);
            appSettings.setCoords(nmCH);
            if (appSettings.getNMHolder() == null || appSettings.isSaveSettings()) {
                appSettings.saveNavMeshDrawn();
            }
    }
    
    /*
     * Importer Method. Loads two blender files from the given location and stores them in assets as j3o.
     * 
     */
    /*Does Not Work. Blend files not found!*/
    public void newModel(String loc) {
        appSettings.setModelLocation("Models/ImportedModel/");
        File destFile1 = new File("assets/Models/ImportedModel/NavModel.blend");
        if (destFile1.exists()) destFile1.delete();
        File sourceFile1 = new File(loc + "/NavModel.blend");
        File destFile2 = new File("assets/Models/ImportedModel/ShowModel.blend");
        if (destFile2.exists()) destFile2.delete();
        File sourceFile2 = new File(loc + "/ShowModel.blend");
        
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile1).getChannel();
            destination = new FileOutputStream(destFile1).getChannel();
            destination.transferFrom(source, 0, source.size());
            source = new FileInputStream(sourceFile2).getChannel();
            destination = new FileOutputStream(destFile2).getChannel();
            destination.transferFrom(source, 0, source.size());
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EvacSim.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EvacSim.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        Spatial navMod = assetManager.loadModel("Models/ImportedModel/NavModel.blend");
        Spatial showMod = assetManager.loadModel("Models/ImportedModel/ShowModel.blend");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File navModel = new File("assets/Models/ImportedModel/NavModel.j3o");
        try {
            exporter.save(navMod, navModel);
          } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Error: Failed to save NavMesh!", ex);
          }
        File file2 = new File("assets/Models/ImportedModel/ShowModel.j3o");
        try {
            exporter.save(showMod, file2);
          } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Error: Failed to save NavMeshCoords!", ex);
          }
        destFile1.delete();
        destFile2.delete();
    }

}
