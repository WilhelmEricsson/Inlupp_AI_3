import processing.core.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class LabyrinthLearn extends PApplet{
    private  Grid grid;
    private PImage img;
    private Agent agent;
    private boolean firstFrame;
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
        firstFrame = true;
        frameRate(500);
        //Detta är tillfälligt vill bara rita ut agenten och se hur det såg ut
        Node agentStart = grid.getNodeByCoord(0,8);
        double[][] qTable = readQTable(new File("qTable.txt"));
        agent = new Agent(this, agentStart , grid.getNodeByCoord(18,10),agentStart.getPosition(), "Q-Agent", 20, 10, 0.1, 0.5, 10, qTable);
    }

    private void drawLabyrinth() {
        img.resize(404, 404);
        imageMode(CENTER);
        image(img, width/2, height/2);
        if (firstFrame) {
            grid.setupNodes();
            firstFrame = false;
        }

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
            PrintWriter pw = new PrintWriter("qTable.txt");
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
