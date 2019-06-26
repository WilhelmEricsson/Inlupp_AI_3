import processing.core.*;
import java.util.ArrayList;
public class Grid {
    private int cols, rows;
    private int grid_size;
    private Node[][] nodes;
    private Node startNode, goalNode;
    private PApplet mainProg;
    //***************************************************
    Grid(PApplet mainProg,int _cols, int _rows, int _grid_size) {
        this.mainProg = mainProg;
        cols = _cols;
        rows = _rows;
        grid_size = _grid_size;
        nodes = new Node[cols][rows];

        createGrgetId();
    }

    //***************************************************
    void createGrgetId() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                // Initialize each object
                nodes[i][j] = new Node(i, j, i*grid_size+grid_size, j*grid_size+grid_size);
            }
        }
        determineAdjacentNodes();
    }
    private void determineAdjacentNodes(){
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                nodes[i][j].determineAdjacentNodes(this);
            }
        }
    }

    public void printIds() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.println(nodes[i][j].getId());
            }
        }
    }

    public void setupNodes() {
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mainProg.get((int)nodes[j][i].getPosition().x, (int)nodes[j][i].getPosition().y) != -1) {
                    nodes[j][i].setId(counter);
                    nodes[j][i].setReward(-1);
                    nodes[j][i].setIsEmpty(false);
                } else {
                    nodes[j][i].setId(counter);
                    nodes[j][i].setReward(0);
                    if (i == rows - 1) {
                        if (mainProg.get((int) nodes[j][i].getPosition().x, (int) nodes[j][i].getPosition().y + grid_size - 1) == -1) {
                            nodes[j][i].setId(counter);
                            nodes[j][i].setReward(1);
                            goalNode = nodes[j][i];
                        }
                    }
                    if (i == 0) {
                        if (mainProg.get((int) nodes[j][i].getPosition().x, (int) nodes[j][i].getPosition().y - grid_size + 1) == -1) {
                            startNode = nodes[j][i];
                        }
                    }
                }
                counter++;
            }
        }
    }

    //***************************************************
    // ANVÄNDS INTE!
    void display1() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                // Initialize each object
                mainProg.line(j*grid_size+grid_size, 0, j*grid_size+grid_size, mainProg.height);
            }
            mainProg.line(0, i*grid_size+grid_size,mainProg.width, i*grid_size+grid_size);
        }
    }

    //***************************************************
    void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                // Initialize each object
                if (nodes[i][j].getReward() == -1) {
                    mainProg.stroke(255, 0, 0);
                } else if (nodes[i][j].getReward() == 1) {
                    mainProg.stroke(0, 255, 0);
                } else if (nodes[i][j].getReward() == 0) {
                    mainProg.stroke(0);
                }

                /*
                if (mainProg.get((int)nodes[i][j].getPosition().x, (int)nodes[i][j].getPosition().y) != -1) {
                    mainProg.stroke(255, 0, 0);
                } else {
                    mainProg.stroke(0);
                }
                */

                mainProg.ellipse(nodes[i][j].getPosition().x, nodes[i][j].getPosition().y, 5.0f, 5.0f);
                //println("nodes[i][j].position.x: " + nodes[i][j].position.x);
                //println(nodes[i][j]);
            }
            //line(0, i*grid_size+grid_size, width, i*grid_size+grid_size);
        }
    }

    //***************************************************
    // ANVÄNDS INTE!
    PVector getNearestNode1(PVector pvec) {
        //PVector pvec = new PVector(x,y);
        PVector vec = new PVector(0, 0);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes[i][j].getPosition().dist(pvec) < grid_size/2) {
                    vec.set(nodes[i][j].getPosition());
                }
            }
        }
        return vec;
    }

    //***************************************************
    Node getNearestNode(PVector pvec) {
        // En justering för extremvärden.
        float tempx = pvec.x;
        float tempy = pvec.y;
        if (pvec.x < 5) {
            tempx=5;
        } else if (pvec.x > mainProg.width-5) {
            tempx= mainProg.width-5;
        }
        if (pvec.y < 5) {
            tempy=5;
        } else if (pvec.y > mainProg.height-5) {
            tempy= mainProg.height-5;
        }

        pvec = new PVector(tempx, tempy);

        ArrayList<Node> nearestNodes = new ArrayList<Node>();

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes[i][j].getPosition().dist(pvec) < grid_size) {
                    nearestNodes.add(nodes[i][j]);
                }
            }
        }

        Node nearestNode = new Node(0, 0);
        for (int i = 0; i < nearestNodes.size(); i++) {
            if (nearestNodes.get(i).getPosition().dist(pvec) < nearestNode.getPosition().dist(pvec) ) {
                nearestNode = nearestNodes.get(i);
            }
        }

        return nearestNode;
    }

    // Node getNearestNodePosition(PVector pvec) {

    //  ArrayList<Node> nearestNodes = new ArrayList<Node>();

    //  for (int i = 0; i < cols; i++) {
    //    for (int j = 0; j < rows; j++) {
    //      if (nodes[i][j].position.dist(pvec) < grid_size) {
    //        nearestNodes.add(nodes[i][j]);
    //      }
    //    }
    //  }

    //  Node nearestNode = new Node(0,0);
    //  for (int i = 0; i < nearestNodes.size(); i++) {
    //    if (nearestNodes.get(i).position.dist(pvec) < nearestNode.position.dist(pvec) ) {
    //      nearestNode = nearestNodes.get(i);
    //    }
    //  }

    //  return nearestNode;
    //}

    //***************************************************
    PVector getNearestNodePosition(PVector pvec) {
        Node n = getNearestNode(pvec);

        return n.getPosition();
    }

    //***************************************************
    // ANVÄNDS INTE?
    void displayNearestNode(float x, float y) {
        PVector pvec = new PVector(x, y);
        displayNearestNode(pvec);
    }

    //***************************************************
    // ANVÄNDS INTE!
    void displayNearestNode1(PVector pvec) {
        //PVector pvec = new PVector(x,y);
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (nodes[i][j].getPosition().dist(pvec) < grid_size/2) {
                    PVector vec = nodes[i][j].getPosition();
                    mainProg.ellipse(vec.x, vec.y, 5, 5);
                }
            }
        }
    }

    //***************************************************
    void displayNearestNode(PVector pvec) {

        PVector vec = getNearestNodePosition(pvec);
        mainProg.ellipse(vec.x, vec.y, 5, 5);
    }

    //***************************************************
    PVector getRandomNodePosition() {
        int c = (int)mainProg.random(cols);
        int r = (int)mainProg.random(rows);

        PVector rn = nodes[c][r].getPosition();

        return rn;
    }

    //***************************************************
    public Node getRandomNode() {
        int c = (int)mainProg.random(cols);
        int r = (int)mainProg.random(rows);

        return nodes[c][r];
    }

    // Används troligen tillsammans med getNearestNode().empty
    // om tom så addContent(Sprite)
    //'''''''''''''''''''''''''''''''''''''''''''''''''''''''
    public Node getNodeByCoord(int row, int col){
        try{
            return nodes[col][row];
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }


    //*******************************************************
    public int getCols(){
        return cols;
    }
    public int getRows(){
        return rows;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

}