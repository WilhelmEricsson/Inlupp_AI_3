import processing.core.*;

import java.util.Arrays;
import java.util.Random;

public class Agent extends Sprite{
    private static final int NUM_OF_ACTIONS = 4;
    private static Random rnd;
    private LabyrinthLearn mainProg;
    private Node current, previous, goal;
    //Reinforcement vars.
    private double[][]  qTable;
    private double[] stateValue;
    private final double ALPHA, GAMMA, EPSILON;
    //-----------------------------CONSTRUCTORS------------------------------------------
    public Agent(PApplet mainProg, Node current, Node goal, PVector position, String name, float diameter, float radius, double ALPHA, double GAMMA){
        super(position, name, diameter, radius);
        this.mainProg = (LabyrinthLearn) mainProg;
        this.current = current;
        this.goal = goal;
        this.ALPHA = ALPHA;
        this.GAMMA = GAMMA;
        this.EPSILON = 0.1;
        if(rnd == null){
            rnd = new Random();
        }
        initializeQTable();

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
        mainProg.fill(0,255,0);
        mainProg.ellipse(position.x, position.y-radius, radius/2,radius/2);
        mainProg.popStyle();
    }


    //parametrarna är ev. onödiga -> nyttja previous node - action kanske är nödvändig beroende på;
    public void stateActionFunction(int state, int action){
        double reward = 0.0;
        int newState = 0; // skall vara current.id
        qTable[state][action] = qTable[state][action] + ALPHA*((reward + GAMMA*maxQ(newState)) - qTable[state][action]);
    }

    //nyttjas för att hämta handlingen med högst Q-värde
    private double maxQ(int state){
        int action = 0;
        for(int i = (action + 1); i < NUM_OF_ACTIONS; i++ ){
            if(qTable[state][action] < qTable[state][i]){
                action = i;
            }
        }
        return qTable[state][action];
    }


   private void initializeQTable(){
        int numOfStates = mainProg.getGrid().getRows()*mainProg.getGrid().getCols();
        qTable = new double[numOfStates][NUM_OF_ACTIONS];
        for(int state = 0; state < numOfStates; state++){
            for(int action = 0; action < NUM_OF_ACTIONS; action++){
                qTable[state][action] = rnd.nextDouble(); // fixa precision
            }
            System.out.println(Arrays.toString(qTable[state]));
        }
    }





}
