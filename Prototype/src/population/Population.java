/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import jme3tools.navmesh.NavMesh;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/*
 *
 * @author michael, tony, dan
 */
public class Population implements Runnable {

    public static Vector3f GOAL = new Vector3f(1f, 0f, 1f);
    private SimpleApplication simp;
    private Person people[];
    private Thread peopleThreads[];
    private com.jme3.scene.Node rootNode;
    private NavMesh mesh;
    private ArrayList<LinkedList<Person>> neighbourList;
    private BoundaryComparator bComp = new BoundaryComparator();

    public Population(com.jme3.scene.Node rootNode, NavMesh mesh, SimpleApplication simp) {
        this.mesh = mesh;
        this.simp = simp;
        this.rootNode = rootNode;
    }

    public void populate(int popNumber) {
        people = new Person[popNumber];
        peopleThreads = new Thread[popNumber];
        for (int i = 0; i < popNumber; i++) {
            people[i] = new Person(mesh, rootNode, simp, new Vector3f(FastMath.nextRandomInt(-3, 3) + FastMath.nextRandomFloat(), 0, FastMath.nextRandomInt(-3, 3) + FastMath.nextRandomFloat()));
            peopleThreads[i] = new Thread(people[i]);
        }
        neighbourList = new ArrayList<LinkedList<Person>>(popNumber);
        refreshNeighbourList();
    }

    private void refreshNeighbourList() {
        ArrayList<LinkedList<Person>> tempNeighbourList = new ArrayList<LinkedList<Person>>(people.length);
        LinkedList<PersonBoundary> xSorted = new LinkedList<PersonBoundary>();

        for (Person p : people) {
            xSorted.add(p.getBoundary((short) 0, true));
            xSorted.add(p.getBoundary((short) 0, false));
        }

        LinkedList<Person> group = new LinkedList<Person>();
        ArrayList<LinkedList<Person>> xGroups = new ArrayList<LinkedList<Person>>();

        Collections.sort(xSorted, bComp);
        int groupCount = 0;

        int openCount = 0;

        System.out.println("X Groups:");

        for (PersonBoundary b : xSorted) {
            if (b.opening) {
                openCount++;
                group.add(b.person);
            } else {
                openCount--;
                if (openCount == 0) {
                    xGroups.add(new LinkedList<Person>());
                    for (Person p : group) {
                        System.out.println("(" + groupCount + ")" + p.getPosition());
                        xGroups.get(groupCount).add(p);
                    }
                    group = new LinkedList<Person>();
                    groupCount++;
                }
            }
        }

        ArrayList<LinkedList<Person>> yGroups = new ArrayList<LinkedList<Person>>();

        groupCount = 0;
        System.out.println("Y Groups:");

        for (LinkedList<Person> currentXGroup : xGroups) {

            if (currentXGroup.size() == 1) {
                //System.out.println("(" + groupCount + ")" + currentXGroup.get(0).getPosition());
                tempNeighbourList.add(currentXGroup);
                //groupCount++;
                continue;
            }

            LinkedList<PersonBoundary> ySorted = new LinkedList<PersonBoundary>();

            for (Person p : currentXGroup) {
                ySorted.add(p.getBoundary((short) 1, true));
                ySorted.add(p.getBoundary((short) 1, false));
            }

            Collections.sort(ySorted, bComp);

            openCount = 0;

            for (PersonBoundary b : ySorted) {
                if (b.opening) {
                    openCount++;
                    group.add(b.person);
                } else {
                    openCount--;
                    if (openCount == 0) {
                        yGroups.add(new LinkedList<Person>());

                        for (Person p : group) {
                            System.out.println("(" + groupCount + ")" + p.getPosition());
                            yGroups.get(groupCount).add(p);
                        }
                        group = new LinkedList<Person>();
                        groupCount++;
                    }
                }
            }
        }


        ArrayList<LinkedList<Person>> zGroups = new ArrayList<LinkedList<Person>>();

        groupCount = 0;
        System.out.println("Z Groups:");

        for (LinkedList<Person> currentYGroup : yGroups) {

            if (currentYGroup.size() == 1) {
                //System.out.println("(" + groupCount + ")" + currentYGroup.get(0).getPosition());
                tempNeighbourList.add(currentYGroup);
                //groupCount++;
                continue;
            }

            LinkedList<PersonBoundary> zSorted = new LinkedList<PersonBoundary>();

            for (Person p : currentYGroup) {
                zSorted.add(p.getBoundary((short) 2, true));
                zSorted.add(p.getBoundary((short) 2, false));
            }

            Collections.sort(zSorted, bComp);

            openCount = 0;

            for (PersonBoundary b : zSorted) {
                if (b.opening) {
                    openCount++;
                    group.add(b.person);
                } else {
                    openCount--;
                    if (openCount == 0) {
                        zGroups.add(new LinkedList<Person>());

                        for (Person p : group) {
                            zGroups.get(groupCount).add(p);
                            System.out.println("(" + groupCount + ")" + p.getPosition());
                        }
                        group = new LinkedList<Person>();
                        groupCount++;
                    }
                }
            }
        }

        ArrayList<LinkedList<Person>> XXGroups = new ArrayList<LinkedList<Person>>();

        groupCount = 0;
        System.out.println("XX Groups:");

        for (LinkedList<Person> currentZGroup : zGroups) {

            if (currentZGroup.size() == 1) {
                //System.out.println("(" + groupCount + ")" + currentZGroup.get(0).getPosition());
                tempNeighbourList.add(currentZGroup);
                //groupCount++;
                continue;
            }

            LinkedList<PersonBoundary> XXSorted = new LinkedList<PersonBoundary>();

            for (Person p : currentZGroup) {
                XXSorted.add(p.getBoundary((short) 0, true));
                XXSorted.add(p.getBoundary((short) 0, false));
            }

            Collections.sort(XXSorted, bComp);

            openCount = 0;

            for (PersonBoundary b : XXSorted) {
                if (b.opening) {
                    openCount++;
                    group.add(b.person);
                } else {
                    openCount--;
                    if (openCount == 0) {
                        XXGroups.add(new LinkedList<Person>());

                        for (Person p : group) {
                            XXGroups.get(groupCount).add(p);
                            System.out.println("(" + groupCount + ")" + p.getPosition());
                        }
                        group = new LinkedList<Person>();
                        groupCount++;
                    }
                }
            }
        }

        ArrayList<LinkedList<Person>> YYGroups = new ArrayList<LinkedList<Person>>();

        groupCount = 0;
        System.out.println("YY Groups:");

        for (LinkedList<Person> currentXXGroup : XXGroups) {

            if (currentXXGroup.size() == 1) {
                //System.out.println("(" + groupCount + ")" + currentXXGroup.get(0).getPosition());
                tempNeighbourList.add(currentXXGroup);
                //groupCount++;
                continue;
            }

            LinkedList<PersonBoundary> YYSorted = new LinkedList<PersonBoundary>();

            for (Person p : currentXXGroup) {
                YYSorted.add(p.getBoundary((short) 1, true));
                YYSorted.add(p.getBoundary((short) 1, false));
            }

            Collections.sort(YYSorted, bComp);

            openCount = 0;

            for (PersonBoundary b : YYSorted) {
                if (b.opening) {
                    openCount++;
                    group.add(b.person);
                } else {
                    openCount--;
                    if (openCount == 0) {
                        YYGroups.add(new LinkedList<Person>());

                        for (Person p : group) {
                            YYGroups.get(groupCount).add(p);
                            System.out.println("(" + groupCount + ")" + p.getPosition());
                        }
                        group = new LinkedList<Person>();
                        groupCount++;
                    }
                }
            }
        }


        tempNeighbourList.addAll(YYGroups);
        
        System.out.println(tempNeighbourList.size());
    }

    public void evacuate() {
        for (int i = 0; i < peopleThreads.length; i++) {
            peopleThreads[i].run();
        }

        /* for(int i = 0; i < peopleThreads.length; i++){
         try{
         peopleThreads[i].join();
         }catch(InterruptedException e){return ;}
         }*/
    }

    public void play() {
        for (int i = 0; i < people.length; i++) {
            people[i].play();
        }
    }

    public void update(float tpf) {
        //for(int i = 0; i < peopleThreads.length; i++){
        //    people[i].update(tpf);
        //}
    }

    public void run() {
    }
}
