/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import EvacSim.EvacSim;
import GUI.Components.AdvancedSettings;
import Init.Settings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/**
 *
 * @author hector
 */
public class EvacSimMainFrame extends javax.swing.JFrame {
    
    private Settings settings;
    private EvacSim evacSim;
    private Timer playTimer;
    private static int m = 0, s = 0;
    private GridBagConstraints g;
    JmeCanvasContext ctx;
    int newPopSize;
    private AdvancedSettings adset;
    
    

    /**
     * Creates new form EvacSimMainFrame
     */
    public EvacSimMainFrame() {
        initComponents();
        this.setVisible(true);
        evacSim = null;
        newPopSize = -1;
        
        //Finds the size of the screen and item. Uses this to calculate how to position the frame in the center of the screen.
        Toolkit kit = this.getToolkit();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Insets in = kit.getScreenInsets(gs[0].getDefaultConfiguration());
        Dimension d = kit.getScreenSize();
        int max_width = (d.width - in.left - in.right);
        int max_height = (d.height - in.top - in.bottom);
        this.setSize(Math.min(max_width, 1070), Math.min(max_height, 690));
        this.setLocation((int) (max_width - this.getWidth()) / 2, (int) (max_height - this.getHeight()) / 2);
        
        
        playTimer = new Timer(1000, new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                s++;
                if (s == 60){
                    m++;
                    s = 0;
                }
                sidePanel.setTime(m, s);
                
                if (evacSim.isDone()) {
                    playTimer.stop();
                }

            }    
          });
    }
    
    public void asyncPopSetup(int p) {
        newPopSize = p;
    }
    
    public void setSPParent() {
        sidePanel.passParent(this);
    }
    
    public void updateSettings(Settings s) {
        settings = s;
        sidePanel.updateSettings(settings);
        this.setTitle("TeamL Evacuation Simulator: " + settings.getModelName());
        newPopSize = settings.getPopulationNumber();
    }
    
    public void updateStatus(int numEvac) {
        sidePanel.updateStatus(settings.getPopulationNumber(), numEvac);
    }
    
    public void route() {
        evacSim.route();
        canvas.getComponent(0).requestFocus();
    }
    
    public void evac(int i) {
        if (i != 0) {
            evacSim.evac();
            playTimer.start();
        }
        canvas.getComponent(0).requestFocus();
    }
    
    public void stop() {
        playTimer.stop();
        evacSim.stopSim();
        canvas.getComponent(0).requestFocus();
    }
    
    public void camControl(String s) {
        if (evacSim != null) {
            evacSim.camControl(s);
            canvas.getComponent(0).requestFocus();
        }
    }
    public void hideCamCont() {
        sidePanel.updateSettings(settings);
    }
    
    public void setcam(String s) {
        if (evacSim != null) {
            evacSim.setCam(s);
        }
        canvas.getComponent(0).requestFocus();
    }
    
    public void setCamSpeed(int s) {
        if (evacSim != null) {
            evacSim.setCamSpeed(s);
        }
        settings.setCamSpeed(s);
        canvas.getComponent(0).requestFocus();
    }
    
    public void drawNM(Settings set) {
        evacSim.drawNavmesh();
        settings = set;
        evacSim.setSettings(set);
        canvas.getComponent(0).requestFocus();
    }
    public void remNM(Settings set) {
        evacSim.removeNavmesh();
        settings = set;
        evacSim.setSettings(set);
        canvas.getComponent(0).requestFocus();
    }
    
    public void updateCanvas() {
        if (newPopSize > 0 && settings.getPopulationNumber() != newPopSize) {
            settings.setPopulationNumber(newPopSize);
        }
        canvas.remove(loadingText);
        canvas.removeAll();
        
        if (evacSim != null) {
            evacSim.destroy(); /*NOT GOOD! Fix if possible!*/
        }
        else {
            g = new GridBagConstraints();
            g.fill = GridBagConstraints.HORIZONTAL;
            g.weightx = g.weighty = 1.0;
            g.gridwidth = 2;
            g.insets = new Insets(10, 0, 0, 8);
            g.gridx = g.gridy = 0;
        }
        
        playTimer.restart();
        playTimer.stop();
        
        sidePanel.updateSettings(settings);
        sidePanel.updateStatus(settings.getPopulationNumber(), -1);
                
        evacSim = new EvacSim(settings);
        evacSim.passParent(this);
        
        evacSim.createCanvas(); // create canvas!
        ctx = (JmeCanvasContext) evacSim.getContext();
        ctx.setSystemListener(evacSim);
        ctx.getCanvas().setSize(canvas.getSize());

        canvas.add(ctx.getCanvas(), g);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas = new javax.swing.JPanel();
        loadingText = new javax.swing.JLabel();
        sidePanel = new GUI.Components.SidePanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        importMod = new javax.swing.JMenuItem();
        expSet = new javax.swing.JMenuItem();
        goAway = new javax.swing.JMenuItem();
        settingsMenu = new javax.swing.JMenu();
        advanSet = new javax.swing.JMenuItem();
        saveSet = new javax.swing.JMenuItem();
        resSet = new javax.swing.JMenuItem();
        help = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 690));

        canvas.setBackground(new java.awt.Color(1, 1, 1));
        canvas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                canvasComponentResized(evt);
            }
        });

        loadingText.setBackground(new java.awt.Color(1, 1, 1));
        loadingText.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        loadingText.setForeground(new java.awt.Color(254, 254, 254));
        loadingText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingText.setText("Set Population Size, Then Click Pop. to Initiate Population");

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canvasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadingText, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                .addContainerGap())
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(canvasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadingText, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                .addContainerGap())
        );

        fileMenu.setText("File");

        importMod.setText("Import Model");
        fileMenu.add(importMod);

        expSet.setText("Export Settings");
        fileMenu.add(expSet);

        goAway.setText("Exit");
        goAway.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goAwayActionPerformed(evt);
            }
        });
        fileMenu.add(goAway);

        menuBar.add(fileMenu);

        settingsMenu.setText("Settings");

        advanSet.setText("Advanced Settings");
        advanSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advanSetActionPerformed(evt);
            }
        });
        settingsMenu.add(advanSet);

        saveSet.setText("Save Settings");
        settingsMenu.add(saveSet);

        resSet.setText("Restore Default Settings");
        settingsMenu.add(resSet);

        help.setText("Help");
        settingsMenu.add(help);

        menuBar.add(settingsMenu);

        setJMenuBar(menuBar);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(canvas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void canvasComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_canvasComponentResized
        if (evacSim != null) {
            canvas.getComponent(0).setSize(canvas.getSize());
        }
    }//GEN-LAST:event_canvasComponentResized

    private void goAwayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goAwayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goAwayActionPerformed

    private void advanSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advanSetActionPerformed
        if (adset == null) {
            adset = new AdvancedSettings();
        }
        settingsMenu.setPopupMenuVisible(false);
        adset.setVisible(true);
        adset.update(this, settings);
    }//GEN-LAST:event_advanSetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EvacSimMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EvacSimMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EvacSimMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EvacSimMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EvacSimMainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem advanSet;
    private javax.swing.JPanel canvas;
    private javax.swing.JMenuItem expSet;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem goAway;
    private javax.swing.JMenuItem help;
    private javax.swing.JMenuItem importMod;
    private javax.swing.JLabel loadingText;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem resSet;
    private javax.swing.JMenuItem saveSet;
    private javax.swing.JMenu settingsMenu;
    private GUI.Components.SidePanel sidePanel;
    // End of variables declaration//GEN-END:variables
}
