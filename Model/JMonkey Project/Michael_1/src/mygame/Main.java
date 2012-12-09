package mygame;

import Population.Population;
import com.bulletphysics.collision.shapes.TriangleShape;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
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
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.scene.VertexBuffer;
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

        inputManager.addMapping("toggle wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(actionListener, "toggle wireframe");
        rootNode.setCullHint(Spatial.CullHint.Never);
        this.flyCam.setMoveSpeed(this.flyCam.getMoveSpeed()*10);

        Spatial ship = assetManager.loadModel("Models/NoHullScaled/Export.j3o");


        Node node;
        node = (Node) ship;
        Node chil1 = (Node) node.getChildren().get(4);
        Geometry chil = (Geometry) chil1.getChildren().get(0);
        Vector3f scale = chil1.getLocalScale();
        

        Mesh shipMesh = chil.getMesh();
        shipNM = new NavMesh();

        NavMeshGenerator generator = new NavMeshGenerator();

        Mesh optimisedMesh = generator.optimize(shipMesh);

        shipNM.loadFromMesh(optimisedMesh);

        Node navMeshHolder = new Node();
        navMeshHolder.setLocalScale(scale);
        rootNode.attachChild(navMeshHolder);
        
        Node coOrdsMeshHolder = new Node();
        coOrdsMeshHolder.setLocalScale(scale);
        rootNode.attachChild(coOrdsMeshHolder);


//        Mesh shipMesh = chil.getMesh();
//        shipNM = new NavMesh();
//        
//        
//        shipNM.loadFromMesh(shipMesh);
//        
//        Node navMeshHolder = new Node();
//        rootNode.attachChild(navMeshHolder);

        setUpLight();
        node = rootNode;

        Vector3f minX = new Vector3f(1000f, 1000f, 1000f);
        Vector3f minY = new Vector3f(1000f, 1000f, 1000f);
        Vector3f minZ = new Vector3f(1000f, 1000f, 1000f);
        Vector3f maxX = new Vector3f(-1000f, -1000f, -1000f);
        Vector3f maxY = new Vector3f(-1000f, -1000f, -1000f);
        Vector3f maxZ = new Vector3f(-1000f, -1000f, -1000f);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        
        for (int i = 0; i < shipNM.getNumCells(); i++) {
            TriangleShape tr;
            Vector3f v0 = shipNM.getCell(i).getVertex(0);
            Vector3f v1 = shipNM.getCell(i).getVertex(1);
            Vector3f v2 = shipNM.getCell(i).getVertex(2);

            if (v0.x > maxX.x) {
                maxX = v0;
            }
            if (v1.x > maxX.x) {
                maxX = v1;
            }
            if (v2.x > maxX.x) {
                maxX = v2;
            }
            if (v0.x < minX.x) {
                minX = v0;
            }
            if (v1.x < minX.x) {
                minX = v1;
            }
            if (v2.x < minX.x) {
                minX = v2;
            }

            if (v0.y > maxY.y) {
                maxY = v0;
            }
            if (v1.y > maxY.y) {
                maxY = v1;
            }
            if (v2.y > maxY.y) {
                maxY = v2;
            }
            if (v0.y < minY.y) {
                minY = v0;
            }
            if (v1.y < minY.y) {
                minY = v1;
            }
            if (v2.y < minY.y) {
                minY = v2;
            }

            if (v0.z > maxZ.z) {
                maxZ = v0;
            }
            if (v1.z > maxZ.z) {
                maxZ = v1;
            }
            if (v2.z > maxZ.z) {
                maxZ = v2;
            }
            if (v0.z < minZ.z) {
                minZ = v0;
            }
            if (v1.z < minZ.z) {
                minZ = v1;
            }
            if (v2.z < minZ.z) {
                minZ = v2;
            }


            Mesh lineMesh = new Mesh();
            lineMesh.setMode(Mesh.Mode.Lines);
            lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v0.x, v0.y, v0.z, v1.x, v1.y, v1.z});
            lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
            lineMesh.updateBound();
            lineMesh.updateCounts();
            Geometry lineGeometry = new Geometry("line", lineMesh);
            
            lineGeometry.setMaterial(mat);
            navMeshHolder.attachChild(lineGeometry);

            lineMesh = new Mesh();
            lineMesh.setMode(Mesh.Mode.Lines);
            lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v0.x, v0.y, v0.z, v2.x, v2.y, v2.z});
            lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
            lineMesh.updateBound();
            lineMesh.updateCounts();
            lineGeometry = new Geometry("line", lineMesh);
            lineGeometry.setMaterial(mat);
            navMeshHolder.attachChild(lineGeometry);

            lineMesh = new Mesh();
            lineMesh.setMode(Mesh.Mode.Lines);
            lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{v2.x, v2.y, v2.z, v1.x, v1.y, v1.z});
            lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
            lineMesh.updateBound();
            lineMesh.updateCounts();
            lineGeometry = new Geometry("line", lineMesh);
            lineGeometry.setMaterial(mat);
            navMeshHolder.attachChild(lineGeometry);



            guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
            BitmapText helloText = new BitmapText(guiFont, false);
            helloText.setSize(0.04f);
            helloText.setText(v0.toString());
            helloText.setColor(ColorRGBA.Green);
            helloText.setLocalTranslation(v0.x, v0.y, v0.z);
            coOrdsMeshHolder.attachChild(helloText);
            
            helloText = new BitmapText(guiFont, false);
            helloText.setSize(0.04f);
            helloText.setText(v1.toString());
            helloText.setColor(ColorRGBA.Green);
            helloText.setLocalTranslation(v1.x, v1.y, v1.z);
            coOrdsMeshHolder.attachChild(helloText);
            
            helloText = new BitmapText(guiFont, false);
            helloText.setSize(0.04f);
            helloText.setText(v2.toString());
            helloText.setColor(ColorRGBA.Green);
            helloText.setLocalTranslation(v2.x, v2.y, v2.z);
            coOrdsMeshHolder.attachChild(helloText);

        }
        
        //rootNode.attachChild(chil);

//        Population population = new Population(rootNode, shipNM, this);
//        int populationSize = 1;
//        AgentGeometries = new Geometry[populationSize];
//        population.populate(populationSize);
//        
//        population.evacuate();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.4f);
        helloText.setText("ORIGIN");
        helloText.setColor(ColorRGBA.Green);
        helloText.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(helloText);





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
    
    private void setUpLight() {
    // Add light so we see the scene
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(5f));
    rootNode.addLight(al);
 
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(5.6f, -5.6f, -5.6f).normalizeLocal());
    rootNode.addLight(dl);
  }

    @Override
    public void simpleUpdate(float tpf) {
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
