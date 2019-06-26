import processing.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class LabyrinthLearn extends PApplet{
    private  Grid grid;
    private PImage img;
    private Agent agent;

    // Set this according to labyrinth size
    private final int GRID_SIZE = 30;
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
        img = loadImage("labyrint2.png");
        drawLabyrinth();
        grid = new Grid(this,GRID_SIZE-1, GRID_SIZE-1, GRID_SIZE);


        frameRate(500);
        //Detta är tillfälligt vill bara rita ut agenten och se hur det såg ut
        Node agentStart = grid.getNodeByCoord(0,8);
        double[][] qTable = readQTable(new File("resources/qTable.txt"));
        agent = new Agent(this, grid.getStartNode() , grid.getGoalNode(), grid.getStartNode().getPosition(), "Q-Agent", 20, 10, 0.5, 0.5, 100, null);
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
    }


    public Grid getGrid(){
            return grid;
    }

    @Override
    public void keyPressed() {
        if (key == 'p') {
            printQTable(agent.getQTable());
        }
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

    public void printQTable(double[][] qTable) {
        try {
            PrintWriter pw = new PrintWriter("resources/qTable.txt");
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

}
