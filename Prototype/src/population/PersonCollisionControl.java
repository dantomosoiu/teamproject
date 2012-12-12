/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;

/**
 *
 * @author michael
 */
public class PersonCollisionControl extends GhostControl implements PhysicsTickListener{

    public PersonCollisionControl() {
    }

    public PersonCollisionControl(CollisionShape shape) {
        super(shape);
    }

    

    
  

    public void prePhysicsTick(PhysicsSpace space, float tpf) {
        System.out.println(this.getOverlappingCount());
    }

    public void physicsTick(PhysicsSpace space, float tpf) {
       
    }
    
}
