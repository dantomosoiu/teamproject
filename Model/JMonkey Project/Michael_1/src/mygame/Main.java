package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import jme3tools.navmesh.Cell;
import jme3tools.navmesh.NavMesh;
import jme3tools.navmesh.util.NavMeshGenerator;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    boolean wireframe;
    Material mat;
    boolean solid = false;
    public static NavMesh shipNM;
    public static Node root;
    public static Geometry[] AgentGeometries;

    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        //Sets up material, font, rootNode and CameraSpeed
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        rootNode.setCullHint(Spatial.CullHint.Never);
        this.flyCam.setMoveSpeed(this.flyCam.getMoveSpeed()*10);

        //Load Ship Model
        Spatial ship = assetManager.loadModel("Models/Export1/Export1.j3o");

        //Make a NavMesh from the ship Model
        Node shipNode = (Node) ship;
        shipNode = (Node) shipNode.getChildren().get(0);
        
        Geometry shipGeom = (Geometry) shipNode.getChildren().get(0);
        Mesh shipMesh = shipGeom.getMesh();
        if ((shipMesh = new NavMeshGenerator().optimize(shipMesh))!= null) {
        shipNM = new NavMesh();
        shipNM.loadFromMesh(shipMesh, new Vector3f(1,1,1));

        //Holder Nodes for Navmesh / Coords (Allows easy adding / removing)
        Node navMeshHolder = new Node();
        rootNode.attachChild(navMeshHolder);
        Node coOrdsHolder = new Node();
        rootNode.attachChild(coOrdsHolder);

        //Initially sets Min/Max Variables to Highest/Lowest values
        Vector3f minX = Vector3f.POSITIVE_INFINITY
            , minY = Vector3f.POSITIVE_INFINITY
            , minZ = Vector3f.POSITIVE_INFINITY
            , maxX = Vector3f.NEGATIVE_INFINITY
            , maxY = Vector3f.NEGATIVE_INFINITY
            , maxZ = Vector3f.NEGATIVE_INFINITY;

        //Loops over Every Cell in NavMesh
        for (Cell c : shipNM.getCells()) {
            //Loops over the 3 vertices
            for (int j = 0; j < c.getVertices().length; j++) {
                //sets up two needed vertices
                Vector3f v2, v = c.getVertex(j);
                if (j < c.getVertices().length-1) { v2 = c.getVertex(j+1); }
                else { v2 = c.getVertex(0); }
                
                //Checks if new Lowest / Highest Values
                if (v.x > maxX.x) { maxX = v; }
                if (v.x < minX.x) { minX = v; }
                if (v.y > maxY.y) { maxY = v; }
                if (v.y < minY.y) { minY = v; }
                if (v.z > maxZ.z) { maxZ = v; }
                if (v.z < minZ.z) { minZ = v; }
                
                //Adds CoOrds Text to Node
                BitmapText helloText = new BitmapText(guiFont, false);
                helloText.setSize(0.2f);
                helloText.setText(v.toString());
                helloText.setColor(ColorRGBA.Green);
                helloText.setLocalTranslation(v.x, v.y, v.z);
                coOrdsHolder.attachChild(helloText);
                
                //Adds Line to navMeshHolder
                Mesh lineMesh = new Mesh();
                lineMesh.setMode(Mesh.Mode.Lines);
                lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v.x, v.y, v.z, v2.x, v2.y, v2.z});
                lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
                lineMesh.updateBound();
                lineMesh.updateCounts();
                Geometry lineGeometry = new Geometry("line", lineMesh);
            
                lineGeometry.setMaterial(mat);
                navMeshHolder.attachChild(lineGeometry);
                
            }

        }
        //Prints debug Numbers
        System.out.println("X range: " + (maxX.x - minX.x));
        System.out.println("Y range: " + (maxY.y - minY.y));
        System.out.println("Z range: " + (maxZ.z - minZ.z));
        
        System.out.println("minX " + minX.toString());
        System.out.println("minY " + minY.toString());
        System.out.println("minZ " + minZ.toString());
        System.out.println("maxX " + maxX.toString());
        System.out.println("maxY " + maxY.toString());
        System.out.println("maxZ " + maxZ.toString());
        }
        //Adds Origin Text
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.4f);
        helloText.setText("ORIGIN");
        helloText.setColor(ColorRGBA.Green);
        helloText.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(helloText);
        
        

    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
}
