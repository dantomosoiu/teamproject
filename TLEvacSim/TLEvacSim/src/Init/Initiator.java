package Init;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import GUI.EvacSimMainFrame;

/**
 *
 * @author hector
 */
public class Initiator {
    
    public static void main(String[] args) {
        
        Settings settings = new Settings();
        settings.loadFromFile();
        
        EvacSimMainFrame mainFrame = new EvacSimMainFrame();
        mainFrame.setSPParent();
        mainFrame.updateSettings(settings);
        
    }
    
    
}
