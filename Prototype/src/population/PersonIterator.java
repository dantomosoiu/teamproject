/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import java.util.Iterator;

/**
 *
 * @author michael
 */
public class PersonIterator implements Iterator {
    Person people[];
    int pos = 0;
    
    public PersonIterator() {
     
    }

    public boolean hasNext() {
        return (pos < people.length);       //safe since array is always full
    }

    public Object next() {
        Object p = people[pos];
        pos++;
        return p;
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
