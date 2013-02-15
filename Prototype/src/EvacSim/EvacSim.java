package EvacSim;

import EvacSim.jme3tools.navmesh.NavMesh;
import EvacSim.jme3tools.navmesh.util.NavMeshGenerator;
import EvacSim.population.Population;
import GUI.EvacSimMainFrame;
import Init.Settings.CamLoc;
import Init.Settings.Settings;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class EvacSim extends SimpleApplication {

    private static Settings appSettings;
    private static Population population;
    private static NavMesh shipNM;
    private static EvacSimMainFrame parent;
    private Node navMeshHolder;
    private boolean navMeshOn;
    
    public EvacSim(Settings set) {
        appSettings = set;
    }

    @Override
    public void simpleInitApp() {
        
        
        this.setDisplayStatView(false); //Hides debug info
        this.setDisplayFps(appSettings.isShowFPS()); //Depending on settings hides FPS
        
        flyCam.setDragToRotate(true); //Sets cam mouse controls

        flyCam.setMoveSpeed(appSettings.getCamSpeed()); //Sets Cam speed

        Logger.getLogger("").setLevel(Level.SEVERE);

        //Loads Model
        Spatial ship = assetManager.loadModel(appSettings.getModelLocation());
        Node node = (Node) ship;
        Node chil1 = (Node) node.getChildren().get(0);
        Geometry chil = (Geometry) chil1.getChildren().get(0);

        Mesh shipMesh = chil.getMesh();
        shipNM = new NavMesh();

        NavMeshGenerator generator = new NavMeshGenerator();

        Mesh optimisedMesh = generator.optimize(shipMesh);

        shipNM.loadFromMesh(optimisedMesh);
        navMeshOn = false;
        if (appSettings.isShowNavMesh()) { 
            drawNavmesh(); //Draws NavMesh
            navMeshOn = true;
        }

        guiFont = assetManager.loadFont(appSettings.getGuiFont());
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.1f);
        helloText.setText("ORIGIN");
        helloText.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(helloText);

        try {
            population = new Population(rootNode, shipNM, this);
        } catch (Exception e) {
        }

        population.populate(appSettings.getPopulationNumber());

    }
    
    public void setSettings(Settings set) {
        appSettings = set;
    }
    
    public void passParent(EvacSimMainFrame mf) {
        parent = mf;
    }
    
    public static Population getPopulation() {
        return population;
    }
    
    public static void updateStatus() {
        parent.updateStatus(population.getNumEvacuated());
    }
    
    public boolean isDone() {
        return population.isDone();
    }
    
    public void evac() {
        population.play();
    }
    
    public void route() {
        population.evacuate();
    }
    
    public void stopSim() {
        population.stopSim();
    }
    
    public void camControl(String s) {
        flyCam.onAnalog(s, (appSettings.getCamSpeed()+3.0f)/10f, (appSettings.getCamSpeed()+3.0f)/10f);
    }
    
    public void camRotate(String s) {
        flyCam.onAnalog(s, 2f, 2f);
    }
    
    public void setCam(String cl) {
        CamLoc c = appSettings.getCamLocations().get(cl);
        if (c != null) {
            cam.setLocation(c.getLoc());
            cam.setRotation(c.getRot());
        }
    }
    
    public void setCamSpeed(int s) {
        flyCam.setMoveSpeed(s);
    }
    
    public void removeNavmesh() {
        if (navMeshOn) {
            navMeshOn = false;
            rootNode.detachChild(navMeshHolder);
        }
    }
    public void drawNavmesh() {
        if (!navMeshOn) {
            navMeshOn = true;
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

                    if (appSettings.isShowCoordinates()) {
                        BitmapText helloText = new BitmapText(guiFont, false);
                        helloText.setSize(0.05f);
                        helloText.setText(vt[0].toString());
                        helloText.setLocalTranslation(vt[0].x, vt[0].y, vt[0].z);
                        navMeshHolder.attachChild(helloText);
                    }
                }
            }
        }
    }
    

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
}
