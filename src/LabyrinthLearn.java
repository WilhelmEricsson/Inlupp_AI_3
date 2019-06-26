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

    public static int frameRate;

    // Set this according to labyrinth size
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
        //Detta är tillfälligt vill bara rita ut agenten och se hur det såg ut
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
        surface.setTitle("|Labyrinth Learn| ***" + agent.getAgentActivity() + "*** Episode: " + agent.getLearningEpisodes());
        drawLabyrinth();
        grid.display();
        agent.update();
        agent.drawNodeInfo();
    }

    public Grid getGrid(){
            return grid;
    }

    @Override
    public void keyPressed() {
            switch (key){
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
                case '+':
                    setFrameRate(this.frameRate+10);
                    break;
                case '-':
                    setFrameRate(this.frameRate-10);
                    break;
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
            restartAgent();

    }
    private void restartAgent(){
        System.out.println("***Total Reset***");
        setFrameRate(500);
        restartAgent(null);
    }
    private void restartAgent(double[][] qTable){
        System.out.println("Restarting");
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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void setFrameRate(int frameRate){
            this.frameRate = frameRate;
            if(this.frameRate <= 0) {
                System.err.println("Frame rate at its lowest!");
                this.frameRate = 50;
            }
            frameRate(this.frameRate);
    }
}
