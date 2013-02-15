package Init;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import Init.Settings.Settings;
import GUI.EvacSimMainFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author hector
 */
public class Initiator {
    
    private static Settings settings;
    
    public static void main(String[] args) {
        
        settings = new Settings();
        settings.loadFromFile();
        try {
            UIManager.setLookAndFeel(Settings.getTheme());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        EvacSimMainFrame mainFrame = new EvacSimMainFrame();
        mainFrame.setSPParent();
        mainFrame.updateSettings(settings);
        
    }
    
    
}
