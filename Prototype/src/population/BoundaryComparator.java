/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import java.util.Comparator;

/**
 *
 * @author 1102486t
 */
public class BoundaryComparator implements Comparator<PersonBoundary> {

    public int compare(PersonBoundary o1, PersonBoundary o2) {
        float p1 = o1.position;
        float p2 = o2.position;
        if (p1 < p2) {
            return -1;
        } else if (p1 > p2) {
            return 1;
        }
        return 0;
    }
}
