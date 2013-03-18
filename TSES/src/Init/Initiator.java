package Init;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import EvacSim.EvacSim;
import GUI.EvacSimMainFrame;
import Init.Settings.Settings;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Hector Grebbell
 */
public class Initiator {
    
    public static void main(String[] args) {
        
        Settings settings = Settings.get();
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
        
        EvacSim evacSim = new EvacSim(settings);
        
        EvacSimMainFrame.get(settings, evacSim);
        
    }  
}
