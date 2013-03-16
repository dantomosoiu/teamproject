package EvacSim;

import EvacSim.jme3tools.navmesh.NavMesh;
import EvacSim.jme3tools.navmesh.util.NavMeshGenerator;
import EvacSim.population.Population;
import GUI.Components.SidePanel;
import Init.Settings.CamLoc;
import Init.Settings.Settings;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class EvacSim extends SimpleApplication {
    
    private static Settings appSettings;
    private static Population population;
    private BitmapText origin;
    private boolean running;
    private String buttonCam;
    private BitmapText routing;
    
    /**
     *
     * @param set
     */
    public EvacSim(Settings set) {
        appSettings = set;
        running = false;
    }
    
    @Override
    public void update() {
        super.update();
        if (buttonCam != null) flyCam.onAnalog(buttonCam, 0.02f, 0);
    }

    /**
     *
     */
    @Override
    public void simpleInitApp() {
        buttonCam = null;
        this.setPauseOnLostFocus(false);
        this.setDisplayStatView(false); //Hides debug info
        this.setDisplayFps(appSettings.isShowFPS()); //Depending on settings hides FPS
        flyCam.setDragToRotate(true); //Sets cam mouse controls
        routing = new BitmapText(guiFont, false);   
        routing.setSize(guiFont.getCharSet().getRenderedSize()*1.2f);      // font size
        routing.setColor(ColorRGBA.White);                             // font color
        routing.setText("Routing. Please Wait...");            // the text
        int add = 0;
        if (appSettings.isShowFPS()) add = 30;
        routing.setLocalTranslation(0, add + routing.getLineHeight(), 0); // position
        flyCam.setMoveSpeed(appSettings.getCamSpeed()); //Sets Cam speed

        Logger.getLogger("").setLevel(Level.SEVERE);

        if ((appSettings.getNavMesh()) == null) {
            drawNM();
        }
        else {
            appSettings.setNMHolder((Node)assetManager.loadModel("Settings/navMeshNode.j3o"));
            //appSettings.setCoords((Node)assetManager.loadModel("Settings/navCoordsNode.j3o"));
        }
        
        showNavMesh();
        
        guiFont = assetManager.loadFont(appSettings.getGuiFont());
        origin = new BitmapText(guiFont, false);
        origin.setSize(0.1f);
        origin.setText("ORIGIN");
        origin.setLocalTranslation(0, 0, 0);
        if (appSettings.showOrigin()) {
            rootNode.attachChild(origin);
        }

        
        population = new Population(this);
        if (population == null) System.out.println("Shite");
        
        population.populate();

    }
    
    /**
     *
     * @param N
     */
    public void attachChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                rootNode.attachChild(N);
                return null;
            }
        });
    }
    public void attachGUIChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                guiNode.attachChild(N);
                return null;
            }
        });
    }
    
    /**
     *
     * @param s
     */
    public void moveCamC(String s) {
        if (s == null) buttonCam = null;
        else if (buttonCam == null) buttonCam = s;
        else buttonCam = null;
    }

    /**
     *
     * @param N
     */
    public void detachChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                rootNode.detachChild(N);
                return null;
            }
        });
    }
    public void detachGUIChild(final Spatial N) {
        this.enqueue( new Callable<Object>() {
        public Spatial call() throws Exception {
                guiNode.detachChild(N);
                return null;
            }
        });
    }
    
    /**
     *
     * @param c
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
     *
     */
    public void route() {
        attachGUIChild(routing);
        population.evacuate();
        detachGUIChild(routing);
    }
    /**
     *
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
     *
     * @param n
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
     *
     */
    public void showNavMesh() {
        if (appSettings.isShowNavMesh() && !rootNode.hasChild(appSettings.getNMHolder())) {
            attachChild(appSettings.getNMHolder());
        }
        else if (rootNode.hasChild(appSettings.getNMHolder())) {
            detachChild(appSettings.getNMHolder());
        }
        /*if (appSettings.isShowCoordinates() && !rootNode.hasChild(appSettings.Coords())) {
            attachChild(appSettings.Coords());
        }
        else if (rootNode.hasChild(appSettings.Coords())) {
            detachChild(appSettings.Coords());
        }*/
    }
    
    /**
     *
     * @return
     */
    public InputManager getInManager() {
        return inputManager;
    }
    
    /**
     *
     * @return
     */
    public boolean isDone() {
        return population.isDone();
    }
    
    /**
     *
     */
    public void drawNM() {
        //Loads Model
            Spatial ship = assetManager.loadModel(appSettings.getModelLocation());
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

}
