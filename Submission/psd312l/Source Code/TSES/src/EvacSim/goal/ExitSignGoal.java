/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.goal;

import EvacSim.population.Person;
import com.jme3.math.Vector3f;

/**
 *
 * @author 1003819k/1102266l
 */
public class ExitSignGoal extends Goal {
	private  Vector3f normal;

	/**
     *
     * @param location
     * @param n
     * @param c
     */
    public ExitSignGoal(Vector3f location,Vector3f n ,float c) {
		super(location);
		normal = n.normalize();
		
	}


	/**
     *
     * @param p
     * @return
     */
    public Vector3f registerWithGoal(Person p){
		return null;
	}

}
