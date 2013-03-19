/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.population;

/**
 *
 * @author 1102486t
 */
public class PersonBoundary {
    /**
     *
     */
    public Person person;
    /**
     *
     */
    public float position;
    /**
     *
     */
    public short axis;
    /**
     *
     */
    public boolean opening;
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        String s;
        if(opening) {
            s = " (open)";
        } else {
            s = " (close)";
        }
        
        return Float.toString(position) + s;
    }
}
