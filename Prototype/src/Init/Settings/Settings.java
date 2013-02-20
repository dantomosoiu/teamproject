/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init.Settings;

import EvacSim.jme3tools.navmesh.NavMesh;
import EvacSim.population.Population;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import java.io.File;
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
    private int numEvac = 0;
    private Node nmHolder;
    private Node nmCHolder;
    
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
    public void resetDefault() {
        variables = new SettingsVariables();
    }
    
    public void loadFromFile() {
        loadFromFile("assets/Settings/settings.data");
    }
    public void loadFromFile(String fileName) {
        SettingsVariables v2 = load(fileName);
        if (v2 != null) variables = v2;
    }
    
    private SettingsVariables load(String fileName) {
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
                return (SettingsVariables) obj;
            }
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException io) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, io);
        } catch (ClassNotFoundException ce) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ce);
        }
        return null;
    }
        
        
    public void saveToFile() {
        if (variables.saveSettings) {
            saveToFile("assets/Settings/settings.data");
        }
    }
    public void saveToFile(String fileName) {
        save(variables, fileName);
    }
    
    private void save(SettingsVariables sv, String fileName) {
        FileOutputStream f_out;
        try {
            f_out = new FileOutputStream(fileName);
            // Write object with ObjectOutputStream
            ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
            // Write object out to disk
            obj_out.writeObject ( sv );
            f_out.close();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException io) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, io);
        }
    }
    
    public void saveNavMeshDrawn() {
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File("assets/Settings/navMeshNode.j3o");
        try {
            exporter.save(nmHolder, file);
          } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Error: Failed to save NavMesh!", ex);
          }
        File file2 = new File("assets/Settings/navCoordsNode.j3o");
        try {
            exporter.save(nmCHolder, file2);
          } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Error: Failed to save NavMeshCoords!", ex);
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
    public Node getNMHolder() {
        return nmHolder;
    }
    public void setNMHolder(Node n) {
        nmHolder = n;
    }
        public Node Coords() {
        return nmCHolder;
    }
    public void setCoords(Node n) {
        nmCHolder = n;
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
        if (saveSettings == false){
            SettingsVariables sv = load("assets/Settings/settings.data");
            sv.saveSettings = false;
            save(sv, "assets/Settings/settings.data");
        }
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

    public String getNavMeshColor() {
        return variables.navMeshColor;
    }
    public ColorRGBA getNavMeshColorC() {
        return getCol(variables.navMeshColor);
    }

    public void setNavMeshColor(String navMeshColor) {
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
    
    public void setNumEvac(int i) {
        numEvac = i;
    }
    public int getNumEvac() {
        return numEvac;
    }
    public void incNumEvac() {
        numEvac += 1;
        if (variables.populationNumber-numEvac == 0) Population.done();
    }
    
    public HashMap<String, PersonCategory> getPersonCategories() {
        return variables.peopleTypes;
    }
    public void remPersonCategory(String name) {
        variables.peopleTypes.remove(name);
    }
    public void addPersonCategory(String name, PersonCategory p) {
        variables.peopleTypes.put(name, p);
    }
    
    public boolean confExit() {
        return variables.confExit;
    }
    public void setConfExit(boolean b) {
        variables.confExit = b;
    }
    
    public String currentCamLoc() {
        return variables.currentCamLoc;
    }
    public void setCamLoc(String s) {
        if (variables.camLocations.keySet().contains(s)) variables.currentCamLoc = s;
    }
    
    public ColorRGBA getCol(String s) {
        if (s.equals("White")) return ColorRGBA.White;
        else if (s.equals("LightGray")) return ColorRGBA.LightGray;
        else if (s.equals("Red")) return ColorRGBA.Red;
        else if (s.equals("Green")) return ColorRGBA.Green;
        else if (s.equals("Blue")) return ColorRGBA.Blue;
        else if (s.equals("Yellow")) return ColorRGBA.Yellow;
        else if (s.equals("Magenta")) return ColorRGBA.Magenta;
        else if (s.equals("Cyan")) return ColorRGBA.Cyan;
        else if (s.equals("Orange")) return ColorRGBA.Orange;
        else if (s.equals("Pink")) return ColorRGBA.Pink;
        else return ColorRGBA.White;
    }
    
}