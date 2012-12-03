package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import jme3tools.navmesh.NavMesh;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    boolean wireframe;
    Material mat;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        inputManager.addMapping("toggle wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(actionListener, "toggle wireframe");
        
        Spatial ship = assetManager.loadModel("Models/AccurateModelExp/AccurateModelExp.j3o");
        
        
        Node node;
        node = (Node) ship;
        Node chil1 = (Node) node.getChildren().get(2);
        Geometry chil = (Geometry) chil1.getChildren().get(0);
        
        System.out.println(chil1.getChildren().size());
        mat = chil.getMaterial();
        rootNode.attachChild(chil1);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
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
    
}
