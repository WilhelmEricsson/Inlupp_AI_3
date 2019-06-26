import processing.core.*;

import java.util.Arrays;
import java.util.Random;

public class Agent extends Sprite{
    //N = 0, E = 1, S = 2, W = 3
    private static final int NUM_OF_ACTIONS = 4;
    private static Random rnd;
    private LabyrinthLearn mainProg;
    private Node current, previous, goal;
    private String agentActivity;
    private boolean isTraining;
    //Reinforcement vars.
    private double[][]  qTable;
    private double[] stateValue;
    private final double ALPHA, GAMMA;
    private double epsilon;
    private int trails, numOfBestAction, nextAction, learningEpisodes;
    //-----------------------------CONSTRUCTORS------------------------------------------
    public Agent(PApplet mainProg, Node current, Node goal, PVector position, String name, float diameter, float radius, double ALPHA, double GAMMA, int learningEpisodes){
        super(position, name, diameter, radius);
        this.mainProg = (LabyrinthLearn) mainProg;
        this.current = current;
        this.goal = goal;
        this.ALPHA = ALPHA;
        this.GAMMA = GAMMA;
        this.epsilon = 0.1;
        this.learningEpisodes = learningEpisodes;
        trails = numOfBestAction = 0;
        if(rnd == null){
            rnd = new Random();
        }
        initializeQTable();
        this.agentActivity = "Training";
        this.isTraining = true;

    }
    //***********************************************************************************

    public void update(){
        if(current.getId() != goal.getId()) {
            move();
            position = current.getPosition();
        }else{
            System.out.println("Restarting... ");
            if(learningEpisodes > 0){
                learningEpisodes--;
            }
            current = mainProg.getGrid().getNodeByCoord(0,8);
        }
        drawAgent();

    }

    private void drawAgent(){
        mainProg.pushStyle();
        mainProg.fill(255,0,0);
        mainProg.ellipse(position.x, position.y, diameter, diameter);
        mainProg.fill(0,0,255);
        mainProg.ellipse(position.x, position.y, radius,radius);
        mainProg.fill(0,255,0);
        setAgentDirection();
        mainProg.popStyle();
    }
    private void setAgentDirection(){
        switch (nextAction){
            case 0: // NORTH
                mainProg.ellipse(position.x, position.y-radius, radius/2,radius/2);
                break;
            case 1: // EAST
                mainProg.ellipse(position.x+radius, position.y, radius/2,radius/2);
                break;
            case 2: // SOUTH
                mainProg.ellipse(position.x, position.y+radius, radius/2,radius/2);
                break;
            case 3: // WEST
                mainProg.ellipse(position.x-radius, position.y, radius/2,radius/2);
                break;
        }
    }


    //parametrarna är ev. onödiga -> nyttja previous node - action kanske är nödvändig beroende på;
    // SE ÖVER
    public void updatePreviousStateQTableValue(int action){
        int prevState = previous.getId();

        if(current != null){
            int newState = current.getId();
            qTable[prevState][action] = qTable[prevState][action] + ALPHA*((current.getReward() + GAMMA*maxQ(newState)) - qTable[prevState][action]);
           // System.out.println("CHANGE: STATE - " + prevState + " Action - " + action + " values -> " + qTable[prevState][action] + "\t E: " + epsilon);
            if(!current.empty()){
                current = previous;
            }
        }else{
            double reward = -1;
            qTable[prevState][action] = qTable[prevState][action] + ALPHA*((reward) - qTable[prevState][action]);

            //System.out.println("CHANGE: STATE - " + prevState + " Action - " + action + " values -> " + qTable[prevState][action] + "\t|WALKED INTO A WALL| -- E: " + epsilon);
            current = previous;
        }

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

    //Används för att fatta belust om action vid Node current
    private int maxQAction(){
        int action = 0;
        int state = current.getId();
        for(int i = (action + 1); i < NUM_OF_ACTIONS; i++ ){
            if(qTable[state][action] < qTable[state][i]){
                action = i;
            }
        }
        return action;
    }


   private void initializeQTable(){
        int numOfStates = mainProg.getGrid().getRows()*mainProg.getGrid().getCols();
        qTable = new double[numOfStates][NUM_OF_ACTIONS];
        for(int state = 0; state < numOfStates; state++){
            for(int action = 0; action < NUM_OF_ACTIONS; action++){
                qTable[state][action] = rnd.nextDouble();
            }
            System.out.println(state + " :" + Arrays.toString(qTable[state]));
        }
    }

    private int greedyEpsilonPolicy(){
        int action = 0;
        double randomProb = rnd.nextDouble() + 0.2;

        if(randomProb <= epsilon){
            action = rnd.nextInt(4);
        }else{
            action = maxQAction();
            numOfBestAction++;
        }
        trails++;

        epsilon = numOfBestAction/(double)trails;

        return action;
    }

    public void move(){
        if(learningEpisodes > 0){

            nextAction = greedyEpsilonPolicy();

        }else{
            if(isTraining){
                agentActivity = "Testing";
                isTraining = false;
                System.out.println("Starting Test process");
            }
            nextAction = maxQAction();
        }
        previous = current;
        current = current.getAdjacentNode(nextAction);
        if(isTraining){
            updatePreviousStateQTableValue(nextAction);
        }
    }


    public int getLearningEpisodes(){
        return learningEpisodes;
    }
    public String getAgentActivity(){
        return agentActivity;
    }


}
