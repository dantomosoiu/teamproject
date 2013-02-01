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
    private float minspeed;
    private float maxspeed;
    private float minstress;
    private float maxstress;
    private ColorRGBA color;
    private int numberOfPeople;

    public PersonCategory(String name, float minspeed, float maxspeed, float minstress, float maxstress, ColorRGBA color, int numberOfPeople) {
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
