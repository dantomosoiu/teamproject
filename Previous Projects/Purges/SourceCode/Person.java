// Library needed for the getPoint() method
import javax.vecmath.*;

/**
* This class is used to represent People in the PURGES
* Evacuation Simulator. It is not general purpose and it
* is not designed as a general purpose representation. 
*
* This represents thoeretical people, not the 3D objects
* of people.
*
* @author John Urquhart Ferguson
* @version 0.03
* @see Graph
* @see Vertex
*/
/*
* Last Modified: 6th March, 2006: 16:50 GMT
*
* Status: Has worked correctly for all test input.
*
* Notes: There is almost no error checking in this class.
* It is assumed that everything is used correctly and with
* sensible parameters. It's not thread safe at the moment.
*
* Added Monte Carlo value for random decision making.
*/


public class Person
{
	
	// The curent location of the Person
	private Vertex current;
	
	// Where the Person is currently trying to get to
	private Vertex goal;
	
	// The human readable name of the Person
	private String name;
	
	// Monte Carlo value for 'random decision making'
	// and 'aggressiveness'
	private int monte;
	
	// The following variables are used for the 
	// 3D animation of movement for a person
	private float x;
	private float y;
	private float z;
	private boolean moving;
	private float speed = 0.001f;
	
	
	// ************ Constructors ************
	
    /**
	* Default constructor if no arguements supplied.
	*/
	public Person()
	{
		// Just set everything to a default value.
		name = "default person";
		current = new Vertex();
		goal = new Vertex();
		x = 0;
		y = 0;
		z = 0;
		moving = false;
		monte = 0;
	}
	
	/**
	* Constructor if specified arguments are supplied.
	*
	* @param nin  Human readable name for Person.
	*/
	public Person (String nin)
	{
		name = nin;
		current = new Vertex();
		goal = new Vertex();
		x = 0;
		y = 0;
		z = 0;
		moving = false;
		monte = 0;
	}
	
	/**
	* Constructor if specified arguments are supplied.
	*
	* @param nin  Human readable name for Person.
	* @param cin  Current Vertex position for Person.
	* @param gin  Vertex position Person is trying to get to.
	*/
	public Person(String nin, Vertex cin, Vertex gin)
	{
		name = nin;
		current = cin;
		
		// Make sure that the program can tell there
		// is someone at this Vertex position.
		current.block();
		
		goal = gin;
		x = 0;
		y = 0;
		z = 0;
		moving = false;
		monte = 0;
	}
	
	/**
	* Constructor if specified arguments are supplied.
	*
	* @param nin  Human readable name for Person.
	* @param cin  Current Vertex position for Person.
	* @param gin  Vertex position Person is trying to get to.
	* @param min  Monte Carlo value for this person.
	*/
	public Person(String nin, Vertex cin, Vertex gin, int min)
	{
		name = nin;
		current = cin;
		
		// Make sure that the program can tell there
		// is someone at this Vertex position.
		current.block();
		
		goal = gin;
		x = 0;
		y = 0;
		z = 0;
		moving = false;
		monte = min;
	}
	
	// ************ end Constructors ************
	
	
	/**
	* Return the current position of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @return  The Point3f representing the current position of this Person.
	*/
	public Point3f getPoint()
	{
		return (new Point3f(x, y, z));
	}//end getPoint()
	
	
	/**
	* Return the current X value of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @return  The float representing the current X value of this Person.
	*/
	public float getX()
	{
		return x;
	}//end getX()
	
	
	/**
	* Return the current Y value of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @return  The float representing the current Y value of this Person.
	*/
	public float getY()
	{
		return y;
	}//end getY()
	
	
	/**
	* Return the current Z value of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @return  The float representing the current Z value of this Person.
	*/
	public float getZ()
	{
		return z;
	}//end getZ()
	
	
	/**
	* Return the current Monte Carlo value of this person.
	*
	* @return  The int representing the current Monte Carlo value of this Person.
	*/
	public int getMonte()
	{
		return monte;
	}//end getMonte()
	
	
	/**
	* Change the current Monte Carlo value of this person.
	*
	* @param min The int of the new Monte Carlo value for this Person.
	*/
	public void setMonte(int min)
	{
		monte = min;
	}//end setMonte()
	
	
	/**
	* Change the current X value of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @param xin The float of the new real world X position for this Person.
	*/
	public void setX(float xin)
	{
		x = xin;
	}//end setX()
	
	
	/**
	* Change the current Y value of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @param yin The float of the new real world Y position for this Person.
	*/
	public void setY(float yin)
	{
		y = yin;
	}//end setY()
	
	
	/**
	* Change the current Z value of this person. This is
	* intended for use in animation only. The actual 
	* Vertex returned by getCurrent() is a better 
	* indication of the location of a Person for things
	* like route finding.
	*
	* @param zin The float of the new real world Z position for this Person.
	*/
	public void setZ(float zin)
	{
		z = zin;
	}//end setZ()
	
	
	/**
	* Return a boolean if the Person is still
	* being animated.
	*
	* @return  The boolean representing if this Person is in the process of moving.
	*/
	public boolean isMoving()
	{
		return moving;
	}//end isMoving()
	
	
	/**
	* Update the Person's real world coordinates
	* a little in the direction of the Person's current
	* Vertex position unless they have reached the
	* 'current' position. The amount moved is 
	* dependent on the speed of this Person.
	*/
	public void animate()
	{
		// Don't animate anything unless we're actually supposed to be moving
		if (moving) {
			// The next three if statements are just determining what direction to
			// move in and by how much. The code should correctly use trigonometry
			// to move people in straight lines from source to destination. However,
			// that would require a lot more processing, and this way produces
			// perfectly valid looking results and is a lot more efficient.
			if ( Math.abs(x - current.getX()) > speed) {
				if ( (x - current.getX()) > 0) {
					x -= speed;
				} else {
					x += speed;
				}
			}
			if ( Math.abs(y - current.getY()) > speed) {
				if ( (y - current.getY()) > 0) {
					y -= speed;
				} else {
					y += speed;
				}
			}
			if ( Math.abs(z - current.getZ()) > speed) {
				if ( (z - current.getZ()) > 0) {
					z -= speed;
				} else {
					z += speed;
				}
			}
			
			// Check to see if we've reached the Vertex the animation is moving us towards (or near enough).
			// If there, we've finished moving.
			if ( (Math.abs(x - current.getX()) <= speed) && (Math.abs(y - current.getY()) <= speed) && (Math.abs(z - current.getZ()) <= speed) ) {
				moving = false;
			}
		}
	}//end animate()
	
	
	/**
	* Change the current Vertex position of this Person.
	*
	* @param vin  The new Vertex position of this Person.
	*/
	public void setCurrent(Vertex vin)
	{
		current = vin;
	}//end setCurrent()
	
	
	/**
	* Return the current Vertex of this Person.
	*
	* @return  The Vertex representation of the Person's position.
	*/
	public Vertex getCurrent()
	{
		return current;
	}//end getCurrent()
	
	
	/**
	* Change the goal Vertex position of this Person.
	*
	* @param vin  The new Vertex goal position of this Person.
	*/
	public void setGoal(Vertex vin)
	{
		goal = vin;
	}//end setGoal()
	
	
	/**
	* Return the current Goal Vertex of this Person.
	*
	* @return  The Vertex representation of the Person's Goal.
	*/
	public Vertex getGoal()
	{
		return goal;
	}//end getGoal()
	
	
	/**
	* Change the human readable name of this Person.
	*
	* @param nin  The new name of this Person.
	*/
	public void setName(String nin)
	{
		name = nin;
	}//end setName()
	
	
	/**
	* Return the human readable name of this Person.
	*
	* @return  The human readable name of this Person.
	*/
	public String getName()
	{
		return name;
	}//end getName()
	
	
	/**
	* Return true if this Person has carried out full animation.
	*
	* @return  The boolean of whether or not animation is finished.
	*/
	public boolean arrived()
	{
		return current.equals(goal);
	}//end arrived()
	
	
	/**
	* Attempt to move a Person to a new Vertex location.
	* If successful, method will return true. If 
	* unsuccessful (e.g., if destination is blocked), then
	* method will return false.
	*
	* @param vin   The Vertex to move Person to
	* @return  The success value of this method.
	* @see Vertex
	*/
	public boolean move(Vertex vin)
	{
		if (vin.isBlocked()) {
			return false;
		} else {
			vin.block();
			current.unblock();			
			x = current.getX();
			y = current.getY();
			z = current.getZ();
			current = vin;
			moving = true;
			return true;
		}
	}//end move()
	
	
	/**
	* Unblock the current Vertex that this Person
	* is located at. This is intended to be called
	* if the Person is being removed from a
	* Graph
	*
	* @see Graph
	* @see Vertex
	*/
	public void unblock()
	{
		current.unblock();
	}//end unblock()
	
	
	/**
	* Change the speed of this Person. This
	* determines how much a person will move
	* in each animation frame when using the
	* animate method. If this method is never
	* called, a default value is used.
	*
	* @param sin  The new speed of this Person.
	*/
	public void setSpeed(float sin)
	{
		speed = sin;
	}//end setSpeed()
	
	
	/**
	* Return a String representation of this Person.
	* Depends partly on the toString method of the
	* Vertex class.
	*
	* @return  The human readable String representation of this Person.
	* @see Vertex
	*/
	public String toString()
	{
		String outstring = "Person: " + name + "\n" + "Current:\n" + current + "\n" + "Goal:\n" + goal;
		if (isMoving()) {
			outstring = outstring + "\nMoving: Yes";
		} else {
			outstring = outstring + "\nMoving: No";
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
		doOutput("\nDefault Constructor Test: \n\n");
		Person test = new Person();		
		doOutput(test + "\n");
		
		doOutput("\n\nName Only Constructor Test: \n\n");
		test = new Person("Bob");
		doOutput(test + "\n");
		
		doOutput("\n\nFull Constructor Test: \n\n");
		Vertex v1 = new Vertex("v1", 1, 2, 3);
		Vertex v2 = new Vertex("v2", 1, 2, 3);
		v1.addEdge(v2);
		v2.addEdge(v1);
		test = new Person("Sammy", v1, v2);
		doOutput(test + "\n");
		
		doOutput("\n\nArrival Test: \n\n");
		if (test.arrived()) {
			doOutput(test.getName() + " has arrived\n");
		} else {
			doOutput(test.getName() + " has not arrived\n");
		}
		
		doOutput("\n\nMovement Test: \n\n");
		doOutput("Moving to v2...\n");
		test.move(v2);
		doOutput(test + "\n");
		
		doOutput("\n\nArrival Test 2:\n\n");
		if (test.arrived()) {
			doOutput(test.getName() + " has arrived\n");
		} else {
			doOutput(test.getName() + " has not arrived\n");
		}
	}//end main()*/

}//end class Person