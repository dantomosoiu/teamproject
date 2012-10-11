// java.util is needed for the ArrayList class
import java.util.*;

/**
* This class is used to represent Vertices in the PURGES
* Evacuation Simulator. It is not general purpose and it
* is not designed as a general purpose representation. 
*
* @author John Urquhart Ferguson
* @version 0.02
* @see Graph
*/
/*
* Last Modified: 6th March, 2006: 00:40GMT
*
* Status: Has worked correctly for all test input.
*
* Notes: 
* There is almost no error checking in this class.
* It is assumed that everything is used correctly and with
* sensible parameters. It's not thread safe at the moment.
*
* I've added an 'alwaysFree' variable that overrides 
* if the Vertex is set to blocked or not. This is intended
* for 'goal' Vertices so that Person movement is more
* realistic.
*
* I've also added an 'alwaysBlocked' variable that 
* overrides if the Vertex is set to blocked or not. This is
* intended for the GUI to block Vertices to simulate
* cave-ins. This takes precedence over the 'alwaysFree'
* flag.
*/


public class Vertex 
{
	
	// Human readable name for Vertex
	private String name;
	
	// Real world coordinates for Vertex
	private float x;
	private float y;
	private float z;
	
	// g value and path used in a* algorithm
	private int g;
	private ArrayList path;
	
	// Used to show whether or not a person
	// can move into this Vertex.
	private boolean blocked;
	
	// Used to show whether or not this square
	// is a goal Vertex. This means it is never 
	// treated as blocked.
	private boolean alwaysFree;
	
	// Used to show whether or not this square
	// has been blocked by the GUI. This means it 
	// is always treated as blocked. This takes
	// precedence over the 'alwaysFree' variable.
	private boolean alwaysBlocked;
	
	// All the Vertices reachable from this Vertex
	private ArrayList connectors;
	
	
    // ************ Constructors ************
	
    /**
	* Default constructor if no arguements supplied.
	*/
	public Vertex() 
	{
		// Just set everything to a default value.
		connectors = new ArrayList();
		path = new ArrayList();
		g = 0;
		x = 0;
		y = 0;
		z = 0;
		name = "default vertex";
		blocked = false;
		alwaysFree = false;
		alwaysBlocked = false;
	}
	
	/**
	* Constructor if specified arguments are supplied.
	*
	* @param namein  Human readable name for Vertex.
	* @param xin  Real world x-coordinate for Vertex.
	* @param yin  Real world y-coordinate for Vertex.
	* @param zin  Real world z-coordinate for Vertex.
	*/
	public Vertex(String namein, float xin, float yin, float zin) 
	{
		name = namein;
		
		x = xin;
		y = yin;
		z = zin;
		
		// Vertices are defaultly unblocked.
		blocked = false;
		
		// Vertices are defaulty allowed to
		// be blocked.
		alwaysFree = false;
		
		// Vertices are defaulty allowed to
		// be unblocked.
		alwaysBlocked = false;
		
		// Just set default values for these.
		connectors = new ArrayList();
		path = new ArrayList();
		g = 0;
	}
	
	/**
	* Constructor if specified arguments are supplied.
	*
	* @param namein  Human readable name for Vertex.
	* @param xin  Real world x-coordinate for Vertex.
	* @param yin  Real world y-coordinate for Vertex.
	* @param zin  Real world z-coordinate for Vertex.
	* @param blockedin  Whether Vertex is defaultly blocked.
	*/
	public Vertex(String namein, float xin, float yin, float zin, boolean blockedin)
	{
		name = namein;
		
		x = xin;
		y = yin;
		z = zin;
		
		blocked = blockedin;
		
		// Vertices are defaulty allowed to
		// be blocked.
		alwaysFree = false;
		
		// Vertices are defaulty allowed to
		// be unblocked.
		alwaysBlocked = false;
		
		// Just set default values for these.
		connectors = new ArrayList();
		path = new ArrayList();
		g = 0;
	}
	
	// ************ end Constructors ************
	
	
	/**
	* Test if two Vertex objects represent the 
	* same real world point.
	*
	* @param vin  The Vertex to test equality with.
	* @return  True if Vertices are equal, false otherwise.
	*/
	public boolean equals(Vertex vin)
	{
		// Very simple equality test here
		if ( name.equals( vin.getName() ) ) {
			return true;
		} else {
			return false;
		}
	}//end equals()
	
	
	/**
	* Deep copy a Vertex object.
	*
	* @param vin  The Vertex to copy to.
	*/
	public void copy(Vertex vin) 
	{
		vin.setX(x);
		vin.setY(y);
		vin.setZ(z);
		
		vin.setG(g);
		
		vin.setName(name);
		vin.setBlocked(blocked);
		vin.setAlwaysFree(alwaysFree);
		vin.setAlwaysBlocked(alwaysBlocked);
		for (int i = 0; i < connectors.size(); i++) {
			vin.addEdge( (Vertex)connectors.get(i) );
		}
		for (int i = 0; i < path.size(); i++) {
			vin.addEdge( (Vertex)path.get(i) );
		}
	}//end copy()
	
	
	/**
	* Return the ArrayList of all Verices reachable
	* from this one (this is a shallow return, not a 
	* deep copy).
	*
	* @return  The ArrayList of all Vertices reachable from this one.
	*/
	public ArrayList getConnectors()
	{
		return connectors;
	}//end getConnectors()
	
	
	/**
	* Return the ArrayList of the best path so far
	* leading to this one. This method is used in the
	* a* algorithm of the Graph class. This is a deep
	* copy.
	*
	* @return  The ArrayList of the current best path.
	* @see   Graph
	*/
	public ArrayList getPathCopy()
	{
		ArrayList retArray = new ArrayList();
		for (int i = 0; i < path.size(); i++) {
			retArray.add( retArray.size(), path.get(i) );
		}
		return retArray;
	}//end getPathCopy()
	
	
	/**
	* Add an ArrayList containing more reachable Vertices
	* to the current list.
	*
	* @param connsin  The ArrayList of new reachable nodes.
	*/
	public void addConnectors(ArrayList connsin)
	{
		for (int i = 0; i < connsin.size(); i++) {
			Vertex temp = (Vertex)connsin.get(i);
			connectors.add(connectors.size(), temp);
		}
	}//end addConnectors()
	
	
	/**
	* Add an ArrayList containing more path Vertices
	* to the current list. This method is used in the
	* a* algorithm of the Graph class. This is currently
	* never used.
	*
	* @param pathin  The ArrayList of new path Vertices.
	* @see Graph
	*/
	public void addPath(ArrayList pathin)
	{
		for (int i = 0; i < pathin.size(); i++) {
			Vertex temp = (Vertex)pathin.get(i);
			path.add(path.size(), temp);
		}
	}//end addPath()
	
	
	/**
	* Replace the current Vertices reachable from this one
	* with those in the given ArrayList.
	*
	* @param connsin The ArrayList of new Vertices reachable from this one.
	*/
	public void setConnectors(ArrayList connsin)
	{
		ArrayList tempar = new ArrayList();
		for (int i = 0; i < connsin.size(); i++) {
			Vertex temp = (Vertex)connsin.get(i);
			tempar.add(tempar.size(), temp);
		}
		connectors = tempar;
	}//end setConnectors()
	
	
	/**
	* Replace the current Path with those in the 
	* given ArrayList. This method is used in the
	* a* algorithm of the Graph class. I don't think
	* this is currently used.
	*
	* @param pathin The ArrayList of new Vertices reachable from this one.
	* @see Graph
	*/
	public void setPath(ArrayList pathin)
	{
		ArrayList tempar = new ArrayList();
		for (int i = 0; i < pathin.size(); i++) {
			Vertex temp = (Vertex)pathin.get(i);
			tempar.add(tempar.size(), temp);
		}
		path = tempar;
	}//end setPath()
	
	
	/**
	* Return the human readable name of this Vertex as a string.
	*
	* @return  The String representing the name of this Vertex.
	*/
	public String getName()
	{
		return name;
	}//end getName()
	
	
	/**
	* Change the human readable name of this 
	* Vertex to the given string.
	*
	* @param nin The String of the new name for this Vertex.
	*/
	public void setName(String nin)
	{
		name = nin;
	}//end setName()
	
	
	/**
	* Return the G value of this Vertex. This is 
	* required for the a* method in the Graph class.
	*
	* @return  The int representing the current G value of this Vertex.
	* @see Graph
	*/
	public int getG()
	{
		return g;
	}//end getG()
	
	
	/**
	* Return the X value of the real world coordinate
	* this Vertex represents.
	*
	* @return  The float representing the current X value of this Vertex.
	*/
	public float getX()
	{
		return x;
	}//end getX()
	
	
	/**
	* Return the Y value of the real world coordinate
	* this Vertex represents.
	*
	* @return  The float representing the current Y value of this Vertex.
	*/
	public float getY()
	{
		return y;
	}//end getY()
	
	
	/**
	* Return the Z value of the real world coordinate
	* this Vertex represents.
	*
	* @return  The float representing the current Z value of this Vertex.
	*/
	public float getZ()
	{
		return z;
	}//end getZ()
	
	
	/**
	* Change the G value of this Vertex. This is
	* required for the a* method in the Graph class.
	*
	* @param gin The int representing the new G value for this Vertex.
	* @see Graph
	*/
	public void setG(int gin)
	{
		g = gin;
	}//end setG()
	
	
	/**
	* Change the X value this Vertex represents in
	* the real world coordinates.
	*
	* @param xin The float representing the new X value for this Vertex.
	*/
	public void setX(float xin)
	{
		x = xin;
	}//end setX()
	
	
	/**
	* Change the Y value this Vertex represents in
	* the real world coordinates.
	*
	* @param yin The float representing the new Y value for this Vertex.
	*/
	public void setY(float yin)
	{
		y = yin;
	}//end setY()
	
	
	/**
	* Change the Z value this Vertex represents in
	* the real world coordinates.
	*
	* @param zin The float representing the new Z value for this Vertex.
	*/
	public void setZ(float zin)
	{
		z = zin;
	}//end setZ()
	
	
	/**
	* Return true if this Vertex is currently blocked.
	*
	* @return  The boolean showing if this Vertex is blocked.
	*/
	public boolean isBlocked()
	{
		// The alwaysBlocked variable overrides
		// everything else.
		if (alwaysBlocked) {
			return true;
		} else {
			// The alwaysFree variable overrides
			// the actual blocked status.
			if (alwaysFree) {
				return false;
			} else {
				// return actual blocked status.
				return blocked;
			}
		}
	}//end isBlocked()
	
	
	/**
	* Make this Vertex blocked.
	*/
	public void block()
	{
		blocked = true;
	}//end block()
	
	
	/**
	* Make this Vertex free (unblocked).
	*/
	public void unblock()
	{
		blocked = false;
	}//end unblock()
	
	
	/**
	* Make the always free status of this Vertex 
	* equal to the given input. True to be always free, 
	* False to allow to be blocked.
	*
	* @param fin The boolean representing the new always free state for this Vertex.
	*/
	public void setAlwaysFree(boolean fin)
	{
		alwaysFree = fin;
	}//end setAlwaysFree()
	
	
	/**
	* Return true if this Vertex has its always free
	* status set to true. This takes precedence over
	* the setBlocked(), block() and unblock() methods.
	*
	* @return  The boolean showing if this Vertex is always free.
	*/
	public boolean isAlwaysFree()
	{
		return alwaysFree;
	}//end isAlwaysFree()
	
	
	/**
	* Make the always blocked status of this Vertex 
	* equal to the given input. True to be always blocked, 
	* False to allow to be free. This takes precedence
	* over setAlwaysFree() and any other blocking/unblocking.
	*
	* @param fin The boolean representing the new always free state for this Vertex.
	*/
	public void setAlwaysBlocked(boolean fin)
	{
		alwaysBlocked = fin;
	}//end setAlwaysBlocked()
	
	
	/**
	* Return true if this Vertex has its always blocked
	* status set to true.
	*
	* @return  The boolean showing if this Vertex is always blocked.
	*/
	public boolean isAlwaysBlocked()
	{
		return alwaysBlocked;
	}//end isAlwaysBlocked()
	
	
	/**
	* Make the bocked status of this Vertex 
	* equal to the given input. True to block, 
	* False to unblock.
	*
	* @param statein The boolean representing the new blocked state for this Vertex.
	*/
	public void setBlocked(boolean statein)
	{
		blocked = statein;
	}//end setBlocked()
	
	
	/**
	* Add a Vertex to the current list of Reachable 
	* Vertices from this Vertex.
	*
	* @param vin The Vertex to add to the current list of reachable Vertices.
	*/
	public void addEdge(Vertex vin)
	{
		connectors.add(connectors.size(), vin);
	}//end addEdge()
	
	
	/**
	* Add a Vertex to the current path  
	* for this Vertex. This is required by
	* the a* method in the Graph class.
	*
	* @param vin The Vertex to add to the current path.
	* @see Graph
	*/
	public void addPathEdge(Vertex vin)
	{
		path.add(path.size(), vin);
	}//end addPathEdge()
	
	
	/**
	* Return a String representation of this Vertex.
	*
	* @return  The human readable String representation of this Vertex.
	*/
	public String toString()
	{
		String outstring = "";
		outstring = outstring + name + ": ";
		
		ArrayList v1conns = connectors;
		if (v1conns.size() == 0 ) {
			outstring = outstring + "no edges";
		} else { 
			for (int j = 0; j < v1conns.size(); j++) {
				Vertex tempVec = (Vertex)v1conns.get(j);
				outstring = outstring + tempVec.getName() + " ";
			}
		}
		return outstring;
	}//end toString()
	
	
	/**
	* This is for producing standard output of strings.
	*
	* @param msg  The Object to output.
	*/
	/*public static void doOutput(Object msg)
	{
		System.out.print(msg);
	}//end doOutput()*/
	
	
	/**
	* Just for testing purposes
	*
	* @param args  The command line arguements.
	*/
	/*public static void main(String args[]) 
	{
		// Create a new Vertex
		Vertex v1 = new Vertex("v1", 0, 0, 0, false);
		doOutput("Hi there, I'm a Vertex class and my name is: " + v1.getName());
		
		doOutput("\n\n");
		
		// Test to see if it's blocked
		if (v1.isBlocked()) {
			doOutput("Unfortunately, I'm blocked\n\n");
		} else {
			doOutput("Luckily, I'm not blocked\n\n");
		}
		
		// Let's block it
		v1.block();
		doOutput("I'm being blocked...\n\n");
		
		// Test again to see if it's blocked
		if (v1.isBlocked()) {
			doOutput("Unfortunately, I'm blocked now\n\n");
		} else {
			doOutput("Luckily, I'm still not blocked\n\n");
		}
	
		// Test what it's connected to
		doOutput("I am connected to the following set of Vertices: \n");
		ArrayList v1conns = v1.getConnectors();
		for (int i = 0; i < v1conns.size(); i++) {
			Vertex tempVec = (Vertex)v1conns.get(i);
			doOutput(tempVec.getName() + " ");
		}
		doOutput("\n\n");
		
		// Connect it to some other Vertices
		doOutput("Someone is adding edges to me...\n\n");
		Vertex v3 = new Vertex("v3", 0, 0, 0, false);
		Vertex v2 = new Vertex("v2", 0, 0, 0, false);
		v1.addEdge(v3);
		v1.addEdge(v2);
				
		// Test what it's connected to again
		doOutput("I am now connected to the following set of Vetices: ");
		v1conns = v1.getConnectors();
		for (int i = 0; i < v1conns.size(); i++) {
			Vertex tempVec = (Vertex)v1conns.get(i);
			doOutput(tempVec.getName() + " ");
		}
		
		// Replace the connected Vertices
		doOutput("\n\n");
		doOutput("Someone is replacing my vertices...\n\n");
		Vertex v4 = new Vertex("v4", 0, 0, 0, false);
		Vertex v5 = new Vertex("v5", 0, 0, 0, false);
		ArrayList conn = new ArrayList();
		conn.add(0, v4);
		conn.add(0, v5);
		v1.setConnectors(conn);
		
		// Check what we're connected to now
		doOutput("I am now connected to the following set of Vetices: ");
		v1conns = v1.getConnectors();
		for (int i = 0; i < v1conns.size(); i++) {
			Vertex tempVec = (Vertex)v1conns.get(i);
			doOutput(tempVec.getName() + " ");
		}
		doOutput("\n\n");		
	}//end main()*/
	
}//end class Vertex