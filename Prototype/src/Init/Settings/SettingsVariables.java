/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init.Settings;

import EvacSim.jme3tools.navmesh.NavMesh;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import javax.swing.UIManager;

/**
 *
 * @author hector
 */
public class SettingsVariables implements Serializable {
    protected String modelLocation; //Location of j30 file. Should be in Models!
    protected int populationNumber; //Number of people to populate model with
    protected int tmpPopNum; //Number of people to populate model with
    protected float camSpeed; //Camera move speed
    protected String modelName;
    //GUI Settings
    //private Dimension windowSize;
    protected boolean showCoordinates;
    protected boolean showNavMesh;
    protected boolean showShip;
    protected boolean showHullFarSide;
    protected Color navMeshColor;
    protected String guiFont;
    protected boolean showFPS;
    protected boolean saveSettings;
    protected boolean hideCamPanel;
    protected HashMap<String, CamLoc> camLocations;
    protected static String theme;
    protected NavMesh nm;
    protected boolean showOrigin;
    protected String personModelLocation;
    protected boolean showRoutes;
    protected float BASESPEED;
    protected boolean printEverything;

    protected SettingsVariables() {
        modelLocation = "Models/FlatModel2/FlatModel2.j3o";
        populationNumber = 20;
        tmpPopNum = 20;
        camSpeed = 5;
        showCoordinates = false;
        showNavMesh = true;
        showShip = false;
        showHullFarSide = false;
        navMeshColor = Color.white;
        guiFont = "Interface/Fonts/Default.fnt";
        showFPS = true;
        saveSettings = true;
        hideCamPanel = false;
        modelName = "Glenlee, Glasgow";
        camLocations = new HashMap<String, CamLoc>();
        camLocations.put("Default", new CamLoc(new Vector3f( 0,0,10), new Quaternion(0,1,0,0)));
        camLocations.put("Exits", new CamLoc(new Vector3f( 8.117443f, 5.746009f, 12.390097f), new Quaternion(-0.00167683f, 0.98768485f, -0.15608728f, -0.010609908f)));
        theme = UIManager.getSystemLookAndFeelClassName();
        nm = null;
        showOrigin = false;
        personModelLocation = "Models/Ninja/Ninja.mesh.xml";
        showRoutes = true;
        BASESPEED = 1;
        printEverything = false;
    }
    
    protected SettingsVariables(String modelLocation, int populationNumber, int tmpPopNum, float camSpeed, String modelName, boolean showCoordinates, boolean showNavMesh, boolean showShip, boolean showHullFarSide, Color navMeshColor, String guiFont, boolean showFPS, boolean saveSettings, boolean hideCamPanel, HashMap<String, CamLoc> camLocations, boolean showOrigin) {
        this.modelLocation = modelLocation;
        this.populationNumber = populationNumber;
        this.tmpPopNum = tmpPopNum;
        this.camSpeed = camSpeed;
        this.modelName = modelName;
        this.showCoordinates = showCoordinates;
        this.showNavMesh = showNavMesh;
        this.showShip = showShip;
        this.showHullFarSide = showHullFarSide;
        this.navMeshColor = navMeshColor;
        this.guiFont = guiFont;
        this.showFPS = showFPS;
        this.saveSettings = saveSettings;
        this.hideCamPanel = hideCamPanel;
        this.camLocations = camLocations;
        nm = null;
        this.showOrigin = showOrigin;
        personModelLocation = "Models/Ninja/Ninja.mesh.xml";
        showRoutes = true;
        BASESPEED = 1;
        printEverything = false;
    }
    
    
}
