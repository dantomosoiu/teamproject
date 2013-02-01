/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package initializer;

import com.jme3.font.BitmapFont;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author hector
 */
public class Settings {
    
    private String modelLocation; //Location of j30 file. Should be in Models!
    private int populationNumber; //Number of people to populate model with
    private float camSpeed; //Camera move speed
    
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

    
    public Settings() {
        modelLocation = "Models/FlatModel/FlatModel.j3o";
        populationNumber = 20;
        camSpeed = 10;
        showCoordinates = true;
        showNavMesh = true;
        showShip = false;
        showHullFarSide = false;
        navMeshColor = Color.white;
        guiFont = "Interface/Fonts/Default.fnt";
        showFPS = true;
        saveSettings = true;
        hideCamPanel = false;
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
    
}
