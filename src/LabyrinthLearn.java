import processing.core.*;
public class LabyrinthLearn extends PApplet{
    private  Grid grid;
    private PImage img;
    private Agent agent;
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


        //Detta är tillfälligt vill bara rita ut agenten och se hur det såg ut
        Node agentStart = grid.getRandomNode();
        agent = new Agent(this, agentStart , grid.getRandomNode(),agentStart.getPosition(), "Q-Agent", 20, 10);
    }

    @Override
    public void draw() {

        img.resize(404, 404);
        imageMode(CENTER);
        image(img, width/2, height/2);
        grid.display();
        agent.update();



    }
}
