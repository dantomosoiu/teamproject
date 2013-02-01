/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goal;

import population.Person;

import com.jme3.math.Vector3f;

/**
 *
 * @author 1003819k/1102266l
 */
public class ExitSignGoal extends Goal {

	private  Vector3f sideA;
	private  Vector3f sideB;
	private  Vector3f normal;

	public ExitSignGoal(Vector3f location,Vector3f n ,float c) {
		super(location);
		normal = n.normalize();
		sideA = location.add(normal.mult(c));
		sideB = location.subtract(normal.mult(c));

	}


	public Vector3f registerWithGoal(Person p){
		return null;
	}

}
