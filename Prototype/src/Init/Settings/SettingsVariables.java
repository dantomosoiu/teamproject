/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init.Settings;

import EvacSim.jme3tools.navmesh.NavMesh;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
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
    protected boolean showCoordinates;
    protected boolean showNavMesh;
    protected boolean showShip;
    protected boolean showHullFarSide;
    protected String navMeshColor;
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
    protected HashMap<String, PersonCategory> peopleTypes;
    protected boolean confExit;
    protected String currentCamLoc;

    protected SettingsVariables() {
        modelLocation = "Models/FlatModel2/FlatModel2.j3o";
        populationNumber = 20;
        tmpPopNum = populationNumber;
        camSpeed = 5;
        showCoordinates = false;
        showNavMesh = true;
        showShip = false;
        showHullFarSide = false;
        navMeshColor = "White";
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
        peopleTypes = new HashMap<String, PersonCategory>();
        peopleTypes.put("Average", new PersonCategory("Average", 0.6f, 1.2f,0, 0, "White", 80));
        peopleTypes.put("Athlete", new PersonCategory("Athlete", 1.0f, 1.9f,0, 0, "Red", 8));
        peopleTypes.put("Infant", new PersonCategory("Infant", 0.3f, 0.7f,0, 0, "Pink", 12));
        confExit = true;
        currentCamLoc = "Default";
    }
}
