/**
 *
 * Wilhelm Ericsson
 * Ruben Wilhelmsen
 *
 */
import processing.core.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Grid {
    private int cols, rows;
    private int grid_size;
    private Node[][] nodes;
    private HashMap<Integer, Node> nodesByID;
    private Node startNode, goalNode;
    private PApplet mainProg;
    //***************************************************
    Grid(PApplet mainProg,int _cols, int _rows, int _grid_size) {
        this.mainProg = mainProg;
        cols = _cols;
        rows = _rows;
        grid_size = _grid_size;
        nodes = new Node[cols][rows];
        nodesByID = new HashMap<>();
        createGrgetId();
        setupNodes();
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

    public void setupNodes() {
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mainProg.get((int)nodes[j][i].getPosition().x, (int)nodes[j][i].getPosition().y) != -1) {
                    nodes[j][i].setReward(-1);
                    nodes[j][i].setIsEmpty(false);
                } else {

                    nodes[j][i].setReward(0);
                    if (i == rows - 1) {
                        if (mainProg.get((int) nodes[j][i].getPosition().x, (int) nodes[j][i].getPosition().y + grid_size - 1) == -1) {
                            nodes[j][i].setReward(10);
                            goalNode = nodes[j][i];
                        }
                    }
                    if (i == 0) {
                        if (mainProg.get((int) nodes[j][i].getPosition().x, (int) nodes[j][i].getPosition().y - grid_size + 1) == -1) {
                            startNode = nodes[j][i];
                        }
                    }
                }
                nodes[j][i].setId(counter);
                nodesByID.put(counter,nodes[j][i]);
                counter++;
            }
        }
    }


    //***************************************************
    void display() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                // Initialize each object
                if (nodes[i][j].getReward() == -1) {
                    mainProg.stroke(255, 0, 0);
                } else if (nodes[i][j].getReward() == 10) {
                    mainProg.stroke(0, 255, 0);
                } else if (nodes[i][j].getReward() == 0) {
                    nodes[i][j].setQColor();
                    mainProg.stroke(0);
                    if(nodes[i][j].isGreen()){
                        mainProg.fill(0 , nodes[i][j].getQColor(),0);
                    }else{
                        mainProg.fill(nodes[i][j].getQColor(), 0 , 0);
                    }

                }

                mainProg.ellipse(nodes[i][j].getPosition().x, nodes[i][j].getPosition().y, 5.0f, 5.0f);
                mainProg.fill(255,255,255);
            }
        }
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


    public Node getNodeByID(int nodeId){
        return nodesByID.get(nodeId);
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

    //********************************************************

    public void setNodeColors(){
        Node.average = 0;
        int numOfExcludedNodes = 0;
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                if(nodes[col][row].getReward() != -1){
                    Node.average += nodes[col][row].getQValue();
                }else {
                    numOfExcludedNodes++;
                }
            }
        }
        Node.average = Node.average/(nodesByID.size()- numOfExcludedNodes);
        setNodeColorThresholds();
        for(int row = 0; row < rows; row++){
            for(int col = 0; col < cols; col++){
                nodes[col][row].setQColor();
            }
        }
    }
    private void setNodeColorThresholds(){
        Node.threshold[0] = 0.25*Node.average;
        Node.threshold[1] = 0.50*Node.average;
        Node.threshold[2] = 0.75*Node.average;
        Node.threshold[3] = 1.25*Node.average;
        Node.threshold[4] = 1.50*Node.average;
        Node.threshold[5] = 1.75*Node.average;
    }
    //********************************************************
}