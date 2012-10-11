// java.util is needed for the ArrayList class
import java.util.*;

// java.lang.Math is used for the Randomizer
// Most java versions defaultly include this,
// but better safe than sorry.
import java.lang.Math.*;

/**
* This class is used to represent Graphs in the PURGES
* Evacuation Simulator. It is not general purpose and it
* is not designed as a general purpose representation. It
* is simply intended to be a theoretical representation of
* a 3D model in order to calculate movements for a person
* to make.
*
* @author John Urquhart Ferguson
* @version 0.01
* @see Vertex
*/
/*
* Last Modified: 27th February, 2006: 12:00 GMT
*
* Status: Has worked correctly for all test input.
*
* Notes: 
* There is almost no error checking in this class.
* It is assumed that everything is used correctly and with
* sensible parameters. It's not thread safe at the moment.
*/


public class Graph
{
	
	// List of Vertices in the Graph
    private ArrayList vertexList;
	
	
	// ************ Constructors ************
	
    /**
	* Default constructor if no arguements supplied.
	*/
    public Graph() {
		// Just set everything to a default value.
		vertexList = new ArrayList();
    }
	
	// ************ end Constructors ************
	
	
	/**
	* Return a pseudo random Vertex from the Graph.
	* This is a shallow return, not a deep copy.
	*
	* @return  The found random Vertex.
	*/
	public Vertex getRandomVertex()
	{
		Random randy = new Random();
		int rander = randy.nextInt(vertexList.size());
		return (Vertex)vertexList.get(rander);
	}//end getRandomVertex()
	
	
	/**
	* Add a Vertex to the current Graph.
	* Adding the actual object, not a copy.
	*
	* @param vin  The Vertex to add to the graph.
	*/
    public void addVertex(Vertex vin)
	{	
		vertexList.add(vertexList.size(), vin);
    }//end addVertex()
	
	
	/**
	* Connect two existing Vertices. They 
	* don't actually need to be in the Graph,
	* so sensible use is assumed.
	*
	* @param v1  One of the Vertices on the new edge.
	* @param v2  The second Vertex on the new edge.
	*/
    public void addEdge(Vertex v1, Vertex v2)
	{
		v1.addEdge(v2);
		v2.addEdge(v1);
    }//end addEdge()
	
	
	/**
	* Do a depth first traversal of the graph 
	* and return an ordered ArrayList of the 
	* traversal. The Vertices in the array list 
	* are the actual Vertex objects, not deep
	* copies.
	*
	* @param vin  The Vertex to start traversal from.
	* @return  An ordered ArrayList of the traversal.
	*/
    public ArrayList dfTraverse(Vertex vin)
	{
		// Oh, this is a pretty straighforward/standard algorithm.
		// The tricky thing is not changing the graph structure.
		ArrayList visited = new ArrayList();
		ArrayList stack = new ArrayList();
		Vertex current;
		ArrayList currentConns = new ArrayList();		
		int vinIndex = vertexList.indexOf(vin);
		stack.add(0, vertexList.get(vinIndex));
		while (!stack.isEmpty()) {
			current = (Vertex)stack.get(0);
			stack.remove(0);
			visited.add(current);
			for (int i = 0; i < (current.getConnectors()).size(); i++) {
				currentConns.add(i, (current.getConnectors()).get(i) );
			}
			while (!currentConns.isEmpty()) {
				if ( (!stack.contains((Vertex)currentConns.get(0))) && 
					 (!visited.contains((Vertex)currentConns.get(0))) ) {
					stack.add(0, (Vertex)currentConns.get(0) );
				}
				currentConns.remove(0);
			}
		}
		return visited;
    }//end dfTraverse()
	
	
	/**
	* Reset the changes made to the Graph during
	* an A* search.
	*/
	private void resetGraph()
	{
		for (int i = 0; i < vertexList.size(); i++) {
			// Reset G values of Vertices
			((Vertex)vertexList.get(i)).setG(0);
			
			// Reset best paths of Vertices
			((Vertex)vertexList.get(i)).setPath(new ArrayList());
		}
	}//end resetGraph()
	
	
	/**
	* This is a worker function of a*. A* uses
	* an heuristic to determine the best Vertex 
	* from a list as the one to move to. This
	* function carries out that heuristic.
	*
	* @param ain  An ArrayList of the possible squares to move to.
	* @param gin  The Vertex we're currently at.
	* @return  The best Vertex found.
	*/
	private Vertex findBestF(ArrayList ain, Vertex gin)
	{
		// The heuristic we're using here is called the Manhattan heuristic.
		// Basically, it works out the direction of the goal from the current
		// Vertex using the coordinates of each and ignores Vertices that go
		// in a different direction. So by checking all the surrounding 
		// vertices, it is relatively easy to decide which one is likely to
		// produce the best result, however, this could in fact be wrong in the
		// long run. But that's what heuristics are all about.
		
		// We need to find the lowest cost F, where F is H + G. G is the movement
		// cost to each Vertex from the current one (diagonal movement costs 
		// more than horizontal and vertical in 2D. 3D G is not calculated) along 
		// the current path. H is the heuristic score. For Manhattan, this is the 
		// estimated movement cost from the choosen Vertex to the Goal.
		Vertex best = new Vertex();
		int bestScore;
		int h;
		
		// Calculate the H value for the first reachable Vertex.
		h = (int)( ( Math.abs(((Vertex)(ain.get(0))).getX() - gin.getX()) + Math.abs(((Vertex)(ain.get(0))).getY() - gin.getY()) + Math.abs(((Vertex)(ain.get(0))).getZ() - gin.getZ()))*10);
		
		// Calculate F for first Vertex
		bestScore = ((Vertex)ain.get(0)).getG() + h; // Note that G is set in the a* method
		best = ((Vertex)(ain.get(0)));
		
		// Test each of the remaining Vertices
		for (int i=1; i < ain.size(); i++) {
			// Calculate the H value for this reachable Vertex.
			h = (int)( ( Math.abs(((Vertex)(ain.get(i))).getX() - gin.getX()) + Math.abs(((Vertex)(ain.get(i))).getY() - gin.getY()) + Math.abs(((Vertex)(ain.get(i))).getZ() - gin.getZ()))*10);
			
			// calculate F if better than current
			if ( (((Vertex)ain.get(i)).getG() + h) < bestScore) {
				bestScore = ((Vertex)ain.get(i)).getG() + h; // Note that G is set in the a* method
				best = ((Vertex)(ain.get(i)));
			}
		}
		return best;
	}//end findBestF()
	
	
	/**
	* This is a the a* path finding algorithm. This uses
	* an heuristic to get the near shortest path between
	* two Vertices in the Graph. This method is strongly
	* tied to the representation of the Vertex class.
	* The returned list hold the actual Vertex objects,
	* not a deep copy.<br/><br/>
	*
	* It should be noted that this may not necessarily return a path to the
	* desired Vertex from the starting Vertex. If no path is found, this 
	* version of a* will return a path of erratic behaviour. This is trying
	* to model what would happen in real life in a very basic way.
	*
	* @param sin  The Vertex to start from.
	* @param gin  The Vertex to end up at.
	* @return  The ordered ArrayList of the path found by a*.
	*/
	public ArrayList astar(Vertex sin, Vertex gin)
	{
		// Reset graph before doing an A* search
		this.resetGraph();
		
		// Create a node containing the start state
		Vertex start = new Vertex();
		sin.copy(start);
		
		// Create a node containing the goal state
		Vertex goal = new Vertex();
		gin.copy(goal);
		
		// Create node to hold current
		Vertex current = new Vertex();
		
		// Create lists
		ArrayList open = new ArrayList();
		ArrayList closed = new ArrayList();
		
		// put start on the open list
		open.add(open.size(), start);
		
		//while open list is not empty, do this:
		while (open.size() > 0) {
			// Get the node off the open list with the lowest f and make current
			current = (this.findBestF(open, goal));			
			
			// if current equals goal, add to closed and break from loop: We have finished
			if ( current.equals(goal) ) {
				closed.add(closed.size(), current);
				current.addPathEdge(current);
				break;
			}
			
			// for each successor node of current, do this:
			ArrayList successors = new ArrayList();
			successors = current.getConnectors();
			for (int index = 0; index < successors.size(); index++) {
				// Set up a Vertex to hold current successor
				Vertex successor = (Vertex)successors.get(index);
				
				// We can't travel through blocked Vertices, so ignore them
				if ( successor.isBlocked() ) {
					continue;
				}
				
				// Set the cost of current successor to be the cost of current plus the cost to get to current successor from current
				int g = 0;
				if ( ( successor.getX() == current.getX() ) || ( successor.getY() == current.getY() ) ) {
					// found in line square
					g = 10 + successor.getG();
				} else {
					// found diagonal square (greater cost than in line)
					g = 14 + successor.getG();
				}
				
				// find current successor on the OPEN list
				int remIndex = -1;
				for (int indexF = 0; indexF < open.size(); indexF++) {
					if ( ((Vertex)open.get(indexF)).equals(successor) ) {
						remIndex = indexF;
						break;
					}
				}
				
				// if current successor is on the OPEN list but the existing one is as good or better then discard this successor and continue
				if (remIndex != -1) {
					if ( ((Vertex)open.get(remIndex)).getG() <= g ) {
						continue;
					}
				}
				
				// find current successor on the CLOSED list
				int remIndex2 = -1;
				for (int indexF = 0; indexF < closed.size(); indexF++) {
					if ( ((Vertex)closed.get(indexF)).equals(successor) ) {
						remIndex2 = indexF;
						break;
					}
				}
				
				// if current successor is on the CLOSED list but the existing one is as good or better then discard this successor and continue
				if (remIndex2 != -1) {
					if ( ((Vertex)closed.get(remIndex2)).getG() <= g ) {
						continue;
					}
				}
				
				// Remove occurences of current successor from OPEN and CLOSED
				if (remIndex != -1) {
					open.remove(remIndex);
				}
				if (remIndex2 != -1) {
					closed.remove(remIndex2);
				}
				
				// Set the parent of current successor to current
				successor.setPath(current.getPathCopy());
				successor.addPathEdge(current);
				
				// Add current successor to the OPEN list
				open.add(open.size(), successor );
			}
			
			// remove current from open and add to closed
			int remIndex = 0;
			for (int index = 0; index < open.size(); index++) {
				if ( ((Vertex)open.get(index)).equals(current) ) {
					remIndex = index;
					break;
				}
			}
			open.remove(remIndex);
			closed.add(closed.size(), current);
		}
		
		// return what was found
		if (closed.size() > 0) {
			// It should be noted that this may not necessarily return a path to the
			// desired Vertex from the starting Vertex. If no path is found, this 
			// version of a* will return a path of erratic behaviour. This is trying
			// to model what would happen in real life in a very basic way.
			return ((Vertex)closed.get(closed.size()-1)).getPathCopy();
		} else {
			// if no path found at all, return an empty ArrayList
			return new ArrayList();
		}
	}//end astar()
	
	
	/**
	* Return a String representation of this Graph.
	* Depends partly on the toString method of the
	* Vertex class.
	*
	* @return  The human readable String representation of this Graph.
	* @see Vertex
	*/
    public String toString()
	{
		String outstring = "";
		for (int i = 0; i < vertexList.size(); i++) {
			Vertex temp = (Vertex)vertexList.get(i);
			outstring = outstring + temp;
			outstring = outstring + "\n";
		}
		return outstring;
    }//end toString
	
	
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
		
		// Create a new graph and populate it with a long list of vertices
		Graph g1 = new Graph();
		
		// Always add Vertices first, then the edges between them. Otherwise
		// you will get Vertices trying to connect to other ones that don't exist.
		// This is a short sightedness of the implementation but it works fine
		// if you obey this simple principle.
		
		// The vertices produce the following theoretical room:
		//
		// a      b      c      d      e      f      g
		// h      i      j      k      l      m      n
		// o      p      q      r      s      t      u
		// v      w      x      y      z     al     be
		// ga    de     ep     ze     et     te     io
		//
		// Every Vertex is connected to each of it's surrounding ones.
		// Note that k, r and y are blocked.
		
		// Create Vertices
		Vertex a = new Vertex("a", 0, 0, 0, false);
		Vertex b = new Vertex("b", 1, 0, 0, false);
		Vertex c = new Vertex("c", 2, 0, 0, false);
		Vertex d = new Vertex("d", 3, 0, 0, false);
		Vertex e = new Vertex("e", 4, 0, 0, false);
		Vertex f = new Vertex("f", 5, 0, 0, false);
		Vertex g = new Vertex("g", 6, 0, 0, false);
		
		Vertex h = new Vertex("h", 0, 1, 0, false);
		Vertex i = new Vertex("i", 1, 1, 0, false);
		Vertex j = new Vertex("j", 2, 1, 0, false);
		Vertex k = new Vertex("k", 3, 1, 0, true);
		Vertex l = new Vertex("l", 4, 1, 0, false);
		Vertex m = new Vertex("m", 5, 1, 0, false);
		Vertex n = new Vertex("n", 6, 1, 0, false);
		
		Vertex o = new Vertex("o", 0, 2, 0, false);
		Vertex p = new Vertex("p", 1, 2, 0, false);
		Vertex q = new Vertex("q", 2, 2, 0, false);
		Vertex r = new Vertex("r", 3, 2, 0, true);
		Vertex s = new Vertex("s", 4, 2, 0, false);
		Vertex t = new Vertex("t", 5, 2, 0, false);
		Vertex u = new Vertex("u", 6, 2, 0, false);
		
		Vertex v = new Vertex("v", 0, 3, 0, false);
		Vertex w = new Vertex("w", 1, 3, 0, false);
		Vertex x = new Vertex("x", 2, 3, 0, false);
		Vertex y = new Vertex("y", 3, 3, 0, true);
		Vertex z = new Vertex("z", 4, 3, 0, false);
		Vertex al = new Vertex("al", 5, 3, 0, false);
		Vertex be = new Vertex("be", 6, 3, 0, false);
		
		Vertex ga = new Vertex("ga", 0, 4, 0, false);
		Vertex de = new Vertex("de", 1, 4, 0, false);
		Vertex ep = new Vertex("ep", 2, 4, 0, false);
		Vertex ze = new Vertex("ze", 3, 4, 0, false);
		Vertex et = new Vertex("et", 4, 4, 0, false);
		Vertex te = new Vertex("te", 5, 4, 0, false);
		Vertex io = new Vertex("io", 6, 4, 0, false);
		
		// Add Vertices to Graph
		g1.addVertex(a);
		g1.addVertex(b);
		g1.addVertex(c);
		g1.addVertex(d);
		g1.addVertex(e);
		g1.addVertex(f);
		g1.addVertex(g);
		
		g1.addVertex(h);
		g1.addVertex(i);
		g1.addVertex(j);
		g1.addVertex(k);
		g1.addVertex(l);
		g1.addVertex(m);
		g1.addVertex(n);
		
		g1.addVertex(o);
		g1.addVertex(p);
		g1.addVertex(q);
		g1.addVertex(r);
		g1.addVertex(s);
		g1.addVertex(t);
		g1.addVertex(u);
		
		g1.addVertex(v);
		g1.addVertex(w);
		g1.addVertex(x);
		g1.addVertex(y);
		g1.addVertex(z);
		g1.addVertex(al);
		g1.addVertex(be);
		
		g1.addVertex(ga);
		g1.addVertex(de);
		g1.addVertex(ep);
		g1.addVertex(ze);
		g1.addVertex(et);
		g1.addVertex(te);
		g1.addVertex(io);
		
		// All Vertices added, now connect them all up.
		g1.addEdge(a,b);
		g1.addEdge(a,h);
		g1.addEdge(a,i);
		
		g1.addEdge(b,c);
		g1.addEdge(b,h);
		g1.addEdge(b,i);
		g1.addEdge(b,j);
		
		g1.addEdge(c,d);
		g1.addEdge(c,i);
		g1.addEdge(c,j);
		g1.addEdge(c,k);
		
		g1.addEdge(d,e);
		g1.addEdge(d,j);
		g1.addEdge(d,k);
		g1.addEdge(d,l);
		
		g1.addEdge(e,f);
		g1.addEdge(e,k);
		g1.addEdge(e,l);
		g1.addEdge(e,m);
		
		g1.addEdge(f,g);
		g1.addEdge(f,l);
		g1.addEdge(f,m);
		g1.addEdge(f,n);
		
		g1.addEdge(g,m);
		g1.addEdge(g,n);
		
		
		g1.addEdge(h,i);
		g1.addEdge(h,o);
		g1.addEdge(h,p);
		
		g1.addEdge(i,j);
		g1.addEdge(i,o);
		g1.addEdge(i,p);
		g1.addEdge(i,q);
		
		g1.addEdge(j,k);
		g1.addEdge(j,p);
		g1.addEdge(j,q);
		g1.addEdge(j,r);
		
		g1.addEdge(k,l);
		g1.addEdge(k,q);
		g1.addEdge(k,r);
		g1.addEdge(k,s);
		
		g1.addEdge(l,m);
		g1.addEdge(l,r);
		g1.addEdge(l,s);
		g1.addEdge(l,t);
		
		g1.addEdge(m,n);
		g1.addEdge(m,s);
		g1.addEdge(m,t);
		g1.addEdge(m,u);
		
		g1.addEdge(n,t);
		g1.addEdge(n,u);
		
		
		g1.addEdge(o,p);
		g1.addEdge(o,v);
		g1.addEdge(o,w);
		
		g1.addEdge(p,q);
		g1.addEdge(p,v);
		g1.addEdge(p,w);
		g1.addEdge(p,x);
		
		g1.addEdge(q,r);
		g1.addEdge(q,w);
		g1.addEdge(q,x);
		g1.addEdge(q,y);
		
		g1.addEdge(r,s);
		g1.addEdge(r,x);
		g1.addEdge(r,y);
		g1.addEdge(r,z);
		
		g1.addEdge(s,t);
		g1.addEdge(s,y);
		g1.addEdge(s,z);
		g1.addEdge(s,al);
		
		g1.addEdge(t,u);
		g1.addEdge(t,z);
		g1.addEdge(t,al);
		g1.addEdge(t,be);
		
		g1.addEdge(u,al);
		g1.addEdge(u,be);
		
		
		g1.addEdge(v,w);
		g1.addEdge(v,ga);
		g1.addEdge(v,de);
		
		g1.addEdge(w,x);
		g1.addEdge(w,ga);
		g1.addEdge(w,de);
		g1.addEdge(w,ep);
		
		g1.addEdge(x,y);
		g1.addEdge(x,de);
		g1.addEdge(x,ep);
		g1.addEdge(x,ze);
		
		g1.addEdge(y,z);
		g1.addEdge(y,ep);
		g1.addEdge(y,ze);
		g1.addEdge(y,et);
		
		g1.addEdge(z,al);
		g1.addEdge(z,ze);
		g1.addEdge(z,et);
		g1.addEdge(z,te);
		
		g1.addEdge(al,be);
		g1.addEdge(al,et);
		g1.addEdge(al,te);
		g1.addEdge(al,io);
		
		g1.addEdge(be,te);
		g1.addEdge(be,io);
		
		
		g1.addEdge(ga,de);
		
		g1.addEdge(de,ep);
		
		g1.addEdge(ep,ze);
		
		g1.addEdge(ze,et);
		
		g1.addEdge(et,te);
		
		g1.addEdge(te,io);
		
		
		// Output graph
		doOutput("\n");
		doOutput("Graph before operations:\n");
		doOutput(g1);
		doOutput("\n\n");
		
		// Let's get a random Vertex
		doOutput("Random Vertex: \n" + g1.getRandomVertex());
		
		// Do a depth first traversal
		doOutput("\n\n");
		doOutput("Depth-First Traversal:\n");
		ArrayList temp = g1.dfTraverse(a);
		for (int ki = 0; ki < temp.size(); ki++) {
			doOutput( ((Vertex)temp.get(ki)).getName() + " ");
		}
		doOutput("\n\n");
		
		// Let's do an a* path find on two random points.
		// n.b., I'm not checking that start or end Vertices
		// are blocked which may lead to somewhat 
		// unusual results at times.
		doOutput("A* Search One:\n");
		ArrayList temp1 = g1.astar(g1.getRandomVertex(), g1.getRandomVertex());
		for (int ki = 0; ki < temp1.size(); ki++) {
			doOutput( ((Vertex)temp1.get(ki)).getName() + " ");
		}
		
		// Let's do another a* path find on two random points to check
		// that the first one hasn't left side-effects preventing a second 
		// one from working
		doOutput("\n\nA* Search Two:\n");
		ArrayList temp2 = g1.astar(g1.getRandomVertex(), g1.getRandomVertex());
		for (int ki = 0; ki < temp2.size(); ki++) {
			doOutput( ((Vertex)temp2.get(ki)).getName() + " ");
		}
		
		// What the hell, let's do another one
		doOutput("\n\nA* Search Three:\n");
		ArrayList temp3 = g1.astar(g1.getRandomVertex(), g1.getRandomVertex());
		for (int ki = 0; ki < temp3.size(); ki++) {
			doOutput( ((Vertex)temp3.get(ki)).getName() + " ");
		}
		
		// Check that Graph remains unchanged after operations
		doOutput("\n\n\nGraph after operations:\n");
		doOutput(g1);	
	}//end main()*/
	
}//end class Graph