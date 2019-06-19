/**
 *
 * Wilhelm Ericsson
 * Ruben Wilhelmsen
 *
 */

import processing.core.PVector;

public class Sprite {
    PVector position;
    String name;
    public float diameter, radius;

    //-----------------------CONSTRUCTORS---------------------
    public Sprite(PVector position, String name, float diameter, float radius){
        this.position = position;
        this.name = name;
        this.diameter = diameter;
        this.radius = radius;
    }
    public  Sprite(Sprite other){
         position = other.position;
         name = other.name;
         diameter = other.diameter;
         radius = other.radius;
    }


    //**''''''''''''''''''''''''''''''''''''''''''''''''''''''

    // **************************************************
    public String getName() {
        return this.name;
    }

    // **************************************************
    public float diameter() {
        return this.diameter;
    }

    // **************************************************
    public float getRadius() {
        return this.radius;
    }

    // **************************************************
    public PVector position() {
        return this.position;
    }

}