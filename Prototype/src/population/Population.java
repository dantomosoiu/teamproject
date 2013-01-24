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
import goal.ExitGoal;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

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
    private ArrayList<PersonCluster> personClusterList; //list of clusters of closely positioned persons
    private BoundaryComparator bComp = new BoundaryComparator(); //comparator used in sorting the persons based on an individual axis

    public Population(com.jme3.scene.Node rootNode, NavMesh mesh, SimpleApplication simp) {
        this.mesh = mesh;
        this.simp = simp;
        this.rootNode = rootNode;
        GoalParser.parseGoals("assets/Input/Goals.csv");
        for(ExitGoal exit: BehaviourModel.getExits()){
            System.out.println("EXIT: " + exit.getLocation());
        }
        

    }

    public void populate(int popNumber) {
       // GoalParser.parseGoals("/users/level3/1003819k/teamproject/Prototype/assets/Input/Goals.csv");
        people = new Person[popNumber];

        ArrayList<Vector3f> personPositions = new ArrayList<Vector3f>();
        int totalCells = mesh.getNumCells();
        Random rand = new Random();

        peopleThreads = new Thread[popNumber];


        for (int i = 0; i < popNumber; i++) {
            boolean foundCandidate = false;
            while (!foundCandidate) {
                Vector3f candidate = mesh.getCell(rand.nextInt(totalCells)).getRandomPoint();
                boolean overlaps = false;
                for (Vector3f position : personPositions) {
                    if (position.distance(candidate) < 0.02f) {
                        overlaps = true;
                        break;
                    }
                }
                
                if(overlaps){
                    continue;
                }
                foundCandidate = true;
                personPositions.add(candidate);
                people[i] = new Person(mesh, rootNode, simp, candidate);
                peopleThreads[i] = new Thread(people[i]);
            }

            //people[i] = new Person(mesh, rootNode, simp, new Vector3f(FastMath.nextRandomInt(-3, 3) + FastMath.nextRandomFloat(), 0, FastMath.nextRandomInt(-3, 3) + FastMath.nextRandomFloat()));
            //peopleThreads[i] = new Thread(people[i]);
        }
        refreshPersonClusters();
    }

    private class PersonCluster {

        LinkedList<Person> persons;
        int lastSplitDimention;

        private PersonCluster() {
            this.persons = new LinkedList<Person>();
            this.lastSplitDimention = -1;
        }

        private PersonCluster(int x) {
            this.persons = new LinkedList<Person>();
            this.lastSplitDimention = x;
        }
    }

    private void refreshPersonClusters() {
        ArrayList<PersonCluster> newClusterList = new ArrayList<PersonCluster>();

        PersonCluster allPersons = new PersonCluster(0);
        allPersons.persons.addAll(Arrays.asList(people));

        RDC(newClusterList, allPersons, 0);

        this.personClusterList = newClusterList;
        System.out.println(personClusterList.size());
    }

    private void RDC(ArrayList<PersonCluster> finalClusterList, PersonCluster currentCluster, int dimension) {
        //assert (dimension > -1);

        LinkedList<PersonBoundary> sortedBoundaries = new LinkedList<PersonBoundary>();
        for (Person p : currentCluster.persons) {
            sortedBoundaries.add(p.getBoundary((short) dimension, true)); //adds the "left" boundary of specified dimension
            sortedBoundaries.add(p.getBoundary((short) dimension, false)); //adds the "right" boundary of specified dimension
        }

        Collections.sort(sortedBoundaries, bComp); //sorts the list of boundaries

        int openCount = 0;
        LinkedList<PersonCluster> subClusters = new LinkedList<PersonCluster>(); //will hold the clusters that were split in this recursion step
        subClusters.add(new PersonCluster(dimension));

        for (PersonBoundary b : sortedBoundaries) {
            if (b.opening) {
                openCount++;
                subClusters.getLast().persons.add(b.person); //there's a new person, add it to the current cluster
            } else {
                openCount--;
                if (openCount == 0) { //no more persons in this clusters, create a new one
                    subClusters.add(new PersonCluster(dimension));
                }
            }
        }

        subClusters.removeLast(); //remove the last (empty) cluster

        if (subClusters.size() == 1) { //current cluster wasn't divided, check if time to end recursion
            if ((currentCluster.lastSplitDimention + 2) % 3 == dimension) { //all 3 dimensions were tested
                finalClusterList.add(currentCluster);
                // return
            } else {
                RDC(finalClusterList, currentCluster, (dimension + 1) % 3); //test on the next dimension
            }
        } else {
            for (PersonCluster c : subClusters) { //for each subcluster that was split in this step, 
                RDC(finalClusterList, c, (dimension + 1) % 3); //recurse again
            }
        }
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
