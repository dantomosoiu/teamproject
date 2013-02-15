/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init;

import Init.settingsHelpers.CamLoc;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author hector
 */
public class Settings {
    
    private String modelLocation; //Location of j30 file. Should be in Models!
    private int populationNumber; //Number of people to populate model with
    private float camSpeed; //Camera move speed
    private String modelName;
    
    //GUI Settings
    //private Dimension windowSize;
    private boolean showCoordinates;
    private boolean showNavMesh;
    private boolean showShip;
    private boolean showHullFarSide;
    private Color navMeshColor;
    private String guiFont;
    private boolean showFPS;
    private boolean saveSettings;
    private boolean hideCamPanel;
    private HashMap<String, CamLoc> camLocations;


    
    public Settings() {
        modelLocation = "Models/FlatModel2/FlatModel2.j3o";
        populationNumber = 20;
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
    }
    
    public void loadFromFile() {
        
    }
    public void loadFromFile(String fileName) {
        
    }
    public void saveToFile() {
        if (saveSettings) {
            
        }
    }
    public void saveToFile(String fileName) {
        
    }
    
    public HashMap<String, CamLoc> getCamLocations() {
        return camLocations;
    }
    
    public void addCamLoc(String s, CamLoc c) {
        camLocations.put(s, c);
    }

    public boolean isSaveSettings() {
        return saveSettings;
    }

    public void setSaveSettings(boolean saveSettings) {
        this.saveSettings = saveSettings;
    }

    public boolean isHideCamPanel() {
        return hideCamPanel;
    }

    public void setHideCamPanel(boolean hideCamPanel) {
        this.hideCamPanel = hideCamPanel;
    }

    public boolean isShowFPS() {
        return showFPS;
    }

    public void setShowFPS(boolean showFPS) {
        this.showFPS = showFPS;
    }
    
    public String getModelLocation() {
        return modelLocation;
    }

    public void setModelLocation(String modelLocation) {
        this.modelLocation = modelLocation;
    }

    public int getPopulationNumber() {
        return populationNumber;
    }

    public void setPopulationNumber(int populationNumber) {
        this.populationNumber = populationNumber;
    }

    public float getCamSpeed() {
        return camSpeed;
    }

    public void setCamSpeed(float camSpeed) {
        this.camSpeed = camSpeed;
    }

    public boolean isShowCoordinates() {
        return showCoordinates;
    }

    public void setShowCoordinates(boolean showCoordinates) {
        this.showCoordinates = showCoordinates;
    }

    public boolean isShowNavMesh() {
        return showNavMesh;
    }

    public void setShowNavMesh(boolean showNavMesh) {
        this.showNavMesh = showNavMesh;
    }

    public boolean isShowShip() {
        return showShip;
    }

    public void setShowShip(boolean showShip) {
        this.showShip = showShip;
    }

    public boolean isShowHullFarSide() {
        return showHullFarSide;
    }

    public void setShowHullFarSide(boolean showHullFarSide) {
        this.showHullFarSide = showHullFarSide;
    }

    public Color getNavMeshColor() {
        return navMeshColor;
    }

    public void setNavMeshColor(Color navMeshColor) {
        this.navMeshColor = navMeshColor;
    }

    public String getGuiFont() {
        return guiFont;
    }

    public void setGuiFont(String guiFont) {
        this.guiFont = guiFont;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
}