/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author hector
 */
public class GUIHelperMethods {
    
    public static void centralise(int i, int j, JFrame jf) {
        //Finds the size of the screen and item. Uses this to calculate how to position the frame in the center of the screen.
        Toolkit kit = jf.getToolkit();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Insets in = kit.getScreenInsets(gs[0].getDefaultConfiguration());
        Dimension d = kit.getScreenSize();
        int max_width = (d.width - in.left - in.right);
        int max_height = (d.height - in.top - in.bottom);
        jf.setSize(Math.min(max_width, i), Math.min(max_height, j));
        jf.setLocation((int) (max_width - jf.getWidth()) / 2, (int) (max_height - jf.getHeight()) / 2);
    }
    public static void centralise(int i, int j, JDialog jf) {
        //Finds the size of the screen and item. Uses this to calculate how to position the frame in the center of the screen.
        Toolkit kit = jf.getToolkit();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Insets in = kit.getScreenInsets(gs[0].getDefaultConfiguration());
        Dimension d = kit.getScreenSize();
        int max_width = (d.width - in.left - in.right);
        int max_height = (d.height - in.top - in.bottom);
        jf.setSize(Math.min(max_width, i), Math.min(max_height, j));
        jf.setLocation((int) (max_width - jf.getWidth()) / 2, (int) (max_height - jf.getHeight()) / 2);
    }
    
}
