/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init.settingsHelpers;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author hector
 */
public class CamLoc {
    
    public Vector3f loc;
    public Quaternion rot;
    
    public CamLoc() {
        loc = new Vector3f(0,0,10);
        rot = new Quaternion(0, 1, 0, 0);
    }
    
    public CamLoc(Vector3f l, Quaternion q) {
        loc = l;
        rot = q;
    }

    public Vector3f getLoc() {
        return loc;
    }

    public Quaternion getRot() {
        return rot;
    }

    public void setLoc(Vector3f loc) {
        this.loc = loc;
    }

    public void setRot(Quaternion rot) {
        this.rot = rot;
    }
    
    
}
