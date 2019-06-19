import processing.core.*;
public class LabyrinthLearn extends PApplet{
    private  Grid grid;
    private PImage img;

    //-------------------------------------MAIN-------------------------------------------------------


        public static void main(String[] args){
           PApplet.main("LabyrinthLearn");
        }

    //***********************************************************************************************
    @Override
    public void settings() {
        size(400, 400);
    }


    @Override
    public void setup() {
        grid = new Grid(this,19, 19, 20);
        img = loadImage("labyrint1.png");
    }

    @Override
    public void draw() {

        img.resize(404, 404);
        imageMode(CENTER);
        image(img, width/2, height/2);

        grid.display();
    }
}
