/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init.Settings;

import EvacSim.jme3tools.navmesh.NavMesh;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hector
 */
public class Settings {
    //Singleton
    private static Settings instance = null;
    private static SettingsVariables variables;
    
    //Private constructor protects singleton method
    private Settings() {
        variables = new SettingsVariables();
    }//Returns the Singleton
    public static Settings get() {
        if(instance == null) {
        instance = new Settings();
    }
    return instance;
    }
    
    public void loadFromFile() {
        loadFromFile("assets/Settings/settings.data");
    }
    public void loadFromFile(String fileName) {
        FileInputStream f_in;
        try {
            f_in = new FileInputStream(fileName);
            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream (f_in);
            // Read an object
            Object obj = obj_in.readObject();
            
            obj_in.close();
            f_in.close();
            if (obj instanceof SettingsVariables)
            {
                // Cast object to a Vector
                variables = (SettingsVariables) obj;
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException io) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, io);
        } catch (ClassNotFoundException ce) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ce);
        }
    }
    public void saveToFile() {
        if (variables.saveSettings) {
            saveToFile("assets/Settings/settings.data");
        }
    }
    public void saveToFile(String fileName) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(fileName);
            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
            // Write object out to disk
            obj_out.writeObject ( variables );
            f_out.close();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException io) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, io);
        }
    }
    
    public static String getTheme() {
        return SettingsVariables.theme;
    }
    
    public NavMesh getNavMesh() {
        return variables.nm;
    }
    public void saveNavMesh(NavMesh nm) {
        variables.nm = nm;
        saveToFile();
    }
    
    public HashMap<String, CamLoc> getCamLocations() {
        return variables.camLocations;
    }
    
    public void addCamLoc(String s, CamLoc c) {
        variables.camLocations.put(s, c);
    }

    public boolean isSaveSettings() {
        return variables.saveSettings;
    }

    public void setSaveSettings(boolean saveSettings) {
        variables.saveSettings = saveSettings;
    }

    public boolean isHideCamPanel() {
        return variables.hideCamPanel;
    }

    public void setHideCamPanel(boolean hideCamPanel) {
        variables.hideCamPanel = hideCamPanel;
    }

    public boolean isShowFPS() {
        return variables.showFPS;
    }

    public void setShowFPS(boolean showFPS) {
        variables.showFPS = showFPS;
    }
    
    public String getModelLocation() {
        return variables.modelLocation;
    }

    public void setModelLocation(String modelLocation) {
        variables.modelLocation = modelLocation;
    }

    public int getPopulationNumber() {
        return variables.populationNumber;
    }

    public void setPopulationNumber(int populationNumber) {
        variables.tmpPopNum = populationNumber;
    }
    public void updatePopNum() {
        variables.populationNumber = variables.tmpPopNum;
    }

    public float getCamSpeed() {
        return variables.camSpeed;
    }

    public void setCamSpeed(float camSpeed) {
        variables.camSpeed = camSpeed;
    }

    public boolean isShowCoordinates() {
        return variables.showCoordinates;
    }

    public void setShowCoordinates(boolean showCoordinates) {
        variables.showCoordinates = showCoordinates;
    }

    public boolean isShowNavMesh() {
        return variables.showNavMesh;
    }

    public void setShowNavMesh(boolean showNavMesh) {
        variables.showNavMesh = showNavMesh;
    }

    public boolean isShowShip() {
        return variables.showShip;
    }

    public void setShowShip(boolean showShip) {
        variables.showShip = showShip;
    }

    public boolean isShowHullFarSide() {
        return variables.showHullFarSide;
    }

    public void setShowHullFarSide(boolean showHullFarSide) {
        variables.showHullFarSide = showHullFarSide;
    }

    public Color getNavMeshColor() {
        return variables.navMeshColor;
    }

    public void setNavMeshColor(Color navMeshColor) {
        variables.navMeshColor = navMeshColor;
    }

    public String getGuiFont() {
        return variables.guiFont;
    }

    public void setGuiFont(String guiFont) {
        variables.guiFont = guiFont;
    }

    public String getModelName() {
        return variables.modelName;
    }

    public void setModelName(String modelName) {
        variables.modelName = modelName;
    }
    
    public boolean showOrigin() {
        return variables.showOrigin;
    }
    
    public void setShowOrigin(boolean o) {
        variables.showOrigin = o;
    }
    
    public String getPersonModelLocation() {
        return variables.personModelLocation;
    }
    public void setPersonModelLocation(String s) {
        variables.personModelLocation = s;
    }
    
    public float getBASESPEED() {
        return variables.BASESPEED;
    }
    public void setBaseSpeed(float f) {
        variables.BASESPEED = f;
    }
    
    public boolean showRoutes() {
        return variables.showRoutes;
    }
    public void setShowRoutes(boolean b) {
        variables.showRoutes = b;
    }
    
    public boolean getPrintEv() {
        return variables.printEverything;
    }
    public void setPrintEv(boolean b) {
        variables.printEverything = b;
    }
    
}