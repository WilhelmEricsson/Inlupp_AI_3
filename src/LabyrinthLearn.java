/**
 *
 * Wilhelm Ericsson
 * Ruben Wilhelmsen
 *
 */
import processing.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class LabyrinthLearn extends PApplet{
    private  Grid grid;
    private PImage img;
    private Agent agent;
    private double learningRate = 0.9;
    private double discountFactor = 0.5;
    private int episodes = 100;
    private int currentMaze = 1;
    public static int frameRate;
    private boolean showDebugGUI = false;

    private String filePath = "resources/laby1_qTable.txt";
    private final int GRID_SIZE = 20;
    private final int WINDOW_SIZE = GRID_SIZE * GRID_SIZE;
    private final int WINDOW_SIZE_ZOOM = (int)Math.round(WINDOW_SIZE*1.01);

    //-------------------------------------MAIN-------------------------------------------------------


        public static void main(String[] args){
           PApplet.main("LabyrinthLearn");
        }

    //***********************************************************************************************
    @Override
    public void settings() {
        size(WINDOW_SIZE, WINDOW_SIZE);
    }


    @Override
    public void setup() {
        loadMap("labyrint1.png");
        drawLabyrinth();
        grid = new Grid(this,GRID_SIZE-1, GRID_SIZE-1, GRID_SIZE);
        setFrameRate(500);
        double[][] qTable = readQTable(new File(filePath));
        agent = new Agent(this, grid.getStartNode() , grid.getGoalNode(), grid.getStartNode().getPosition(), "Q-Agent", 20, 10, learningRate, discountFactor, episodes, qTable);
    }

    private void loadMap(String mapName){
        img = loadImage(mapName);
    }

    private void drawLabyrinth() {
        img.resize(WINDOW_SIZE_ZOOM, WINDOW_SIZE_ZOOM);
        imageMode(CENTER);
        image(img, width/2, height/2);

    }

    @Override
    public void draw() {
        surface.setTitle("|Labyrinth " + currentMaze + "| ***" + agent.getAgentActivity() + "*** Episode: " + agent.getLearningEpisodes());
        drawLabyrinth();
        if (showDebugGUI) {
            grid.display();
            agent.drawNodeInfo();
        }
        agent.update();
    }

    public Grid getGrid(){
            return grid;
    }


    /*
        Alla olika kommandom som kan användas i programmet.
     */
    @Override
    public void keyPressed() {
            switch (key){
                case 'd': // toggle grid
                    showDebugGUI = !showDebugGUI;
                    break;
                case 's':
                    saveQTable(agent.getQTable());
                    break;
                case 'r'://Reset
                    restartAgent();
                    break;
                case 'R': //Reset from file
                    restartAgent(readQTable(new File(filePath)));
                    break;
                case 't': //Testing
                    agent.setAgentActivity(false);
                    break;
                case 'T':  //Training
                    agent.setAgentActivity(true);
                    break;
                case '+': //increase frame rate
                    setFrameRate(this.frameRate+10);
                    break;
                case '-': //decrease frame rate
                    setFrameRate(this.frameRate-10);
                    break;

                    /*
                        map alternatives 1-3, changes the map and restarts the agent.
                     */
                case '1':
                    changeMap(1);
                    break;
                case '2':
                    changeMap(2);
                    break;
                case '3':
                    changeMap(3);
                    break;

            }

    }

    private void changeMap(int mapNum){
            loadMap("labyrint"+mapNum+".png");
            filePath = "resources/laby" + mapNum + "_qTable.txt";
            drawLabyrinth();
            grid = new Grid(this,GRID_SIZE-1, GRID_SIZE-1, GRID_SIZE);
            currentMaze = mapNum;
            restartAgent();

    }
    private void restartAgent(){
        System.out.println("***Total Reset***");
        setFrameRate(500);
        restartAgent(null);
    }
    private void restartAgent(double[][] qTable){
        System.out.println("Restarting");
        Node.threshold = new double[]{0.0,0.0,0.0,0.0,0.0,0.0};
        agent = new Agent(this, grid.getStartNode() , grid.getGoalNode(), grid.getStartNode().getPosition(), "Q-Agent", 20, 10, learningRate, discountFactor, episodes, qTable);

    }

    public double[][] readQTable(File path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            int lines = 0;

            while ((br.readLine()) != null) {
                lines++;
            }
            br.close();

            br = new BufferedReader(new FileReader(path));
            double[][] qTable = new double[lines][4];
            for (int i = 0; i < lines; i++) {
                line = br.readLine();
                String[] split = line.split(" ");
                for (int j = 0; j < 4; j++) {
                    qTable[i][j] = Double.parseDouble(split[j]);
                }
            }
            br.close();
            return qTable;
        } catch (Exception e) {
            return null;
        }
    }

    public void saveQTable(double[][] qTable) {
        try {
            PrintWriter pw = new PrintWriter(filePath);
            for (int i = 0; i < qTable.length; i++) {
                for (int j = 0; j < qTable[i].length; j++) {
                    if (j != qTable[i].length -1) {
                        pw.print(qTable[i][j] + " ");
                    } else {
                        pw.print(qTable[i][j]);
                    }
                }
                pw.println();
            }
            pw.close();
            System.out.println("Q Table saved to file: " + filePath);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void setFrameRate(int frameRate){
            this.frameRate = frameRate;
            if(this.frameRate <= 0) {
                System.err.println("Frame rate at its lowest!");
                this.frameRate = 10;
            }
            frameRate(this.frameRate);
    }
}
