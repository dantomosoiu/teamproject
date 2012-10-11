// The libraries below are needed for the XML parsing
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

// This library allows the use of HashMaps and ArrayLists
import java.util.*;

/**
* This class is used to deal with the file format in the PURGES
* Evacuation Simulator. It is not general purpose and it
* is not designed as a general purpose class. 
*
* The PURGES file format is essentially XML so this class is 
* really just a SAX parser.
*
* @author John Urquhart Ferguson
* @version 0.05
* @see Graph
* @see Vertex
*/
/*
* Last Modified: 13th March, 2006: 00:40GMT
*
* Status: Has worked correctly for all test input.
*
* Notes: There is almost no error checking in this class.
* It is assumed that everything is used correctly and with
* sensible parameters. It's not thread safe at the moment.
* Assumes a well formatted PURGES file as well.
*
* Now includes the 'alwaysFree' attribute of Vertices
* where available (to represent Goal Vertices).
*
* The program now also allows for multiple goal Vertices.
* I've Changed the way goals are passed as a Graph to an
* ArrayList.
*
* Changed all private variables to non-static to allow
* for multiple use of this class in GUI version of 
* program.
*
* Now has support for GUI blockable vertex groups
*/


// Extending DefaultHandler allows us to use it's SAX parsing capabilities
public class GraphMaker extends DefaultHandler
{
	
	// These are 'flag' variables so we know what type of tag is being processed
	private boolean isVertex = false;
	private boolean isEdge = false;
	private boolean isDXF = false;
	private boolean isPersonscale = false;
	private boolean isFramerate = false;
	private boolean isPopulation = false;
	private boolean isCamera = false;
	
	// This is a flag needed for the output of the temp DXF 3D model file
	private boolean passedLineOne = false;
	
	// Variable to store the current Vertex to be added to the Graph
	private Vertex current = new Vertex();
	
	// The filename for the temp DXF 3D model file
	private String tempFileName = "purges.tmp";
	
	// The temp file to use as the DXF 3D model file
	private File current3D = new File(tempFileName);
	
	// The Graph we are creating from the purges file
	private Graph g1 = new Graph();
	
	// The HashMap used to index our list of created Vertices
	// by their human readable name
	private HashMap hm = new HashMap();
	
	// The HashMap used to store the GUI blockable regions
	private HashMap guiBlocks = new HashMap();
	
	// The ArrayList to store the names of the GUI Blockable regions
	private ArrayList guiBlocksNames = new ArrayList();
	
	// List of goal nodes
	private ArrayList goals = new ArrayList();
	
	// The percentile to scale people by for the model
	private float scaler = 0;
	
	// The default frame rate for the animation
	private int defaultfr = 2;
	
	// The default population for the simulation.
	// The default is now also the maximum population.
	private int defaultPop = 0;
	
	// The default camera rotation offsets for the simulation
	private float cameraRotX = 0;
	private float cameraRotY = 0;
	private float cameraRotZ = 0;
	
	// Our file writer variable to let us create a temp DXF 3D model file
	private Writer out;
	
	
	// ************ Constructors ************
	
    /**
	* Constructor if specified arguments are supplied.
	*
	* @param fin  Filename of PURGES file to process.
	*/
	public GraphMaker(String fin) 
	{
		// Call constructor of super class
		super();
		
		// Temp file already in use, delete it
		if (current3D.exists()) {
			current3D.delete();
		}
		
		// Create temp file for DXF 3D model file
		try {
			current3D.createNewFile();
		}
		catch (IOException ioe)
		{
			System.out.println("IOException = " + ioe.getMessage());
		}
		
		
		// Use the default (non-validating) parser		
        SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
            // Set up output stream
            out = new OutputStreamWriter(System.out, "UTF8");
			
			// Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse( new File(fin), this );			
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	// ************ end Constructors ************
	
	
	/**
	* Return the Graph produced from the PURGES file
	*
	* @return  The Graph produced by this class.
	* @see Graph
	*/
	public Graph getGraph()
	{
		return g1;
	}//end getGraph()
	
	
	/**
	* Return the GUI Blockables HashMap made from the PURGES file
	*
	* @return  The GUI Blockables HashMap produced by this class.
	* @see Vertex
	*/
	public HashMap getGUIBlockables()
	{
		return guiBlocks;
	}//end getGUIBlockables()
	
	
	/**
	* Return the GUI Blockables names made from the PURGES file
	*
	* @return  The GUI Blockables names ArrayList produced by this class.
	*/
	public ArrayList getGUIBNames()
	{
		return guiBlocksNames;
	}//end getGUIBNames()
	
	
	/**
	* Return the camera x-axis rotation produced from the PURGES file
	*
	* @return  The camera x-axis rotation produced by this class.
	*/
	public float getCameraRotX()
	{
		return cameraRotX;
	}//end getCameraRotX()
	
	
	/**
	* Return the camera y-axis rotation produced from the PURGES file
	*
	* @return  The camera y-axis rotation produced by this class.
	*/
	public float getCameraRotY()
	{
		return cameraRotY;
	}//end getCameraRotY()
	
	
	/**
	* Return the camera z-axis rotation produced from the PURGES file
	*
	* @return  The camera z-axis rotation produced by this class.
	*/
	public float getCameraRotZ()
	{
		return cameraRotZ;
	}//end getCameraRotZ()
	
	
	/**
	* Return the HashMap produced from the PURGES file.
	* This is a hash of the Vertices of the Graph, indexed
	* by their human readable names.
	*
	* @return  The HashMap produced by this class.
	*/
	public HashMap getHash()
	{
		return hm;
	}//end getHash()
	
	
	/**
	* Return the goal Vertices produced from the PURGES file
	*
	* @return  The ArrayList of goals produced by this class.
	*/
	public ArrayList getGoals()
	{
		return goals;
	}//end getGoals()
	
	
	/**
	* Return the scaler percentile produced from the PURGES file
	*
	* @return  The float of the scaler percentile produced by this class.
	*/
	public float getScaler()
	{
		return scaler;
	}//end getScaler()
	
	
	/**
	* Return the default frame rate produced from the PURGES file
	*
	* @return  The int of the default framerate produced by this class.
	*/
	public int getDefaultFR()
	{
		return defaultfr;
	}//end getDefaultFR()
	
	
	/**
	* Return the default population produced from the PURGES file
	*
	* @return  The int of the default population produced by this class.
	*/
	public int getDefaultPop()
	{
		return defaultPop;
	}//end getDefaultPop()
	
	
	/**
	* Handler for the start tag of the XML file.
	* Not really processed in this code, but 
	* kept in for good measure.
	*/
	public void startDocument()
	throws SAXException
	{
		// We don't really handle this with any code
	}//end startDocument()
	
	
	/**
	* Handler for the end tag of the XML file.
	* Just clean up stuff really.
	*/
	public void endDocument()
	throws SAXException
	{
		// Make sure we empty the file output writer contents from memory.
		try {
			out.flush();
		} catch (IOException e) {
			throw new SAXException("I/O error", e);
		}
	}//end endDocument()
	
	
	/**
	* Handler for the start of an element in XML.
	* Only qName and the attrs are used, but we need
	* to include all parameters from the parent class.
	*
	* @param namespaceURI The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
	* @param sName The local name (without prefix), or the empty string if Namespace processing is not being performed.
	* @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
	* @param attrs The attributes attached to the element. If there are no attributes, it shall be an empty Attributes object.
	*/
	public void startElement(String namespaceURI,
							 String sName,
							 String qName,
							 Attributes attrs)
	throws SAXException
	{
		// Each tag is handled by one of these 'if' statements
		// This first one deals with Vertex tags.
		if (qName.equals("vertex")) {
			// We've got a vertex tag, so we need to handle the attributes
			// to add the Vertex to the Graph
			isVertex = true;
			boolean temp;
			if ((attrs.getValue("blocked")).equals("true")) {
				temp = true;
			} else {
				temp = false;
			}
			hm.put(attrs.getValue("name"), 
			       new Vertex(attrs.getValue("name"), 
				              Float.parseFloat(attrs.getValue("x")), 
							  Float.parseFloat(attrs.getValue("y")), 
							  Float.parseFloat(attrs.getValue("z")), 
							  temp));
			if( (attrs.getValue("goal")) != null ) {
				boolean temp2 = false;
				if ((attrs.getValue("goal")).equals("true")) {
					temp2 = true;
					((Vertex)hm.get(attrs.getValue("name"))).setAlwaysFree(temp2);
					goals.add( goals.size(), ((Vertex)hm.get(attrs.getValue("name"))) );
				}
			}
			if ( (attrs.getValue("guiblockable")) != null ) {
				if ( guiBlocks.containsKey((attrs.getValue("guiblockable"))) ) {
					ArrayList tempArr = (ArrayList)guiBlocks.get( attrs.getValue("guiblockable") );
					tempArr.add( (Vertex)hm.get(attrs.getValue("name")) );
				} else {
					guiBlocksNames.add( attrs.getValue("guiblockable") );
					ArrayList tempArr = new ArrayList();
					tempArr.add( (Vertex)hm.get(attrs.getValue("name")) );
					guiBlocks.put( attrs.getValue("guiblockable"), tempArr );
				}
			}
			
			// Everything is all set up now, so just add Vertex to Graph
			g1.addVertex((Vertex)hm.get(attrs.getValue("name")));
		}
		// if tag is an edge, set edge flag to true and add edge to Graph from attributes
		else if (qName.equals("edge")) {
			isEdge = true;
			g1.addEdge( (Vertex)hm.get(attrs.getValue("start")), (Vertex)hm.get(attrs.getValue("end")) );
		}
		// if tag is a DXF, set DXF flag to true
		else if (qName.equals("dxf")) {
			isDXF = true;
		}
		// if tag is a person scaler, set personscale flag to true and set scaler value from attributes 
		else if (qName.equals("personscale")) {
			isPersonscale = true;
			scaler = (Float.parseFloat(attrs.getValue("percent")) / 100);
		}
		// if tag is a frame rate, set framerate flag to true and set default frame rate value from attributes 
		else if (qName.equals("framerate")) {
			isFramerate = true;
			defaultfr = (Integer.parseInt(attrs.getValue("default")));
		}
		// if tag is a population, set population flag to true and set defualt/maximum population value from attributes 
		else if (qName.equals("population")) {
			isPopulation = true;
			defaultPop = (Integer.parseInt(attrs.getValue("default")));
		}
		// if tag is a camera offset, set camera flag to true and set camera offset values from attributes 
		else if (qName.equals("cameraoffset")) {
			isCamera = true;
			cameraRotX = Float.parseFloat(attrs.getValue("x"));
			cameraRotY = Float.parseFloat(attrs.getValue("y"));
			cameraRotZ = Float.parseFloat(attrs.getValue("z"));
		}
	}//end startElement()
	
	
	/**
	* Handler for the end of an element in XML.
	* This is mostly just resetting flags.
	*
	* @param namespaceURI The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
	* @param sName The local name (without prefix), or the empty string if Namespace processing is not being performed.
	* @param qName The qualified name (with prefix), or the empty string if qualified names are not available.
	*/
	public void endElement(String namespaceURI,
						   String sName,
						   String qName)
	throws SAXException
	{
		// Reset flag for whatever current element is:
		if (isVertex) {
			isVertex = false;
		}
		else if (isEdge) {
			isEdge = false;
		}
		else if (isDXF) {
			isDXF = false;
		}
		else if (isPersonscale) {
			isPersonscale = false;
		}
		else if (isFramerate) {
			isFramerate = false;
		}
		else if (isPopulation) {
			isPopulation = false;
		}
		else if (isCamera) {
			isCamera = false;
		}
	}//end endElement
	
	
	/**
	* Handler for the contents of an element in XML.
	* This inludes the processing of the encapsulated 
	* DXF file.
	*
	* @param buf The characters.
	* @param offset The start position in the character array.
	* @param len The number of characters to use from the character array.
	*/
	public void characters(char buf[], int offset, int len)
	throws SAXException
	{
		// Find out if we are currently in a DXF tag
		if (isDXF) {
			// We need to output DXF to a temp file.
			// Basically go through the input file line by line
			// and write to the temp file.
			String s = new String(buf, offset, len);			
			try {
				FileWriter out = new FileWriter(current3D, true);
				int c;
				for (int i = 0; i < s.length(); i++) {
					// We need to skip first line as buffer is a bit screwy
					if (passedLineOne) {
						c = s.charAt(i);
						if ( (c != -1) ) {
							out.write(c);
						}
					} else {
						passedLineOne = true;
					}
				}
				out.close();
			} catch (Exception e) {}
		}
	}//end characters()
	
	
	/**
	* Returns the filename for the 3D model file
	*
	* @return String representing the 3D model file
	*/
	public String getFileName()
	{
		return tempFileName;
	}//end getFileName()
	
	
	/**
	* Just for testing purposes
	*
	* @param args  The command line arguements.
	*/
	/*public static void main(String argv[])
	{
		// Make sure a PURGES file was supplied on command line
		if (argv.length != 1) {
			System.err.println("Usage: cmd filename");
			System.exit(1);
		}
		
		// Use an instance of ourselves as the SAX event handler
		GraphMaker handler = new GraphMaker(argv[0]);
		
		// Output the Graph that we made
		System.out.println(handler.getGraph());
		System.exit(0);
	}//end main()*/
	
}//end class GraphMaker