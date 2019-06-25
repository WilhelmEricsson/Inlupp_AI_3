import processing.core.PVector;
public class Node {
    // A node object knows about its location in the grid
    // as well as its size with the variables x,y,w,h
    private float x,y;   // x,y location
    private float w,h;   // width and height
    private float angle; // angle for oscillating brightness

    private PVector position;
    private int col;
    private int row;

    private int id;
    private int reward;

    private boolean isEmpty;

    //***************************************************
    // Node Constructor
    // Denna används för temporära jämförelser mellan Node objekt.
    public Node(float _posx, float _posy) {
        this.position = new PVector(_posx, _posy);
    }

    //***************************************************
    // Används vid skapande av grid
    public Node(int _id_col, int _id_row, float _posx, float _posy) {
        this.position = new PVector(_posx, _posy);
        this.col = _id_col;
        this.row = _id_row;

        this.isEmpty = true;
    }

    //***************************************************
   public Node(float tempX, float tempY, float tempW, float tempH, float tempAngle) {
        x = tempX;
        y = tempY;
        w = tempW;
        h = tempH;
        angle = tempAngle;
    }

    //***************************************************
    public boolean empty() {
        return this.isEmpty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    //-------------------------------------GETTERS--------------------------------------------
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public float getAngle() {
        return angle;
    }

    public PVector getPosition() {
        return position;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getId() {
        return id;
    }

    public int getReward() {
        return reward;
    }

}