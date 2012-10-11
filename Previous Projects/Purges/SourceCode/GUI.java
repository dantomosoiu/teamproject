// These Libraries are reqquired for the basic GUI components
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*; 

// This let's us use HashMaps and ArrayLists .etc
import java.util.*;

// These libraries are for file manipulation
import java.io.File;
import java.net.MalformedURLException;

// These are the Java3D libraries for the 3D stuff
import java.applet.Applet;
import com.sun.j3d.utils.applet.MainFrame; 
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import javax.media.j3d.*;
import javax.vecmath.*;

// This library is for importing DXF 3D model files
import de.raida.cad.loader.dxfloader.DXFLoader;

/**
* This class is the main controller for the other PURGES
* classes in the PURGES Evacuation Simulator. It also
* handles the Graphical User Interface and ties the 
* logical 3D model to the viewable 3D model.
*
* @author John Urquhart Ferguson
* @version 0.11
* @see Graph
* @see Vertex
* @see SplashWindow
* @see GraphMaker
* @see Person
*/
/*
* Last Modified: 16th March, 2006: 16:50 GMT
*
* Status: Has worked correctly for all test input.
*
* Notes: 
* There is almost no error checking in this class.
* It is assumed that everything is used correctly and with
* sensible parameters. It's not thread safe at the moment.
*/


// Extend JFrame to supply window
public class GUI extends JFrame
{
	
	// Swing stuff //////////////////////////////
		// 3D display
		private JPanel simOutputPane;
		private Purges simOutput;
		private JSlider rotSlider;
		private float rotary = 0;
		private int rotPrev = 0;
		private static boolean useMouse = false;
		
		// The display panels for the various UI components
		private JPanel popPane;
		private JPanel leftTopPane1;
		private JPanel leftTopPane2;
		private JPanel leftTopPane3;
		private JPanel sliderPane;
		private JPanel frameRatePane;
		private JPanel leftPane;
		private JPanel startPane;		
		private JFrame silly;
		private JMenuItem blocker;		
		
		// Text entry field for the population size
		private TextField popEntry = new TextField("", 3);
		
		// Timer label
		private JLabel timerLabel;
		private long clockTime;
		private long diff;
		
		// Button to start simulation
		private JButton startButton;
	// end Swing stuff //////////////////////////
	
	
	// Simulator variables  //////////////////////
		// The filename of the Purges file
		private String filename = null;
		
		// Create a new Graph to store theoretical room
		private Graph g1 = new Graph();
		
		// Creat a new HashMap for easier lookup of Vertices in Graph
		private HashMap hm = new HashMap();
		
		// These are required for accessing the various values made by the GraphMaker
		private GraphMaker handler;
		private String threeDfileName;
		private int frameRate;
		private int populationSize;
		private float scaler;
		private ArrayList population = null;
		private FileProc fp = null;
		
		// This is to keep the timer display in sync
		private int simSpeed = 1;
	// end Simulator variables  //////////////////
	
	
	// Variables for the 3D /////////////////////
		// This is the class global variable that we need to add people 'boxes' to
		private TransformGroup tmpTransformGroup;
		
		// This is the class global variable that we need to add people 'boxes' to
		private BranchGroup tmpRootGeometryNode;
		
		// BranchGroup ArrayList for our boxes
		private static ArrayList boxBranchGs = new ArrayList();
		
		// TransformGroup ArrayList for our boxes
		private static ArrayList boxTransGs = new ArrayList();
		
		// HashMap to match TransformGroups to People
		private static HashMap peopleToTg = new HashMap();
		
		// HashMap to match BranchGroups to People
		private static HashMap peopleToBg = new HashMap();
		
		// Whether or not to use wireframe for location (false for wireframe)
		private static boolean bShadedModel = true;
	// end Variables for the 3D /////////////////
	
	
	/**
	* This class is for handling the 3D aspects of the 
	* simulator.
	*
	* @author John Urquhart Ferguson
	* @version 0.04
	* @see GUI
	*/
	private class Purges extends Applet
	{
		
		/**
		* This class is for making the boxes that 
		* represent the People in the simulation.
		*
		* @author John Urquhart Ferguson
		* @version 0.01
		* @see Purges
		*/
		private class SmallNBox extends Shape3D
		{		
			
			// ************ Constructors ************
			
			/**
			* Constructor if specified arguments are supplied.
			*
			* @param pin  The centred real world coordinate of the box.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			SmallNBox(Point3f pin, float scaler)
			{
				// Make all the sides and add them
	            this.setGeometry(front(pin, scaler));
				this.addGeometry(back(pin, scaler));
				this.addGeometry(left(pin, scaler));
				this.addGeometry(right(pin, scaler));
				this.addGeometry(bottom(pin, scaler));
				this.addGeometry(top(pin, scaler));
	            this.setAppearance(smallNBoxAppearance());
			}
			
			// ************ end Constructors ************
			
			
			/**
			* Create one side of the box.
			*
			* @param pin  The centred real world coordinate of the box.
			* @return  The created Geometry of this side of the SmallNBox.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			private Geometry front(Point3f pin, float scaler)
			{
				QuadArray tsa;
				Point3f coords[] = new Point3f[4];
				
				// set the central points for four triangle fan strips
				coords[0] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[3] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z-(scaler*0.09f)) );               
				coords[1] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[2] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				
				tsa = new QuadArray (4, QuadArray.COORDINATES);
				tsa.setCoordinates(0, coords);
				
				return tsa;
			}//end front()
			
			
			/**
			* Create one side of the box.
			*
			* @param pin  The centred real world coordinate of the box.
			* @return  The created Geometry of this side of the SmallNBox.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			private Geometry back(Point3f pin, float scaler)
			{			
				QuadArray tsa;
				Point3f coords[] = new Point3f[4];
				
				// set the central points for four triangle fan strips
				coords[2] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[3] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z-(scaler*0.09f)) );               
				coords[1] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[0] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				
				tsa = new QuadArray (4, QuadArray.COORDINATES);
				tsa.setCoordinates(0, coords);
		
				return tsa;	
			}//end back()
			
			
			/**
			* Create one side of the box.
			*
			* @param pin  The centred real world coordinate of the box.
			* @return  The created Geometry of this side of the SmallNBox.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			private Geometry left(Point3f pin, float scaler)
			{					
				QuadArray tsa;
				Point3f coords[] = new Point3f[4];
				
				// set the central points for four triangle fan strips
				coords[2] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[1] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z+(scaler*0.03f)) );                
				coords[3] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[0] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				
				tsa = new QuadArray (4, QuadArray.COORDINATES);
				tsa.setCoordinates(0, coords);
				
				return tsa;	
			}//end left()
			
			
			/**
			* Create one side of the box.
			*
			* @param pin  The centred real world coordinate of the box.
			* @return  The created Geometry of this side of the SmallNBox.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			private Geometry right(Point3f pin, float scaler)
			{					
				QuadArray tsa;
				Point3f coords[] = new Point3f[4];
				
				// set the central points for four triangle fan strips			
				coords[0] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				coords[1] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z-(scaler*0.09f)) );                
				coords[3] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				coords[2] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				
				tsa = new QuadArray (4, QuadArray.COORDINATES);
				tsa.setCoordinates(0, coords);
				
				return tsa;	
			}//end right()
			
			
			/**
			* Create one side of the box.
			*
			* @param pin  The centred real world coordinate of the box.
			* @return  The created Geometry of this side of the SmallNBox.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			private Geometry bottom(Point3f pin, float scaler)
			{
				QuadArray tsa;
				Point3f coords[] = new Point3f[4];
				
				// set the central points for four triangle fan strips
				coords[0] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				coords[3] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z-(scaler*0.09f)) );               
				coords[1] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[2] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y-(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				
				tsa = new QuadArray (4, QuadArray.COORDINATES);
				tsa.setCoordinates(0, coords);
				
				return tsa;
			}//end bottom()
			
			
			/**
			* Create one side of the box.
			*
			* @param pin  The centred real world coordinate of the box.
			* @return  The created Geometry of this side of the SmallNBox.
			* @param scaler  The percentile that people need to be scaled to for the model.
			*/
			private Geometry top(Point3f pin, float scaler)
			{
				QuadArray tsa;
				Point3f coords[] = new Point3f[4];
				
				// set the central points for four triangle fan strips
				coords[2] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z-(scaler*0.09f)) );
				coords[3] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z-(scaler*0.09f)) );               
				coords[1] = new Point3f( (pin.x+(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				coords[0] = new Point3f( (pin.x-(scaler*0.03f)), (pin.y+(scaler*0.03f)), (pin.z+(scaler*0.03f)) );
				
				tsa = new QuadArray (4, QuadArray.COORDINATES);
				tsa.setCoordinates(0, coords);
				
				return tsa;
			}//end top()
			
			
			/**
			* Set the appearance of the box.
			*
			* @return  The created Appearance of the SmallNBox.
			*/
			private Appearance smallNBoxAppearance()
			{
				Appearance appearance = new Appearance();
				PolygonAttributes polyAttrib = new PolygonAttributes();
				
				polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
				appearance.setPolygonAttributes(polyAttrib);
				
				return appearance;
			}//end smallNBoxAppearance()
			
		}//end class SmallNBox
		
		
		// ************ Constructors ************
		
		/**
		* Default constructor if no arguements supplied.
		*/
		Purges () 
		{
			// Prepare the canvas
			setLayout(new BorderLayout());
			Canvas3D tmpCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
			add(BorderLayout.CENTER, tmpCanvas3D);
			
			// Create the main branch group
			tmpRootGeometryNode = new BranchGroup();
			tmpRootGeometryNode.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			tmpRootGeometryNode.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			tmpRootGeometryNode.setCapability(BranchGroup.ALLOW_DETACH);
			tmpRootGeometryNode.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tmpRootGeometryNode.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);			
			
			// Create the TransformGroup for the rotation of the geometry
			tmpTransformGroup = new TransformGroup();
			tmpTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tmpTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			tmpTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
			tmpTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
			
			// Set up transform group
			tmpTransformGroup.addChild(tmpRootGeometryNode);
			
			// Add mouse rotate capabilities to Transform group if set at command line
			if (useMouse) {
				MouseRotate tmpMouseRotateModel = new MouseRotate();
				tmpMouseRotateModel.setTransformGroup(tmpTransformGroup);
				tmpMouseRotateModel.setSchedulingBounds(new BoundingBox());
				tmpRootGeometryNode.addChild(tmpMouseRotateModel);
				tmpRootGeometryNode.compile();
			}
			
			// Create the root node, which is put into the SimpleUniverse
			BranchGroup tmpRootNode = new BranchGroup();
			tmpRootNode.addChild(tmpTransformGroup);
			
			// Possibly add light to the root node depending on the bShadedModel global variable.
			// The light is not rotated with the rest of the model
			if(bShadedModel){
				AmbientLight tmpAmbientLight = new AmbientLight(new Color3f(0.2f, 0.2f, 0.2f));
				tmpAmbientLight.setInfluencingBounds(new BoundingBox());
				tmpRootNode.addChild(tmpAmbientLight);
				
				DirectionalLight tmpDirectionalLight = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f));
				tmpDirectionalLight.setInfluencingBounds(new BoundingBox());
				tmpRootNode.addChild(tmpDirectionalLight);
			}
			
			// Create the SimpleUniverse
			SimpleUniverse tmpSimpleUniverse = new SimpleUniverse(tmpCanvas3D);
			tmpSimpleUniverse.getViewingPlatform().setNominalViewingTransform();
			tmpSimpleUniverse.addBranchGraph(tmpRootNode);	
		}
		
		// ************ end Constructors ************
		
		
		/**
		* Create a BranchGroup scene graph for a box.
		*
		* @param pin  The central point of the box in the real world.
		* @return  The created BranchGroup scene graph.	
		* @param scaler  The percentile to scale people by for this model.
		*/
		public BranchGroup createBox(Point3f pin, float scaler)
		{
			BranchGroup scene = new BranchGroup();
			scene.addChild(new SmallNBox(pin, scaler));
			return scene;
		}//end createBox()
		
		
		/**
		* Add people 'boxes' to the 3D model.
		*
		* @param population  The people who require 3D representation.
		* @param scaler  The percentile to scale people by for this model.
		*/
		public void addPopulation(ArrayList population, float scaler)
		{
			// Populate the scene with people
			for (int i = 0; i < populationSize; i++) {
				// Set up the transform and branch groups
				boxTransGs.add(i, new TransformGroup());
				boxBranchGs.add(i, new BranchGroup());
				((BranchGroup)boxBranchGs.get(i)).setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
				((BranchGroup)boxBranchGs.get(i)).setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
				((BranchGroup)boxBranchGs.get(i)).setCapability(BranchGroup.ALLOW_DETACH);
				((TransformGroup)boxTransGs.get(i)).setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				((TransformGroup)boxTransGs.get(i)).setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
				
				// Update HashMap so that we can change the Transform and Branch groups later
				peopleToTg.put( ((Person)population.get(i)).getName(), ((TransformGroup)boxTransGs.get(i)) );
				peopleToBg.put( ((Person)population.get(i)).getName(), ((BranchGroup)boxBranchGs.get(i)) );
				
				// Add the people to their own Transform group and Branch group
				((TransformGroup)boxTransGs.get(i)).addChild( createBox(((Person)population.get(i)).getPoint(), scaler) );			
				((BranchGroup)boxBranchGs.get(i)).addChild( ((TransformGroup)boxTransGs.get(i)) );
				
				// Add person to the scene
				tmpTransformGroup.addChild( ((BranchGroup)boxBranchGs.get(i)) );
			}
		}
		
		
		/**
		* Add 3D model to the 3D scene.
		*
		* @param threeDfName  The location of the 3D DXF model file.
		* @param cameraRotX  Camera offset on the X-axis.
		* @param cameraRotY  Camera offset on the Y-axis.
		* @param cameraRotZ  Camera offset on the Z-axis.
		*/
		public void addModel(String threeDfName, float cameraRotX, float cameraRotY, float cameraRotZ)
		{
			// Get rid of old model if it's there.
			if (tmpRootGeometryNode != null) {
				tmpRootGeometryNode.detach();
				tmpRootGeometryNode = null;
			}
			
			// Load the 3D model file
			DXFLoader tmpLoader = new DXFLoader();
			try {
				tmpLoader.loadFile(new File(threeDfName).toURL().getFile());
			} catch(MalformedURLException tmpException){
				tmpException.printStackTrace();
				return;
			}
	
			// Activate all layers of the DXF model
			Switch tmpSwitch = tmpLoader.getSwitch(bShadedModel, false);
			tmpSwitch.setWhichChild(Switch.CHILD_MASK);
			tmpSwitch.setChildMask(tmpLoader.getLayerMask());
	
			// Create the BranchGroup for the geometry
			tmpRootGeometryNode = new BranchGroup();
			tmpRootGeometryNode.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
			tmpRootGeometryNode.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
			tmpRootGeometryNode.setCapability(BranchGroup.ALLOW_DETACH);
			tmpRootGeometryNode.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tmpRootGeometryNode.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);	
			tmpRootGeometryNode.addChild(tmpSwitch);
			
			// Set up transform group
			tmpTransformGroup.addChild(tmpRootGeometryNode);
			
			// Set up the camera offset (the default is looking straight down on model from above
			Transform3D rotateX = new Transform3D();
			Transform3D rotateY = new Transform3D();
			Transform3D rotateZ = new Transform3D();
			Transform3D rotateWorld = new Transform3D();
			rotateX.rotX(cameraRotX);
			rotateY.rotY(cameraRotY);
			rotateZ.rotZ(cameraRotZ);
			rotateWorld.mul(rotateX);
			rotateWorld.mul(rotateY);
			rotateWorld.mul(rotateZ);
			TransformGroup contentTransformGroup = new TransformGroup(rotateWorld);
			tmpTransformGroup.setTransform(rotateWorld);			
		}
		
	}//end class Purges
	
	
	/**
	* Tell the program to exit when we click the close button
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class WinHandler extends WindowAdapter
	{
		public void windowClosing (WindowEvent e)
		{
			// Make sure the temp 3D model file is deleted as soon as we've finished with it
			try {
				File current3D = new File(threeDfileName);
				if (current3D.exists()) {
					current3D.delete();
				}
			} catch (Exception ex) {}
			
			dispose(); 
			System.exit(0); 
		}
	}//end class WinHandler
	
	
	/**
	* Tell the program to exit when we click the File->Exit menu option
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class ExitHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Make sure the temp 3D model file is deleted as soon as we've finished with it
			try {
				File current3D = new File(threeDfileName);
				if (current3D.exists()) {
					current3D.delete();
				}
			} catch (Exception ex) {}
			
			dispose(); 
			System.exit(0); 
		}
	}//end class ExitHandler
	
	
	/**
	* Tell the Block Area window to exit when we click the close button
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class BlockWinHandler extends WindowAdapter
	{
		public void windowClosing (WindowEvent e)
		{
			silly.dispose(); 
			silly = null;
		}
	}//end class BlockWinHandler
	
	/**
	* Tell the program to show the credits when we click the Help->About menu option
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class AboutHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Show dialog window
			JOptionPane.showMessageDialog(new Frame(),
			"                P.U.R.G.E.S.\n" +
			"Public Underground Railway\nGeneral Evacuation Simulator\n" +
			"------------------------------------------\n" +
			"Created by:\nJohn Urquhart Ferguson\n" +
			"<html><a href=\"http://www.sympodius.com\">http://www.sympodius.com</a></html>",
			"About Purges",
			JOptionPane.INFORMATION_MESSAGE);
		}
	}//end class AboutHandler
	
	
	/**
	* Tell the program to show the credits when we click the Help->Quick Help menu option
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class HelpHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Show dialog window
			JOptionPane.showMessageDialog(new Frame(),
			"                                Quick Help\n\n" +
			
			"Purges is an evacuation simulator produced \n" +
			"primarily for use in the Glasgow Subway System.\n\n" +
			"To get started, select Open from the File menu \n" +
			"and choose a 'pur' file. Once it has loaded, set\n" +
			"the population size for the simulation, then hit \n" +
			"the 'Start Simulation' button.\n\n" +
			
			"You can also block areas in some environments \n" +
			"by choosing the 'Block Areas' option from the \n" +
			"option menu. You can then select which areas \n" +
			"to block or unblock. If the 'Block Areas' option \n" +
			"is a light grey in the options menu, the current \n" +
			"environment does not support blockable areas.\n\n" +
			
			"---------------------------------------------------------------------\n" +
			"Created by John Urquhart Ferguson at the \n" +
			"University of Glasgow.",
			"Purges Quick Help",
			JOptionPane.INFORMATION_MESSAGE);
		}
	}//end class HelpHandler
	
	
	/**
	* Deal with opening a new file
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class FileOpenHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Create a new file open dialog.
			// There is no error checking here and it assumed that a correctly
			// formatted Purges file will be choosen.
			Frame f = new Frame();
			FileDialog d = new FileDialog(f, "Open");
			d.setVisible(true);
			String selectedItem = d.getFile();
			String selectedLoc = d.getDirectory();
			
			if (selectedItem == null) {
				selectedItem = "No file selection.";
				filename = null;
			} else {
				filename = selectedLoc + selectedItem;
				
				// Process file
				fp = new FileProc();
				fp.start();
			}
		}
	}//end class FileOpenHandler
	
	
	/**
	* Deal with the start simulation button being pressed
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class StartButtHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Start simulation and change button to a Pause button
			if ( (ae.getActionCommand()).equals("Start Simulation") ) {
				clockTime = System.currentTimeMillis();
				diff = 0;
				(new RunSimulation()).start();
				popEntry.setEnabled(false);
				startButton.setText("Pause Simulation");
				startButton.setActionCommand("Pause Simulation");
			}
			// Pause simulation and change button to a Resume button
			else if ( (ae.getActionCommand()).equals("Pause Simulation") ) {
				startButton.setText("Resume Simulation");
				startButton.setActionCommand("Resume Simulation");
			}
			// Resume simulation and change button to a Pause button
			else if ( (ae.getActionCommand()).equals("Resume Simulation") ) {
				startButton.setText("Pause Simulation");
				startButton.setActionCommand("Pause Simulation");
			}
		}
	}//end class StartButtHandler
	
	
	/**
	* Deal with the radio button group selection
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class RadioButtHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Change the speed of the simulator by adjusting the simSpeed variable
			if ( (ae.getActionCommand()).equals("one") ) {
				simSpeed = 1;
			}
			else if ( (ae.getActionCommand()).equals("two") ) {
				simSpeed = 2;
			}
			else if ( (ae.getActionCommand()).equals("four") ) {
				simSpeed = 4;
			}
			else if ( (ae.getActionCommand()).equals("eight") ) {
				simSpeed = 8;
			}
		}
	}//end class RadioButtHandler
	
	
	/**
	* Deal with the block areas menu checkbox selections
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class BlockCheckHandler implements ItemListener
	{
		public void itemStateChanged (ItemEvent ie)
		{
			// Determine which checkbox was selected and which area that corresponds
			// to on the logical map.
			String area = ((JCheckBox)(ie.getItem())).getText() ;
			HashMap tubular = handler.getGUIBlockables();
			ArrayList flipper = (ArrayList)tubular.get( area );
			
			// Change the blocked status of the found area on the logical map
			if (ie.getStateChange() == ItemEvent.SELECTED) {
				for (int i = 0; i < flipper.size(); i++) {
					((Vertex)(flipper.get(i))).block();
					((Vertex)(flipper.get(i))).setAlwaysBlocked(true);
				}
			} else {
				for (int i = 0; i < flipper.size(); i++) {
					((Vertex)(flipper.get(i))).setAlwaysBlocked(false);
					((Vertex)(flipper.get(i))).unblock();
				}
			}
		}
	}//end class BlockCheckHandler
	
	
	/**
	* Deal with the block areas menu option
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class BlockHandler implements ActionListener
	{
		public void actionPerformed (ActionEvent ae)
		{
			// Make a new window display for the Block areas selection
			if (silly == null) {
				silly = new JFrame();
				
				// set up layout of Block areas selection window
				JLabel poo = new JLabel("Click the checkboxes below to \n block those areas of the environment.");
				JPanel plop = new JPanel(new BorderLayout());
				plop.add(poo, BorderLayout.NORTH);			
				JPanel checkPane = new JPanel(new GridLayout(0,3));
				
				// Make sure we don't allow this until Purges file has been fully processed
				if (fp != null) {
					// Get blockable areas made by Purges file and add to the Block
					// areas selection window.
					ArrayList bnames = handler.getGUIBNames();
					HashMap tubular = handler.getGUIBlockables();
					
					for (int i = 0; i < bnames.size(); i++) {
						ArrayList flipper = (ArrayList)tubular.get( (String)bnames.get(i) );
						JCheckBox quadruped = new JCheckBox((String)bnames.get(i), ((Vertex)(flipper.get(0))).isAlwaysBlocked() );
						quadruped.addItemListener(new BlockCheckHandler());
						
						checkPane.add(quadruped);					
					}
				}			
				
				// Set up scroll pane (theorhetically allowing for any number of Blockable areas 
				// with a relatively good looking GUI display.
				JScrollPane scroller = new JScrollPane(checkPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				scroller.setPreferredSize(new Dimension(200,200));
				
				silly.add(plop, BorderLayout.NORTH);
				silly.add(scroller, BorderLayout.CENTER);
				
				// Set window title
				silly.setTitle("Block Areas");
				
				// Allow window to close when cross is clicked
				silly.addWindowListener(new BlockWinHandler());
				
				// Get the screen dimensions for sizing the window
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				
				// Position window in center of screen
				int width = 450;
				int height = 200;
				Rectangle r = new Rectangle();
				r.setBounds( 10 , (screenSize.height/2)-(height/2)+100, width, height);
				silly.setBounds(r);
				
				// Display fully generated window
				silly.setVisible(true);
			}
		}
	}//end class BlockHandler
	
	
	/**
	* Deal with the slider moving on the rotation control
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class SliderListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e) {			
			JSlider source = (JSlider) e.getSource();
			int cur = source.getValue();
			
			// Check slider has a sensible value
			if ((cur < source.getMaximum()) & (cur > source.getMinimum())) {
				// Set up tranformations
				Transform3D rotateX = new Transform3D();
				Transform3D rotateY = new Transform3D();
				Transform3D rotateZ = new Transform3D();
				Transform3D rotateWorld = new Transform3D();
				if (cur - rotPrev > 0) {
					// We're rotating left
					rotary = rotary + 0.12f;
					
					// We must multiply rotation with camera offset
					// or else the camera angle offset won't be maintained
					rotateX.rotX(handler.getCameraRotX());
					rotateY.rotY(handler.getCameraRotY());
					rotateZ.rotZ(handler.getCameraRotZ() + rotary);
					
					// rotate environment
					rotateWorld.mul(rotateX);
					rotateWorld.mul(rotateY);
					rotateWorld.mul(rotateZ);
					TransformGroup contentTransformGroup = new TransformGroup(rotateWorld);
					tmpTransformGroup.setTransform(rotateWorld); 
				} else {
					// We're rotating right
					rotary = rotary - 0.12f;
					
					// We must multiply rotation with camera offset
					// or else the camera angle offset won't be maintained
					rotateX.rotX(handler.getCameraRotX());
					rotateY.rotY(handler.getCameraRotY());
					rotateZ.rotZ(handler.getCameraRotZ() + rotary);
					
					// rotate environment
					rotateWorld.mul(rotateX);
					rotateWorld.mul(rotateY);
					rotateWorld.mul(rotateZ);
					TransformGroup contentTransformGroup = new TransformGroup(rotateWorld);
					tmpTransformGroup.setTransform(rotateWorld);
				}
			}
			// Store current rotation as previous so that we know 
			// which direction we are rotating next time
			rotPrev = cur;
		}
	}//end class SliderListener
	
	
	/**
	* Seperate thread to process file (otherwise GUI freezes)
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class FileProc extends Thread
	{
		
		// Variable to show if processing of a file is in progress
		private boolean done = false;
		
		
		/**
		* Run the thread and process the Purges file
		* taken from the 'filename' variable of the GUI
		* class.
		*/
		public void run ()
		{
			// If a 3D model is already being displayed, remove it
			// (removal of population is handled in the RunSimulation thread)
			if (silly != null) {
				silly.dispose();
				silly = null;
			}
			
			//Show a loading screen while file is read in
			SplashWindow plough = createAndShowSplash("splashLoad.jpg", 100, true);
			
			// Call file processor class
			handler = new GraphMaker(filename);
			
			// Set default framerate from PURGES file if specified
			frameRate = handler.getDefaultFR();
			
			// Set default population from PURGES file if specified
			populationSize = handler.getDefaultPop();
			
			// Get the Graph and HashMap produced by GraphMaker
			g1 = handler.getGraph();
			hm = handler.getHash();
			
			// Get the 3D filename from the file processor
			threeDfileName = handler.getFileName();
			
			// Get the person scaler value produced from the GraphMaker
			scaler = handler.getScaler();
			
			// Reset rotation slider on GUI
			rotPrev = 0;
			rotSlider.setValue(0);
			rotary = 0;
			
			// Add new 3D model to 3D output pane on GUI
			simOutput.addModel(threeDfileName, handler.getCameraRotX(), handler.getCameraRotY(), handler.getCameraRotZ());
			
			// Enable the interface buttons since we now have something
			// to run simulation on
			popEntry.setEnabled(true);
			popEntry.setText(String.valueOf(populationSize));			
			startButton.setEnabled(true);
			rotSlider.setEnabled(true);
			if ( (handler.getGUIBNames()).size() > 0) {
				blocker.setEnabled(true);
			} else {
				blocker.setEnabled(false);
			}
			
			// Signal that processing is finsished if required
			done = true;
			
			// Remove the splash screen now that file has loaded
			plough.remove();
		}//end run()
		
		
		/**
		* Show if Purges file is still being processed
		*
		* @return   True if file is still being processed.
		*/
		public boolean isDone()
		{ 
			return done;
		}//end isDone()
		
	}//end class FileProc
	
	
	/**
	* Seperate thread to run simulation (otherwise GUI freezes)
	*
	* @author John Urquhart Ferguson
	* @version 0.01
	* @see GUI
	*/
	private class RunSimulation extends Thread
	{
		
		// Variable to show if simulation is in progress
		private boolean done = false;
		
		
		/**
		* QuickSort implementation for an ArrayList of the Person class.
		* The sort is based on the Monte Carlo value of the Person class.
		* This is for ordering people by aggressiveness in the Purges
		* program.
		*
		* @param lin  The list of People to be be sorted.
		* @return  The sorted ArrayList of People by Monte Carlo value.
		* @see Person
		*/
		private ArrayList personQSort(ArrayList lin)
		{
			// Base case for recursion
			if (lin.size() == 0 ) {
				return new ArrayList();
			} else {
				// Find a value from list and then quicksort everything
				// less than it, everything greater than it and then merge as:
				//
				// 	quickSort(less) + foundValue + quickSort(greater)
				ArrayList less = new ArrayList();
				ArrayList great = new ArrayList();
				Person middle = (Person)lin.get((int)(lin.size()/2));
				ArrayList x = new ArrayList();
				
				// Make 'less than', 'greater than' and 'equal to' lists for x
				for (int i = 0; i < lin.size(); i++) {
					if ( ((Person)lin.get(i)).getMonte() < middle.getMonte() ) {
						less.add(lin.get(i));
					}
					else if ( ((Person)lin.get(i)).getMonte() > middle.getMonte() ) {
						great.add(lin.get(i));
					}
					else {
						x.add(lin.get(i));
					}
				}
				
				// Recurrsive call to function for 'less than' list
				// and merge with our own list
				ArrayList output = new ArrayList();
				output = personQSort(less);
				for (int i = 0; i < x.size(); i++) {
					output.add(output.size(), x.get(i));
				}
				
				// Recurrsive call to function for 'greater than' list
				// and merge with our own list
				ArrayList sortedGreat = new ArrayList();
				sortedGreat = personQSort(great);
				for (int i = 0; i < sortedGreat.size(); i++) {
					output.add(output.size(), sortedGreat.get(i));
				}
				
				// Output result of sort
				return output;
			}
		}//end personQSort()
		
		
		/**
		* This is a worker function of this class. 
		* This method finds the best goal node
		* for a Person to be aiming for.
		*
		* This is a modified version of the findGoal
		* method found in the Graph class
		*
		* @param gin  An ArrayList of the possible goals to move to.
		* @param ain  The Vertex we're currently at.
		* @return  The best Vertex found.
		* @see Vertex
		*/
		private Vertex findGoal(ArrayList gin, Vertex ain)
		{
			// This works out which one of the goal Vertices
			// is closest to the current Vertex. If two goals are
			// near the same difference, the last found is picked.
			
			Vertex best = new Vertex();
			int bestScore;
			int h;		
			
			// Calculate the H value for the first reachable Vertex.
			h = (int)( ( Math.abs(((Vertex)(gin.get(0))).getX() - ain.getX()) + Math.abs(((Vertex)(gin.get(0))).getY() - ain.getY()) + Math.abs(((Vertex)(gin.get(0))).getZ() - ain.getZ()))*10);
			
			// Calculate F for first Vertex
			bestScore = h;
			best = ((Vertex)(gin.get(0)));
			
			// Test each of the remaining Vertices
			for (int i=1; i < gin.size(); i++) {
				// Calculate the H value for this reachable Vertex.
				h = (int)( ( Math.abs(((Vertex)(gin.get(i))).getX() - ain.getX()) + Math.abs(((Vertex)(gin.get(i))).getY() - ain.getY()) + Math.abs(((Vertex)(gin.get(i))).getZ() - ain.getZ()))*10);
				
				// calculate F if better than current (give or take 5 units)
				if ( h < (bestScore + 5) ) {
					bestScore = h;
					best = ((Vertex)(gin.get(i)));
				}
			}
			return best;
		}//end findGoal()
		
		
		/**
		* Makes a given quantity of time (in milliseconds) into
		* a human readable version.
		*
		* @param diff  A quantity of time given in milliseconds
		* @return  A human readable representation of the given time quantity
		*/
		private String makeReadableTime(long diff)
		{
			// Calculate the various time segments
			long millis = (long)(diff%60);
			long seconds = (long)((diff/1000)%60);
			long minutes = (long)((diff/1000)/60);
			
			// Construct the output, including extra zeros where required
			String output;
			String mil;
			String sec;
			if (millis < 10) {
				mil = "0" + String.valueOf(millis);
			} else {
				mil = String.valueOf(millis);
			}
			if (seconds < 10) {
				sec = "0" + String.valueOf(seconds);
			} else {
				sec = String.valueOf(seconds);
			}
			
			return String.valueOf(minutes) + ":" + sec + ":" + mil;
		}//end makeReadableTime()
		
		
		/**
		* Run the thread and run the simulation
		*/
		public void run ()
		{
			// Store the unsorted population in this list
			ArrayList populationUnsorted = new ArrayList();
			
			// Don't process the GUI population setter unless it is set
			if ( popEntry.getText() != null ) {
				try {
					// If GUI population setter is less than max population, set population to that
					if ( Integer.parseInt(popEntry.getText()) <= handler.getDefaultPop() ) {
						populationSize = Integer.parseInt(popEntry.getText());
					} else {
						// Population set too high, display error and exit simulation thread
						startButton.setText("Start Simulation");
						startButton.setActionCommand("Start Simulation");
						
						JOptionPane.showMessageDialog(new Frame(),
						"                       Population Size Warning\n\n" +
						"The maximum population size for this environment is " + handler.getDefaultPop() + ".",
						"Population Size Warning",
						JOptionPane.WARNING_MESSAGE);
						
						done = true;
						startButton.setText("Start Simulation");
						startButton.setActionCommand("Start Simulation");
						popEntry.setEnabled(true);					
						popEntry.setText(String.valueOf(handler.getDefaultPop()));
						
						return;
					}
				} catch (Exception e) {
					// Population not a whole number, display error and exit simulation thread
					startButton.setText("Start Simulation");
					startButton.setActionCommand("Start Simulation");
					
					JOptionPane.showMessageDialog(new Frame(),
					"                       Population Size Warning\n\n" +
					"The population size must be a whole number. \nYou cannot create fractions of people.",
					"Population Size Warning",
					JOptionPane.WARNING_MESSAGE);
					
					done = true;
					startButton.setText("Start Simulation");
					startButton.setActionCommand("Start Simulation");
					popEntry.setEnabled(true);					
					popEntry.setText(String.valueOf(handler.getDefaultPop()));
					
					return;
				}
			}
			
			// Set up the population for the simulation
			for (int i = 0; i < populationSize; i++) {
				// Randomly place people in Graph
				Vertex start = g1.getRandomVertex();
				while (start.isBlocked()) {
					start = g1.getRandomVertex();
				}
				
				// Use Monte Carlo to determine which of the specified
				// goals to assign to this person (goals are specified in
				// the file format). People will pick the closest goal to them
				// within a set limit, but will randomly determine
				// between two goals that are both close.
				Collections.shuffle( handler.getGoals() );
				Vertex end = findGoal( handler.getGoals(), start );
				
				// Create a random Monte Carlo value for the Person
				Random randyP = new Random();
				int randerP = randyP.nextInt(100);
				
				// Add the person to the general population now that we 
				// have a starting place and a goal for them.
				populationUnsorted.add(i, new Person( ("Lizzy_"+String.valueOf(i)), start, end, randerP ));
			}
			
			// Now that we've made our population, we will sort it by 
			// Monte Carlo Value. These Monte Carlo values are going
			// to be used as a measure of aggressiveness. Therefore,
			// by ordering the list by aggressiveness, the most aggressive
			// will always get the chance to move before the lesser
			// aggressive people in the animation loop.
			population = personQSort(populationUnsorted);
			
			// Add population to the 3D simulation
			simOutput.addPopulation(population, scaler);
			
			// Animate people until they've all escaped
			while (population.size() > 0) {
				// If processing file is not done, don't run simulation
				if (fp.isDone()) {
					// If user has set simulation to run, run simulation
					if ( (startButton.getActionCommand()).equals("Pause Simulation") ) {
						// Transform for the current person
						Transform3D trans = new Transform3D();
						
						// Go through population and animate towards goals
						for (int i = (population.size()-1); i > -1; i--) {
							// While not moving between squares, find next place to move to and start movement
							if ( !((Person)population.get(i)).isMoving() ) {
								
								// If person has arrived, remove him from the scene and further processing
								if ( ((Person)population.get(i)).arrived() ) {
									((Person)population.get(i)).unblock();
									((BranchGroup)peopleToBg.get( ((Person)population.get(i)).getName() )).detach();
									population.remove(i);
									popEntry.setText(String.valueOf(population.size()));
									continue;
								}
								
								// If person is stuck in a Vertex that is always blocked, there must have been a GUI block
								// of that area. Since this is supposed to simulate caveins, this person is effectively dead
								// so this person is removed from scene and further processing
								if ( (((Person)population.get(i)).getCurrent()).isAlwaysBlocked() ) {
									((BranchGroup)peopleToBg.get( ((Person)population.get(i)).getName() )).detach();
									population.remove(i);
									popEntry.setText(String.valueOf(population.size()));
									continue;
								}
								
								// Perform a* to find next square to move to for this person
								ArrayList temp = g1.astar( ((Person)population.get(i)).getCurrent(), ((Person)population.get(i)).getGoal());
								boolean inresult = false;
								
								// Find out if we have a path to goal, or just erratic movement
								for (int y = 0; y < temp.size(); y++) {
									if ( (Vertex)temp.get(y) == ((Person)population.get(i)).getGoal() ) {
										inresult = true;
									}
									if (inresult) {
										break;
									}
								}
								
								// If no path to goal found, assign a new random goal
								// Since person is now in a state of panic, the goal might
								// not be anywhere near their current position. It is just
								// the first one that 'occurred' to them.
								if (!inresult) {
									Random randy = new Random();
									int rander = randy.nextInt(100);
									int mc = 0;
									int index = 0;
									for (int j = 0; j < (handler.getGoals()).size(); j++) {
										mc = 100 - ( (j+1)*( (int)(100/(handler.getGoals()).size()) ) );
										index = ((handler.getGoals()).size()-(j+1));
										if (rander > mc) {
											break;
										}
									}
									Vertex end = (Vertex)(handler.getGoals()).get(index);
									((Person)population.get(i)).setGoal(end);
								}
								
								// Attempt to move Person
								if (temp.size() > 1) {
									((Person)population.get(i)).move((Vertex)temp.get(1));
								}
							} else {
								// We must still be moving between squares, so animate further till we get there
								((Person)population.get(i)).animate();
								float tempX = ((Person)population.get(i)).getX();
								float tempY = ((Person)population.get(i)).getY();
								float tempZ = ((Person)population.get(i)).getZ();
								trans.setTranslation(new Vector3f(tempX,tempY,tempZ));					
								((TransformGroup)peopleToTg.get( ((Person)population.get(i)).getName() )).setTransform(trans);
							}
						}
						// Sleep so that animation doesn't move too fast.
						// frameRate is from purges file and simSpeed is from GUI
						try {
						  Thread.sleep(frameRate/simSpeed);
						} catch (Exception e) {
						  e.printStackTrace();
						}
						
						// Update timer
						long st = System.currentTimeMillis();
						diff = ((st - clockTime) * simSpeed) + diff;
						clockTime = st;
						String hrT = makeReadableTime(diff);
						timerLabel.setText("<html><font size=\"20\" color=\"#333333\">" + hrT + "</font></html>");
					} else {
						// Simulation paused or not started, so keep clock time in sync ready for resume
						clockTime = System.currentTimeMillis();
					}//end if
				} else {
					// Processing of Purges file not done, so remove population
					if (population != null) {
						for (int i = 0; i < population.size(); i++) {
							((Person)population.get(i)).unblock();
							((BranchGroup)peopleToBg.get( ((Person)population.get(i)).getName() )).detach();
							population.remove(i);
						}
					}
				}//end if
			}//end while
			
			// Signal that psimulation is finsished and reset GUI
			done = true;
			startButton.setText("Start Simulation");
			startButton.setActionCommand("Start Simulation");
			popEntry.setText(String.valueOf(populationSize));
			popEntry.setEnabled(true);
		}//end run()
		
		
		/**
		* Show if Simulation has finished
		*
		* @return   True if simulation is still running
		*/
		public boolean isDone()
		{
			return done;
		}//end isDone()
		
	}//end class RunSimulation
	
	
	// ************ Constructors ************
	
	/**
	* Default constructor if no arguements supplied.
	* Constructs Main Window
	*/
	GUI()
	{
		// Set window title
		setTitle("Purges");
		
		// Allow window to close when cross is clicked
		addWindowListener(new WinHandler());
		
		// Get the screen dimensions for sizing the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Position window in center of screen
		int width = 800;
		int height = 600;
		Rectangle r = new Rectangle();
		r.setBounds((screenSize.width/2)-(width/2) , (screenSize.height/2)-(height/2), width, height);
		setBounds(r);
		
		// Set up content pane
		setContentPane(createContentPane());
		
		// Set up menu bar
		setJMenuBar(createMenuBar());
		
		// Make window visible
		setVisible(true);
	} //constructor
	
	// ************ end Constructors ************
	
	
	/**
	* Create a menu bar for the program
	*
	* @return  The the created JMenuBar.
	*/
	private JMenuBar createMenuBar()
	{
		// Variables for menu bar
        JMenuBar menuBar;
        JMenu menu, help, options;
        JMenuItem menuItem, about, qHelp;
		
        //Create the menu bar.
        menuBar = new JMenuBar();
		
        //Build the file menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("File options");
        menuBar.add(menu);
		
        //a group of JMenuItems
		// Open option
		menuItem = new JMenuItem("Open...", KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Open Purges File");
		menuItem.setActionCommand("Open..."); 
        menuItem.addActionListener(new FileOpenHandler());
        menu.add(menuItem);
		
		// Makes things look tidier
		menu.addSeparator();
		
		// Exit program option
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription("Exit program");
		menuItem.setActionCommand("Exit"); 
        menuItem.addActionListener(new ExitHandler());
        menu.add(menuItem);
		
		//Build the help menu.
        help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.getAccessibleContext().setAccessibleDescription("Help options");        
		
		// Quick help
        qHelp = new JMenuItem("Quick Help", KeyEvent.VK_Q);		
        qHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        qHelp.getAccessibleContext().setAccessibleDescription("Quick Help");
		qHelp.setActionCommand("Help"); 
        qHelp.addActionListener(new HelpHandler());
        help.add(qHelp);
		
		// Makes things look tidier
		help.addSeparator();
		
		// About purges
        about = new JMenuItem("About Purges", KeyEvent.VK_A);		
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        about.getAccessibleContext().setAccessibleDescription("About Purges");
		about.setActionCommand("About"); 
        about.addActionListener(new AboutHandler());
        help.add(about);
		
		// Build the Options menu
		options = new JMenu("Options");
		options.setMnemonic(KeyEvent.VK_O);
        options.getAccessibleContext().setAccessibleDescription("Program Options");
        menuBar.add(options);
		
		// Blocker
        blocker = new JMenuItem("Block Areas", KeyEvent.VK_B);		
        blocker.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        blocker.getAccessibleContext().setAccessibleDescription("Block Areas");
		blocker.setActionCommand("Block"); 
        blocker.addActionListener(new BlockHandler());
		blocker.setEnabled(false);
        options.add(blocker);
		
		// Keep help menu on right of others by adding last
		menuBar.add(help);
		
		// Return constructed menu bar
        return menuBar;
    }//end createMenuBar()
	
	
	/**
	* Create a content pane for the program
	*
	* @return  The the created Container.
	*/
	private Container createContentPane()
	{
        //Create the content pane.
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
		
        // Create the 3D output pane
		simOutputPane = new JPanel(new BorderLayout());
		simOutput = new Purges();
		simOutputPane.add(simOutput);
		
		// Create everything to the left of the 3D output pane
		leftPane = new JPanel(new BorderLayout());
			// The rest of this code is just swing stuff, rather than go through it
			// all, I'll just summarise here. This is a BoxLayout pane and has multiple
			// hierarchies of them. This is a good way of creating a good looking
			// layout that doesn't break with screen resize.
			// All the components on the left of the 3D output pane in the main window
			// are added with the code below.
			leftTopPane3 = new JPanel(new BorderLayout());
				// This deals with adding the rotation slider
				sliderPane = new JPanel(new BorderLayout());
					// The spacers are for layout
					JPanel sliderSpacer = new JPanel(new BorderLayout());
						JLabel sliderSpacerLabel = new JLabel(" ");
					sliderSpacer.add(sliderSpacerLabel, BorderLayout.WEST);
					
					JPanel sliderSpacer2 = new JPanel(new BorderLayout());
						JLabel sliderSpacerLabel2 = new JLabel(" ");
					sliderSpacer2.add(sliderSpacerLabel2, BorderLayout.EAST);
					
					JPanel sliderSpacer3 = new JPanel(new BorderLayout());
						JLabel sliderSpacerLabel3 = new JLabel(" ");
					sliderSpacer3.add(sliderSpacerLabel3, BorderLayout.EAST);
					
					//Create the slider pane.
					JPanel spane = new JPanel();
					
					// Creates and sets a border for the radio button pane:				
					spane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Rotate Environment"));
					
					// Chaneg slider layout to GridLayout
					spane.setLayout(new GridLayout(0,1));
					
					//Create the slider
					rotSlider = new JSlider(JSlider.HORIZONTAL, -60, 60, 0);
					rotSlider.addChangeListener(new SliderListener());
					rotSlider.setMajorTickSpacing(10);
					rotSlider.setMinorTickSpacing(1);
					//rotSlider.setPaintTicks(true);  // paint the divisions
					rotSlider.setEnabled(false);
					
					//Create the label table
					Hashtable labelTable = new Hashtable();
					labelTable.put( new Integer( 0 ), new JLabel("Left") );
					labelTable.put( new Integer( 100 ), new JLabel("Right") );
					rotSlider.setLabelTable( labelTable );
					
					//rotSlider.setPaintLabels(true);  // show the labels under slider
					
					// Add the slider to the inner slider pane
					spane.add(rotSlider);
					
					// Create full slider content pane
					sliderPane.add(sliderSpacer3, BorderLayout.NORTH);				
					sliderPane.add(sliderSpacer, BorderLayout.WEST);
					sliderPane.add(spane, BorderLayout.CENTER);
					sliderPane.add(sliderSpacer2, BorderLayout.EAST);					
					
					
				// Hierarchy pane for layout
				leftTopPane2 = new JPanel(new BorderLayout());
					// The simulation speed selection pane
					frameRatePane = new JPanel(new BorderLayout());
						// Spacer panes for layout
						JPanel frameSpacer = new JPanel(new BorderLayout());
							JLabel frameSpacerLabel = new JLabel(" ");
						frameSpacer.add(frameSpacerLabel, BorderLayout.WEST);
						
						JPanel frameSpacer2 = new JPanel(new BorderLayout());
							JLabel frameSpacerLabel2 = new JLabel(" ");
						frameSpacer2.add(frameSpacerLabel2, BorderLayout.EAST);
						
						JPanel frameSpacer3 = new JPanel(new BorderLayout());
							JLabel frameSpacerLabel3 = new JLabel(" ");
						frameSpacer3.add(frameSpacerLabel3, BorderLayout.EAST);
						
						//Create the radio buttons.
						JPanel bpane = new JPanel();
						
						// Creates and sets a border for the radio button pane:				
						bpane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Simulation Speed"));
						
						// Set the Layout scheme to GridLayout
						bpane.setLayout(new GridLayout(0,2));
						
						// Create all the buttons
						JRadioButton one = new JRadioButton("1x");
						one.setMnemonic(KeyEvent.VK_1);
						one.setActionCommand("one");
						one.addActionListener(new RadioButtHandler());
						one.setSelected(true);
						
						JRadioButton two = new JRadioButton("2x");
						two.setMnemonic(KeyEvent.VK_2);
						two.setActionCommand("two");
						two.addActionListener(new RadioButtHandler());
						
						JRadioButton four = new JRadioButton("4x");
						four.setMnemonic(KeyEvent.VK_4);
						four.setActionCommand("four");
						four.addActionListener(new RadioButtHandler());
						
						JRadioButton eight = new JRadioButton("8x");
						eight.setMnemonic(KeyEvent.VK_8);
						eight.setActionCommand("eight");
						eight.addActionListener(new RadioButtHandler());
						
						//Group the radio buttons.
						ButtonGroup frameRateButts = new ButtonGroup();
						frameRateButts.add(one);
						frameRateButts.add(two);
						frameRateButts.add(four);
						frameRateButts.add(eight);
						
						// Add all the buttons to the inner button pane
						bpane.add(one);
						bpane.add(two);
						bpane.add(four);
						bpane.add(eight);
						
					// Create main simulation speed pane
					frameRatePane.add(frameSpacer3, BorderLayout.NORTH);				
					frameRatePane.add(frameSpacer, BorderLayout.WEST);
					frameRatePane.add(bpane, BorderLayout.CENTER);
					frameRatePane.add(frameSpacer2, BorderLayout.EAST);
					
					
					// Hierarchy pane for layout
					leftTopPane1 = new JPanel(new BorderLayout());
						// Content pane for the population setter
						popPane = new JPanel(new BorderLayout());
							// Spacer panes for layout
							JPanel popSpacer = new JPanel(new BorderLayout());
								JLabel popSpacerLabel = new JLabel(" ");
							popSpacer.add(popSpacerLabel, BorderLayout.WEST);
							
							JPanel popSpacer2 = new JPanel(new BorderLayout());
								JLabel popSpacerLabel2 = new JLabel(" ");
							popSpacer2.add(popSpacerLabel2, BorderLayout.EAST);
							
							JPanel popSpacer3 = new JPanel(new BorderLayout());
								JLabel popSpacerLabel3 = new JLabel(" ");
							popSpacer3.add(popSpacerLabel3, BorderLayout.EAST);
							
							// Population text label and text entry
							JPanel popGet = new JPanel(new FlowLayout(FlowLayout.CENTER));
								JLabel popNameLabel = new JLabel("Population Size:");
							popGet.add(popNameLabel);
							popEntry.setEnabled(false);
							popGet.add(popEntry);
							
							// Set Borders for population pane
							popGet.setBorder(BorderFactory.createLineBorder(new Color(200,198,198)));
							
						// Set up population content pane
						popPane.add(popSpacer3, BorderLayout.NORTH);				
						popPane.add(popSpacer, BorderLayout.WEST);
						popPane.add(popGet, BorderLayout.CENTER);
						popPane.add(popSpacer2, BorderLayout.EAST);
						
						
						// Content pane for the start button
						startPane = new JPanel(new BorderLayout());
							// Spacer panes for layout
							JPanel startSpacer = new JPanel(new BorderLayout());
								JLabel startSpacerLabel = new JLabel(" ");
							startSpacer.add(startSpacerLabel, BorderLayout.WEST);
							
							JPanel startSpacer2 = new JPanel(new BorderLayout());
								JLabel startSpacerLabel2 = new JLabel(" ");
							startSpacer2.add(startSpacerLabel2, BorderLayout.EAST);
							
							JPanel startSpacer3 = new JPanel(new BorderLayout());
								JLabel startSpacerLabel3 = new JLabel(" ");
							startSpacer3.add(startSpacerLabel3, BorderLayout.EAST);
							
							// Start Simulation button
							JPanel startGet = new JPanel(new FlowLayout(FlowLayout.CENTER));
								startButton = new JButton("Start Simulation");
								startButton.setEnabled(false);
								startButton.setActionCommand("Start Simulation"); 
								startButton.addActionListener(new StartButtHandler());
							startGet.add(startButton);
							
						// Set up start button content pane
						startPane.add(startSpacer3, BorderLayout.NORTH);				
						startPane.add(startSpacer, BorderLayout.WEST);
						startPane.add(startGet, BorderLayout.CENTER);
						startPane.add(startSpacer2, BorderLayout.EAST);
						
						
					// Build hierarchies into single panes
					leftTopPane1.add(popPane, BorderLayout.NORTH);
					leftTopPane1.add(startPane, BorderLayout.SOUTH);
					
				// Build hierarchies into single panes
				leftTopPane2.add(leftTopPane1, BorderLayout.NORTH);
				leftTopPane2.add(frameRatePane, BorderLayout.SOUTH);
				
			// Build hierarchies into single panes
			leftTopPane3.add(leftTopPane2, BorderLayout.NORTH);
			leftTopPane3.add(sliderPane, BorderLayout.SOUTH);
			
			
			// Set up timer pane
			JPanel timerPane = new JPanel(new BorderLayout());
				// Spacer panes for layout
				JPanel timerSpacer = new JPanel(new BorderLayout());
					JLabel timerSpacerLabel = new JLabel(" ");
				timerSpacer.add(timerSpacerLabel, BorderLayout.WEST);
				
				JPanel timerSpacer2 = new JPanel(new BorderLayout());
					JLabel timerSpacerLabel2 = new JLabel(" ");
				timerSpacer2.add(timerSpacerLabel2, BorderLayout.EAST);
				
				JPanel timerSpacer3 = new JPanel(new BorderLayout());
					JLabel timerSpacerLabel3 = new JLabel(" ");
				timerSpacer3.add(timerSpacerLabel3, BorderLayout.EAST);
				
				// This pane has a bottom spacer as well to raise it a little from bottom of window border
				JPanel timerSpacer4 = new JPanel(new BorderLayout());
					JLabel timerSpacerLabel4 = new JLabel(" ");
				timerSpacer4.add(timerSpacerLabel4, BorderLayout.EAST);
				
				// Set up inner pane for timer
				JPanel timerGet = new JPanel();
				
				// Creates and sets a border for the radio button pane:				
				timerGet.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),"Timer"));
				
				// Set layout for timer to FlowLayout
				timerGet.setLayout(new FlowLayout(FlowLayout.CENTER));
				
				// Set default value for timer
				timerLabel = new JLabel("<html><font size=\"20\" color=\"#333333\">0:00:00</font></html>");
				
				// Add timer to inner timer pane
				timerGet.add(timerLabel);
				
			// Set up timer content pane
			timerPane.add(timerSpacer3, BorderLayout.NORTH);
			timerPane.add(timerSpacer4, BorderLayout.SOUTH);
			timerPane.add(timerSpacer, BorderLayout.WEST);
			timerPane.add(timerGet, BorderLayout.CENTER);
			timerPane.add(timerSpacer2, BorderLayout.EAST);
			
			
		// Build hierarchies into single panes, left pane is now fully built
		leftPane.add(timerPane, BorderLayout.SOUTH);
		leftPane.add(leftTopPane3, BorderLayout.NORTH);
		
        //Add the 3D output pane to the content pane.
		contentPane.add(simOutputPane, BorderLayout.CENTER);
		contentPane.add(leftPane, BorderLayout.WEST);
		
		return contentPane;
    }//end createContentPane()
	
	
	/**
	* Construct a splash screen or loader screen
	*
	* @param pic  The filename of the picture to use as the splash screen
	* @param time  A quantity of time given in milliseconds to display splash screen for
	* @param loader  Set this to true to disable automatic removal of splash screen
	* @return  The SplashWindow made by the function
	*/
	private static SplashWindow createAndShowSplash(String pic, int time, boolean loader)
	{
		// Display spash screen
		SplashWindow pling = new SplashWindow(pic, new Frame(), time, loader);
		return pling;
	}//end createAndShowSplash()
	
	
	/**
	* This is for producing standard output of strings.
	*
	* @param msg  The Object to output.
	*/
	/*public void doOutput(Object msg)
	{
		//System.out.print(msg);
	}//end doOutput() */
	
	
	/**
	* Main program for Purges. Sets up GUI
	* and controls simulations.
	*
	* @param argv  Command line arguements
	*/
	public static void main(String[] argv)
	{
		// Check if we were given a parameter on the command line.
		// The command line can be used to set if mouse dragging can 
		// be used for manipulating the 3D and to set if 3D display 
		// should be set to wireframe.
		if (argv.length > 0) {
			String errMessage = "Usage:\njava -jar Purges.jar <OPTIONS>\n\nWhere " +
			                    "<OPTIONS> can be any set of the following (including none):\n" +
								"-mouseRotate -wireFrame";
			if (argv[0].equals("-mouseRotate")) {
				useMouse = true;
			}
			else if (argv[0].equals("-wireFrame")) {
				bShadedModel = false;
			}
			else {
				System.err.println(errMessage);
				System.exit(1);
			}
			if (argv.length > 1) {
				if (argv[1].equals("-mouseRotate")) {
					useMouse = true;
				}
				else if (argv[1].equals("-wireFrame")) {
					bShadedModel = false;
				}
				else {
					System.err.println(errMessage);
					System.exit(1);
				}
			}
        }
		
		// Set cross platform look and feel
		String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
		try {
		    UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e) { }
		
		// Make sure Swing menus are drawn on top of AWT canvas (lightweight vs. heavyweight components)
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		//Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
		
		// Get the main GUI window started
		new GUI();
		
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's Splash Screen.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowSplash("splash.jpg", 2000, false);
            }
        });		
	}//end main()
	
}//end class GUI