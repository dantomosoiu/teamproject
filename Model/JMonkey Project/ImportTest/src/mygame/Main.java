package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.awt.event.ActionListener;
import jme3tools.navmesh.NavMesh;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    private ActionListener actionListener = new ActionListener() {
    @Override
    public void onAction(String name, boolean pressed, float tpf) {
      // toggle wireframe
      if (name.equals("toggle wireframe") && !pressed) {
        wireframe = !wireframe; // toggle boolean
        mat.getAdditionalRenderState().setWireframe(wireframe); 
      }
      // else ... other input tests.
    }
  };

    @Override
    public void simpleInitApp() {
        Spatial ship = assetManager.loadModel("Models/ts/ts.j3o");
        
        //Node node;
        //node = (Node) ship;
        //System.out.println(node.getChildren().size());
        
        //for(Spatial node.getChildren();
        
        Geometry shipGeo = (Geometry) ship;
        
        
        
        Mesh shipMesh = shipGeo.getMesh();
        NavMesh shipNM = new NavMesh();
        shipNM.loadFromMesh(shipMesh);
        
        rootNode.attachChild(ship);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
