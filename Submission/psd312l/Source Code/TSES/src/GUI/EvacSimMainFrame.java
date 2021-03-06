
package GUI;

import EvacSim.EvacSim;
import GUI.Components.About;
import GUI.Components.AdvancedSettings;
import GUI.Components.GUIHelperMethods;
import GUI.Components.Help;
import GUI.Components.Importer;
import Init.Settings.Settings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author hector
 */
public class EvacSimMainFrame extends javax.swing.JFrame {
    
    private Settings settings;//settings to use
    private EvacSim evacSim;//evacSim to use

    //Singleton
    private static EvacSimMainFrame instance = null;
    //Private Constructor protects Singleton
    private EvacSimMainFrame(Settings set, EvacSim es) {
        settings = set;
        evacSim = es;
        initComponents();
        this.setTitle("TeamL Evacuation Simulator: " + settings.getModelName());
        this.setVisible(true);
        GUIHelperMethods.centralise(1070, 690, this);
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = g.weighty = 1.0;
        g.gridwidth = 2;
        g.insets = new Insets(10, 0, 0, 8);
        g.gridx = g.gridy = 0;

        evacSim.createCanvas(); // create canvas!
        JmeCanvasContext ctx = (JmeCanvasContext) evacSim.getContext();
        ctx.setSystemListener(evacSim);
        ctx.getCanvas().setSize(canvas.getSize());

        canvas.add(ctx.getCanvas(), g);
        
        canvas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ((Canvas)canvas.getComponent(0)).setSize(canvas.getSize());
            }
        });
        
    }
    //Returns the Singleton
    public static EvacSimMainFrame get(Settings set, EvacSim evs) {
        if(instance == null) {
            instance = new EvacSimMainFrame(set, evs);
        }
        return instance;
    }
    
    public static void giveCanFoc() {
        if (canvas != null) if (canvas.getComponent(0) != null) canvas.getComponent(0).requestFocus();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }
    
    public static Canvas getCan() {
        return (Canvas)canvas.getComponent(0);
    }
    
    
    /*
     * Modified Netbeans Generated code hence comments are scarce. Initialises components.
     * 
     */
    private void initComponents() {
        sidePanel = new GUI.Components.SidePanel(settings, evacSim);
        canvas = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importModel = new javax.swing.JMenuItem();
        exit = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        adSet = new javax.swing.JMenuItem();
        savSet = new javax.swing.JMenuItem();
        impSet = new javax.swing.JMenuItem();
        exSet = new javax.swing.JMenuItem();
        resSet = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();
        help = new javax.swing.JMenuItem();

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                if (settings.confExit()) {
                    int result = JOptionPane.showConfirmDialog((Component)e.getSource(), "Are you sure you want to exit the application?", "Exit Application", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        settings.saveToFile();
                        setDefaultCloseOperation(EXIT_ON_CLOSE);
                    }
                }
                else {
                    settings.saveToFile();
                    setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            }
        });
        
        setPreferredSize(new java.awt.Dimension(1070, 690));

        sidePanel.setBorder(null);

        canvas.setBackground(new java.awt.Color(1, 1, 1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 773, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        fileMenu.setText("File");

        importModel.setText("Import Model");
        //importModel.setEnabled(false);
        importModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importActionPerformed(evt);
            }
        });
        fileMenu.add(importModel);

        exit.setText("Exit");
        fileMenu.add(exit);
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        jMenuBar1.add(fileMenu);

        settingsMenu.setText("Settings");

        adSet.setText("Advanced Settings");
        adSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adSetActionPerformed(evt);
            }
        });
        
        settingsMenu.add(adSet);

        savSet.setText("Save Settings");
        settingsMenu.add(savSet);
        savSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settings.saveToFile();
            }
        });
        
        impSet.setText("Import Settings");
        settingsMenu.add(impSet);
        impSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String file = fileChooser.getSelectedFile().getPath();
                    
                    int result = JOptionPane.showConfirmDialog(null, "Program will Exit with manual restart required. Continue?", "", JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        settings.loadFromFile(file);
                        evacSim.drawNM();
                        settings.saveToFile();
                        evacSim.stop();
                        System.exit(0);
                    }
               
                }
            }
        });

        exSet.setText("Export Settings");
        settingsMenu.add(exSet);
        exSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String file = fileChooser.getSelectedFile().getPath();
                    settings.saveToFile(file);
                }
            }
        });

        resSet.setText("Restore Factory Settings");
        settingsMenu.add(resSet);
        resSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int result = JOptionPane.showConfirmDialog(null, "Program will Exit with manual restart required. Continue?", "", JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION) {
                    settings.resetDefault();
                    settings.saveToFile();
                    evacSim.drawNM();
                    evacSim.stop();
                    System.exit(0);
                }
            }
        });

        jMenuBar1.add(settingsMenu);

        helpMenu.setText("Help");

        about.setText("About");
        helpMenu.add(about);
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed();
            }
        });

        help.setText("Help");
        helpMenu.add(help);
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed();
            }
        });
        

        jMenuBar1.add(helpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    private void adSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adSetActionPerformed
        AdvancedSettings advancedSettings = new AdvancedSettings(this, settings, evacSim);
        sidePanel.updateSettings();
    }
    private void importActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adSetActionPerformed
        Importer importer = new Importer(this, settings, evacSim);
        sidePanel.updateSettings();
        this.setTitle("TeamL Evacuation Simulator: " + settings.getModelName());
    }
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }
    private void aboutActionPerformed() {
        About about = new About(this, true);
    }
    private void helpActionPerformed() {
        Help help = new Help(this, true);
    }

    private javax.swing.JMenuItem about;
    private javax.swing.JMenuItem adSet;
    private javax.swing.JMenuItem impSet;
    private javax.swing.JMenuItem exSet;
    private javax.swing.JMenuItem exit;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem help;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem importModel;
    private javax.swing.JMenuBar jMenuBar1;
    private static javax.swing.JPanel canvas;
    private javax.swing.JMenuItem resSet;
    private javax.swing.JMenuItem savSet;
    private javax.swing.JMenu settingsMenu;
    private GUI.Components.SidePanel sidePanel;
}
