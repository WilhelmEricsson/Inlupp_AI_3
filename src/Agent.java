import processing.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Agent extends Sprite {
    //N = 0, E = 1, S = 2, W = 3
    private static final int NUM_OF_ACTIONS = 4;
    private static Random rnd;
    private LabyrinthLearn mainProg;
    private Node current, previous, goal;
    private String agentActivity;
    private boolean isTraining;
    //Reinforcement vars.
    private double[][] qTable;
    private final double ALPHA, GAMMA;
    private double epsilon;
    private int trails, numOfBestAction, nextAction, learningEpisodes;

    //-----------------------------CONSTRUCTORS------------------------------------------
    public Agent(PApplet mainProg, Node current, Node goal, PVector position, String name, float diameter, float radius, double ALPHA, double GAMMA, int learningEpisodes, double[][] qTable) {
        super(position, name, diameter, radius);
        this.mainProg = (LabyrinthLearn) mainProg;
        this.current = current;
        this.goal = goal;
        this.ALPHA = ALPHA;
        this.GAMMA = GAMMA;
        this.epsilon = 0.1;
        this.learningEpisodes = learningEpisodes;
        trails = numOfBestAction = 0;
        if (rnd == null) {
            rnd = new Random();
        }
        initializeQTable(qTable);
        this.agentActivity = "Training";
        this.isTraining = true;

    }
    //***********************************************************************************

    // Flyttar agenten ifall den inte är vid mål
    // Ifall agenten hitta till mål och håller på att tränas så startas den om på en ny learningEpisode
    // Färgerna på noderna uppdateras efter varje episod
    // Sist ritas agenten ut
    public void update() {
        if (current.getId() != goal.getId()) {
            move();
            position = current.getPosition();
        } else {
            System.out.println("Restarting... ");
            if (learningEpisodes > 0 && isTraining) {
                System.out.println("Q_Value Average: " + Node.average);
                mainProg.getGrid().setNodeColors();
                printQTableToConsole();
                learningEpisodes--;
            }
            current = mainProg.getGrid().getStartNode();
        }
        drawAgent();

    }

    private void drawAgent() {
        mainProg.pushStyle();
        mainProg.fill(255, 0, 0);
        mainProg.ellipse(position.x, position.y, diameter, diameter);
        mainProg.fill(0, 0, 255);
        mainProg.ellipse(position.x, position.y, radius, radius);
        mainProg.fill(0, 255, 0);
        setAgentDirection();
        mainProg.popStyle();
    }

    private void setAgentDirection() {
        switch (nextAction) {
            case 0: // NORTH
                mainProg.ellipse(position.x, position.y - radius, radius / 2, radius / 2);
                break;
            case 1: // EAST
                mainProg.ellipse(position.x + radius, position.y, radius / 2, radius / 2);
                break;
            case 2: // SOUTH
                mainProg.ellipse(position.x, position.y + radius, radius / 2, radius / 2);
                break;
            case 3: // WEST
                mainProg.ellipse(position.x - radius, position.y, radius / 2, radius / 2);
                break;
        }
    }

    // Uppdaterar q-tabellen när en action har tagits
    // Specialfall ifall agenten försöker ta sig ur grid:en, t.ex. flytta norr i nod 0,0
    public void updatePreviousStateQTableValue(int action) {
        int prevState = previous.getId();

        if (current != null) {
            int newState = current.getId();
            qTable[prevState][action] = qTable[prevState][action] + ALPHA * ((current.getReward() + GAMMA * maxQ(newState)) - qTable[prevState][action]);
            // System.out.println("CHANGE: STATE - " + prevState + " Action - " + action + " values -> " + qTable[prevState][action] + "\t E: " + epsilon);
            if (!current.empty()) {
                current = previous;
            }
        } else {
            double reward = -1;
            qTable[prevState][action] = qTable[prevState][action] + ALPHA * ((reward) - qTable[prevState][action]);

            //System.out.println("CHANGE: STATE - " + prevState + " Action - " + action + " values -> " + qTable[prevState][action] + "\t|WALKED INTO A WALL| -- E: " + epsilon);
            current = previous;
        }

    }

    // Nyttjas för att hämta högst q-värde givet en state
    // Räknar ut den handling med högst q-värde i givet state och returnerar sedan q-värdet
    private double maxQ(int state) {
        int action = 0;
        for (int i = (action + 1); i < NUM_OF_ACTIONS; i++) {
            if (qTable[state][action] < qTable[state][i]) {
                action = i;
            }
        }
        return qTable[state][action];
    }

    // Används för att fatta belust om action vid Node current
    // Returnerar den action i current vilket har högst q-värde
    private int maxQAction() {
        int action = 0;
        int state = current.getId();
        for (int i = (action + 1); i < NUM_OF_ACTIONS; i++) {
            if (qTable[state][action] < qTable[state][i]) {
                action = i;
            }
        }
        return action;
    }

    // Initialiserar q-tabellen
    // Ifall agenten inte använder sig av en medföljd q-tabell skapas en ny vilket tilldelas slumpmässiga värden
    private void initializeQTable(double[][] qTable) {
        if (qTable == null) {
            int numOfStates = mainProg.getGrid().getRows() * mainProg.getGrid().getCols();
            this.qTable = new double[numOfStates][NUM_OF_ACTIONS];
            for (int state = 0; state < numOfStates; state++) {
                for (int action = 0; action < NUM_OF_ACTIONS; action++) {
                    this.qTable[state][action] = rnd.nextDouble();
                }
                Node temp = mainProg.getGrid().getNodeByID(state);
                temp.setQValue(maxQ(temp.getId()));
                System.out.println(state + " :" + Arrays.toString(this.qTable[state]));
            }
        } else {
            preparePretrainedAgent(qTable);
        }
        mainProg.getGrid().setNodeColors();

    }

    // Laddar in en redan skapad q-tabell
    // Ifall en q-tabell finns som parameter i agent-konstruktorn kommer denna metod att anropas av initializeQTable
    private void preparePretrainedAgent(double[][] qTable) {
        System.out.println("qTable loaded from file.");
        learningEpisodes = 0;
        this.qTable = qTable;
        for (int state = 0; state < this.qTable.length; state++) {
            Node temp = mainProg.getGrid().getNodeByID(state);
            temp.setQValue(maxQ(temp.getId()));
        }


    }

    // Denna metod bestämmer ifall agenten ska utforska eller utnyttja (explore vs exploit)
    // Agenten tar antingen den action med högst q-värde eller väljer en slumpmässig action
    // Valet mellan dessa två bestäms av slump men slumpen manipuleras delvis av epsilon-värdet
    // Till en början agerar agenten mest slumpmässigt (utforskar) eftersom den inte vet någonting
    // Ju mer tanken har tränat och löst labyrinten desto mer agerar den enligt q-värdena (utnyttjar)
    // eftersom att följa dessa bör ta den till målet när den är tränad
    private int greedyEpsilonPolicy() {
        int action;
        double randomProb = rnd.nextDouble() + 0.2;

        if (randomProb <= epsilon) {
            action = rnd.nextInt(4);
        } else {
            action = maxQAction();
            numOfBestAction++;
        }
        trails++;

        epsilon = numOfBestAction / (double) trails;

        return action;
    }

    // Ifall agenten tränar bestäms dess nästa action genom greedyEpsilonPolicy
    // Ifall tränings-episoderna har utförts så sätts agenten till att istället testa q-tabellen som skapats,
    // då bestäms nästa action endast av det högsta q-värdet i tabellen
    // Ifall agenten tränar så uppdateras även q-tabellen efter varje action
    public void move() {
        if (learningEpisodes > 0 && isTraining) {
            nextAction = greedyEpsilonPolicy();
        } else {
            if (isTraining) {
                mainProg.setFrameRate(10);
                agentActivity = "Testing";
                isTraining = false;
                System.out.println("Starting Test process");
            }
            nextAction = maxQAction();
        }
        previous = current;
        current = current.getAdjacentNode(nextAction);

        if (isTraining) {
            updatePreviousStateQTableValue(nextAction);
            previous.setQValue(maxQ(previous.getId()));
        }
    }

    private double calcAvgQValue(Node node) {
        double average = 0.0;
        for (int i = 0; i < NUM_OF_ACTIONS; i++) {
            average += qTable[node.getId()][i];
        }
        return average / NUM_OF_ACTIONS;
    }

    public void drawNodeInfo() {
        PVector pvec = new PVector(mainProg.mouseX, mainProg.mouseY);
        Node n = mainProg.getGrid().getNearestNode(pvec);

        PVector distanceVect = PVector.sub(pvec, n.getPosition());
        float distanceVectMag = distanceVect.mag();
        if (distanceVectMag < 10) {
            mainProg.fill(255, 255, 0);
            mainProg.ellipse(n.getPosition().x, n.getPosition().y, 5, 5);
            mainProg.fill(0);
            mainProg.rect(0, 0, 185, 15);
            mainProg.fill(255);
            mainProg.text("maxQ: " + maxQ(n.getId()), 2, 12);
        }
    }

    public int getLearningEpisodes() {
        return learningEpisodes;
    }

    public String getAgentActivity() {
        return agentActivity;
    }

    public double[][] getQTable() {
        return qTable.clone();
    }

    public void printQTableToConsole() {
        for (int state = 0; state < qTable.length; state++) {
            System.out.println(state + " :" + Arrays.toString(this.qTable[state]));
        }
    }

    public void setAgentActivity(boolean isTraining) {
        if (isTraining != this.isTraining) {
            if (!isTraining) {
                mainProg.setFrameRate(10);
                agentActivity = "Testing";
            } else {
                mainProg.setFrameRate(500);
                if (learningEpisodes <= 0) {
                    learningEpisodes = 100;
                }
                agentActivity = "Training";
            }
            this.isTraining = isTraining;
            current = mainProg.getGrid().getStartNode();
        }
    }

}
