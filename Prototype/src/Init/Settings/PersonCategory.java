/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Init.Settings;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author 1003819k
 */
public class PersonCategory implements Serializable{
    private String name;
    private float minspeed;
    private float maxspeed;
    private float minstress;
    private float maxstress;
    private String color;
    private int numberOfPeople;
    private Random random;
    
    public PersonCategory(String name) {
        this.name = name;
        minspeed = maxspeed = minstress = maxstress = 1;
        color = "White";
        numberOfPeople = 0;
    }
    
    public PersonCategory(String name, float minspeed, float maxspeed, float minstress, float maxstress, String color, int numberOfPeople) {
        this.name = name;
        this.minspeed = minspeed;
        this.maxspeed = maxspeed;
        this.minstress = minstress;
        this.maxstress = maxstress;
        this.color = color;
        this.numberOfPeople = numberOfPeople;
        this.random = new Random();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMinspeed() {
        return minspeed;
    }

    public void setMinspeed(float minspeed) {
        this.minspeed = minspeed;
    }

    public float getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(float maxspeed) {
        this.maxspeed = maxspeed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
    
    public float generateSpeed(){
         return minspeed + (random.nextFloat() * (maxspeed - minspeed));
    }

    public float generateStress(){
        return minstress + (random.nextFloat() * (maxstress - minstress));
    }
    
}
