/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package EvacSim.population;

import EvacSim.EvacSim;
import EvacSim.goal.ExitGoal;
import EvacSim.jme3tools.navmesh.Cell;
import GUI.Components.SidePanel;
import Init.Settings.PersonCategory;
import Init.Settings.Settings;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author michael, tony, dan
 */
/**
 *
 * @author michael
 */
public class Population implements Runnable {
    /**
     *
     */
    public static float DISTANCEBETWEENMOTIONWAYPOINTS = 0.5f;
    
    private EvacSim evs;
    private Person people[];
    private Thread peopleThreads[];
    //private ArrayList<PersonCluster> personClusterList; //list of clusters of closely positioned persons
    //private BoundaryComparator bComp = new BoundaryComparator(); //comparator used in sorting the persons based on an indvidual axis
    private static boolean evacuationDone;
    //distance from the center of a person to the edge of its surface area.
    //used in generating non-overlaping persons
    //should be linked with a persons surface area
    private float personCollisionDistance = 0.2f; 
    private Settings settings;

    /**
     *
     * @param evs
     */
    public Population(EvacSim evs) {
        this.evs = evs;
        evacuationDone = false;
        settings = Settings.get();
        settings.setNumEvac(0);
        GoalParser.parseGoals("assets/Input/Goals.csv");
        for(ExitGoal exit: BehaviourModel.getExits()){
            System.out.println("EXIT: " + exit.getLocation());
        }
        

    }

    /**
     *
     */
    public void populate() {
        people = new Person[settings.getPopulationNumber()];

        ArrayList<Vector3f> personPositions = new ArrayList<Vector3f>();
        int totalCells = settings.getNavMesh().getNumCells();
        Random rand = new Random();

        peopleThreads = new Thread[settings.getPopulationNumber()];

        float cellAverage = 0;
        for(int i=0;i<totalCells;i++){
            cellAverage += settings.getNavMesh().getCell(i).getArea();
        }
        cellAverage /= totalCells;
        
        ArrayList<Cell> largeCells = new ArrayList<Cell>();
        for(int i=0;i<totalCells;i++){
            if(settings.getNavMesh().getCell(i).getArea() > cellAverage){
                largeCells.add(settings.getNavMesh().getCell(i));
            }
        }
        
        //creates the persons with randomly generated (non-overlaping) positions on the navmesh
        //generate population by category assigned in settings
        int totalNumberAllocated = 0;
        Set catNameSet = settings.getPersonCategories().keySet();
        for(Object cat: catNameSet){
            String catName = (String) cat;
            PersonCategory category = settings.getPersonCategories().get(catName);
            int numberInCat =(int) ((float)(1/category.getNumberOfPeople()) * (float)settings.getPopulationNumber()); //calcuates the percentage of people
            for(int i = 0; i < numberInCat; i++){
                /*generate a random starting position*/
                boolean foundCandidate = false;
                while (!foundCandidate) {
                    Vector3f candidate = largeCells.get(rand.nextInt(largeCells.size())).getRandomPoint();

                    boolean overlaps = false;
                    for (Vector3f position : personPositions) {
                        if (position.distance(candidate) < personCollisionDistance) {
                            overlaps = true;
                            break;
                        }
                    }

                    if(overlaps){
                        continue;
                    }
                    foundCandidate = true;
                    personPositions.add(candidate);

                    people[totalNumberAllocated] = new Person(evs,candidate,category, this);
                    peopleThreads[totalNumberAllocated] = new Thread(people[totalNumberAllocated]);
                    System.out.println("TNA: " + totalNumberAllocated);
                    totalNumberAllocated++;
                   
                }
            }
            //Assign any remaining people randomly
            
            if(totalNumberAllocated < settings.getPopulationNumber()){
                int noOfCategories = settings.getPersonCategories().size();
                while(totalNumberAllocated < settings.getPopulationNumber()){
                       /*generate a random starting position*/
                    boolean foundCandidate = false;
                    while (!foundCandidate) {
                        Vector3f candidate = largeCells.get(rand.nextInt(largeCells.size())).getRandomPoint();

                        boolean overlaps = false;
                        for (Vector3f position : personPositions) {
                            if (position.distance(candidate) < personCollisionDistance) {
                                overlaps = true;
                                break;
                            }
                        }

                        if(overlaps){
                            continue;
                        }
                        foundCandidate = true;
                        personPositions.add(candidate);
                        
                        int targetCatNo =(int) Math.random() * noOfCategories;
                        String targetCatKey = (String) catNameSet.toArray()[targetCatNo];
                        people[totalNumberAllocated] = new Person(evs,candidate,category,this);
                        peopleThreads[totalNumberAllocated] = new Thread(people[totalNumberAllocated]);
                        totalNumberAllocated++;
                    }
                   
                    
                }
            }
        }
           
        
        SidePanel.enablePop();
        //refreshPersonClusters();
    }
    
  
    
    /**
     * Contains a list of persons that are situated closely to each-other.
     */
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
    /** Refreshes the list holding the groups (clusters) holding 
     * neighbourhoods of closely-positioned persons.
     * This method needs to be called periodically to ensure that persons
     * have enough informations about their current neighbourhood in order
     * to safely avoid collisions.
     */
    /*private void refreshPersonClusters() {
        ArrayList<PersonCluster> newClusterList = new ArrayList<PersonCluster>();

        PersonCluster allPersons = new PersonCluster(0);
        allPersons.persons.addAll(Arrays.asList(people));

       // RDC(newClusterList, allPersons, 0);

        //this.personClusterList = newClusterList;
        //System.out.println(personClusterList.size());
    }*/

    /**
     * Recursively splits the current PersonCluster along the 3 dimensions, until no splits can be made.
     */
//    private void RDC(ArrayList<PersonCluster> finalClusterList, PersonCluster currentCluster, int dimension) {
//        //assert (dimension > -1);
//
//        LinkedList<PersonBoundary> sortedBoundaries = new LinkedList<PersonBoundary>();
//        for (Person p : currentCluster.persons) {
//            sortedBoundaries.add(p.getBoundary((short) dimension, true)); //adds the "left" boundary of specified dimension
//            sortedBoundaries.add(p.getBoundary((short) dimension, false)); //adds the "right" boundary of specified dimension
//        }
//
//        Collections.sort(sortedBoundaries, bComp); //sorts the list of boundaries
//
//        int openCount = 0;
//        LinkedList<PersonCluster> subClusters = new LinkedList<PersonCluster>(); //will hold the clusters that were split in this recursion step
//        subClusters.add(new PersonCluster(dimension));
//
//        for (PersonBoundary b : sortedBoundaries) {
//            if (b.opening) {
//                openCount++;
//                subClusters.getLast().persons.add(b.person); //there's a new person, add it to the current cluster
//            } else {
//                openCount--;
//                if (openCount == 0) { //no more persons in this clusters, create a new one
//                    subClusters.add(new PersonCluster(dimension));
//                }
//            }
//        }
//
//        subClusters.removeLast(); //remove the last (empty) cluster
//
//        if (subClusters.size() == 1) { //current cluster wasn't divided, check if time to end recursion
//            if ((currentCluster.lastSplitDimention + 2) % 3 == dimension) { //all 3 dimensions were tested
//                finalClusterList.add(currentCluster);
//                // return
//            } else {
//                RDC(finalClusterList, currentCluster, (dimension + 1) % 3); //test on the next dimension
//            }
//        } else {
//            for (PersonCluster c : subClusters) { //for each subcluster that was split in this step, 
//                RDC(finalClusterList, c, (dimension + 1) % 3); //recurse again
//            }
//        }
 // }

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

    /**
     *
     */
    public void play() {
        for (int i = 0; i < people.length; i++) {
            if (!people[i].isFin()) {
                people[i].play();
            }
        }
    }
    
    /**
     *
     */
    public void stopSim() {
        for (int i = 0; i < people.length; i++) {
            if (people[i].isStart() == true && people[i].isFin() == false) people[i].pause();
        }
    }

    /**
     *
     * @param tpf
     */
    public void update(float tpf) {
        //for(int i = 0; i < peopleThreads.length; i++){
        //    people[i].update(tpf);
        //}
    }

    /**
     *
     */
    public static void done() {
        evacuationDone = true;
    }

    /**
     *
     * @return
     */
    public boolean isDone(){
        return this.evacuationDone;
    }
    
    /**
     *
     */
    public void run() {
    }
}
