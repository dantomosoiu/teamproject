/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package population;

import com.jme3.math.ColorRGBA;

/**
 *
 * @author 1003819k
 */
public class PersonCategory {
    private String name;
    private double minspeed;
    private double maxspeed;
    private double minstress;
    private double maxstress;
    private ColorRGBA color;
    private int numberOfPeople;

    public PersonCategory(String name, double minspeed, double maxspeed, double minstress, double maxstress, ColorRGBA color, int numberOfPeople) {
        this.name = name;
        this.minspeed = minspeed;
        this.maxspeed = maxspeed;
        this.minstress = minstress;
        this.maxstress = maxstress;
        this.color = color;
        this.numberOfPeople = numberOfPeople;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMinspeed() {
        return minspeed;
    }

    public void setMinspeed(double minspeed) {
        this.minspeed = minspeed;
    }

    public double getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(double maxspeed) {
        this.maxspeed = maxspeed;
    }
    
    public void setMinStress(double minStress){
        this.minstress = minStress;
    }
    
    public double getMinStress(){
        return this.minstress;
    }
    
    public void setMaxStress(double maxStress){
        this.maxstress = maxStress;
    }
    
    public double getMaxStress(){
        return this.maxstress;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
    
    
}