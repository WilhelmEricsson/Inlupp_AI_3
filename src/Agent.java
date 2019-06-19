import processing.core.*;
public class Agent extends Sprite{
    private PApplet mainProg;
    private Node start, goal;
    //-----------------------------CONSTRUCTORS------------------------------------------
    public Agent(PApplet mainProg, Node start, Node goal, PVector position, String name, float diameter, float radius){
        super(position, name, diameter, radius);
        this.mainProg = mainProg;
        this.start = start;
        this.goal = goal;

    }
    //***********************************************************************************

    public void update(){
        drawAgent();

    }

    private void drawAgent(){
        mainProg.pushStyle();
        mainProg.fill(255,0,0);
        mainProg.ellipse(position.x, position.y, diameter, diameter);
        mainProg.fill(0,0,255);
        mainProg.ellipse(position.x, position.y, radius,radius);
        mainProg.popStyle();
    }

}
