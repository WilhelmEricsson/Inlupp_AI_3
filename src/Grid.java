import processing.core.*;
import java.util.ArrayList;
public class Grid {
    private int cols, rows;
    private int grid_size;
    private Node[][] nodes;
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
                if (mainProg.get((int)nodes[i][j].getPosition().x, (int)nodes[i][j].getPosition().y) != -1) {
                    mainProg.stroke(255, 0, 0);
                } else {
                    mainProg.stroke(0);
                }

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
    // Används troligen tillsammans med getNearestNode().empty
    // om tom så addContent(Sprite)


}