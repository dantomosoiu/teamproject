/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Components;

import EvacSim.EvacSim;
import Init.Settings.Settings;

/**
 *
 * @author hector
 */
public class CamControls extends javax.swing.JPanel {

    private EvacSim evacSim;
    private Settings settings;
    /**
     * Creates new form CamControls
     */
    public CamControls(EvacSim evs, Settings set) {
        evacSim = evs;
        settings = set;
        initComponents();
        panLeft.setEnabled(false);
        panRight.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        camSpeed = new javax.swing.JSlider();
        panUp = new javax.swing.JButton();
        panDown = new javax.swing.JButton();
        mUp = new javax.swing.JButton();
        mLeft = new javax.swing.JButton();
        mDown = new javax.swing.JButton();
        mRight = new javax.swing.JButton();
        panRight = new javax.swing.JButton();
        panLeft = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(148, 148, 148)));
        setForeground(new java.awt.Color(254, 254, 254));
        setMaximumSize(new java.awt.Dimension(280, 211));
        setMinimumSize(new java.awt.Dimension(280, 211));
        setPreferredSize(new java.awt.Dimension(280, 211));

        jLabel1.setText("Cam Speed");

        camSpeed.setFont(new java.awt.Font("Ubuntu", 0, 3)); // NOI18N
        camSpeed.setForeground(new java.awt.Color(246, 244, 242));
        camSpeed.setMaximum(15);
        camSpeed.setMinimum(1);
        camSpeed.setValue(Math.round(settings.getCamSpeed()));
        camSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                camSpeedStateChanged(evt);
            }
        });

        panUp.setText("↟");
        panUp.setToolTipText("Rise");
        panUp.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                panUpActionPerformed(evt);
            }
        });

        panDown.setText("↡");
        panDown.setToolTipText("Lower");
        panDown.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                panDownActionPerformed(evt);
            }
        });

        mUp.setFont(new java.awt.Font("Symbol", 1, 14)); // NOI18N
        mUp.setText("↑");
        mUp.setToolTipText("Move Forward");
        mUp.setMaximumSize(new java.awt.Dimension(30, 75));
        mUp.setMinimumSize(new java.awt.Dimension(30, 75));
        mUp.setPreferredSize(new java.awt.Dimension(30, 75));
        mUp.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mUpActionPerformed(evt);
            }
        });

        mLeft.setFont(new java.awt.Font("Symbol", 1, 14)); // NOI18N
        mLeft.setText("←");
        mLeft.setMaximumSize(new java.awt.Dimension(30, 75));
        mLeft.setMinimumSize(new java.awt.Dimension(30, 75));
        mLeft.setPreferredSize(new java.awt.Dimension(30, 75));
        mLeft.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mLeftActionPerformed(evt);
            }
        });

        mDown.setFont(new java.awt.Font("Symbol", 1, 14)); // NOI18N
        mDown.setText("↓");
        mDown.setToolTipText("Move Backwards");
        mDown.setMaximumSize(new java.awt.Dimension(30, 75));
        mDown.setMinimumSize(new java.awt.Dimension(30, 75));
        mDown.setPreferredSize(new java.awt.Dimension(30, 75));
        mDown.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mDownActionPerformed(evt);
            }
        });

        mRight.setFont(new java.awt.Font("Symbol", 1, 14)); // NOI18N
        mRight.setText("→");
        mRight.setMaximumSize(new java.awt.Dimension(30, 75));
        mRight.setMinimumSize(new java.awt.Dimension(30, 75));
        mRight.setPreferredSize(new java.awt.Dimension(30, 75));
        mRight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mRightActionPerformed(evt);
            }
        });

        panRight.setText("<html>Pan <br/>Right</html>");
        panRight.setMaximumSize(new java.awt.Dimension(95, 30));
        panRight.setMinimumSize(new java.awt.Dimension(95, 30));
        panRight.setPreferredSize(new java.awt.Dimension(95, 30));
        panRight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                panRightActionPerformed(evt);
            }
        });

        panLeft.setText("<html>Pan <br/>Left</html>");
        panLeft.setMaximumSize(new java.awt.Dimension(95, 30));
        panLeft.setMinimumSize(new java.awt.Dimension(95, 30));
        panLeft.setPreferredSize(new java.awt.Dimension(95, 30));
        panLeft.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                panLeftActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panUp, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(panDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(mLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mDown, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mUp, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mRight, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(panRight, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(camSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(camSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panDown, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(mUp, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mLeft, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mRight, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mDown, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panRight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panLeft, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(78, 78, 78))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void camSpeedStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_camSpeedStateChanged
        settings.setCamSpeed(camSpeed.getValue());
        evacSim.getFlyByCamera().setMoveSpeed(camSpeed.getValue());
    }//GEN-LAST:event_camSpeedStateChanged

    private void panUpActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panUpActionPerformed
        evacSim.moveCamC("FLYCAM_Rise");
    }//GEN-LAST:event_panUpActionPerformed

    private void panDownActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panDownActionPerformed
        evacSim.moveCamC("FLYCAM_Lower");
    }//GEN-LAST:event_panDownActionPerformed

    private void mUpActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mUpActionPerformed
        evacSim.moveCamC("FLYCAM_Forward");
    }//GEN-LAST:event_mUpActionPerformed

    private void mLeftActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mLeftActionPerformed
        evacSim.moveCamC("FLYCAM_StrafeLeft");
    }//GEN-LAST:event_mLeftActionPerformed

    private void mDownActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mDownActionPerformed
        evacSim.moveCamC("FLYCAM_Backward");
    }//GEN-LAST:event_mDownActionPerformed

    private void mRightActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mRightActionPerformed
        evacSim.moveCamC("FLYCAM_StrafeRight");
    }//GEN-LAST:event_mRightActionPerformed

    private void panRightActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panRightActionPerformed
        evacSim.moveCamC("FLYCAM_Right");
    }//GEN-LAST:event_panRightActionPerformed

    private void panLeftActionPerformed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_panLeftActionPerformed
        evacSim.moveCamC("FLYCAM_Left");
    }//GEN-LAST:event_panLeftActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider camSpeed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton mDown;
    private javax.swing.JButton mLeft;
    private javax.swing.JButton mRight;
    private javax.swing.JButton mUp;
    private javax.swing.JButton panDown;
    private javax.swing.JButton panLeft;
    private javax.swing.JButton panRight;
    private javax.swing.JButton panUp;
    // End of variables declaration//GEN-END:variables
}