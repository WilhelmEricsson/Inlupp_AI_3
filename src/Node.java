/**
 *
 * Wilhelm Ericsson
 * Ruben Wilhelmsen
 *
 */
import processing.core.PVector;
public class Node {
    //Dessa två statiskavariabler används för att avgöra vilken färg som noden skall ritas i.
    public static double average;
    //Index: 0-2 innebär röd färg, 3-5 innebär grön
    //0: 0.25*average, 1: 0.5*average, 2: 0.75*average, 3: 1.25*average, 4: 1.5*average, 5: 1.75*average
    public static double[] threshold;

    // A node object knows about its location in the grid
    // as well as its size with the variables x,y,w,h
    private float x,y;   // x,y location
    private float w,h;   // width and height
    private float angle; // angle for oscillating brightness
    private PVector position;
    private int qColor;
    private double qValue;
    private boolean isGreen;
    private int col;
    private int row;

    private Node[] adjacentNodes;
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
        this.adjacentNodes = new Node[4];
        this.isEmpty = true;
        if(threshold == null){
            threshold = new double[6];
        }
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

    //Anävnds för att hämta intilliggande noder i väderstecken - N,Ö,S och V. I vissa fall finns det ingen nod i det givna väderstecket, detta
    // sker till exempel med nod 0,0 som befinner sig uppe i vänstra hörnet vilket gör att N och V är null värden.
    public void determineAdjacentNodes(Grid grid){
        //NORTH
        adjacentNodes[0] = grid.getNodeByCoord(row-1,col);
        //EAST
        adjacentNodes[1] = grid.getNodeByCoord(row,col+1);
        //SOUTH
        adjacentNodes[2] = grid.getNodeByCoord(row+1,col);
        //WEST
        adjacentNodes[3] = grid.getNodeByCoord(row,col-1);
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
    public Node getAdjacentNode(int action){
        return adjacentNodes[action];
    }
    public int getQColor(){
        return qColor;
    }
    //Denna boolean används vid utritandet av noden i Grid-klassen.
    public boolean isGreen(){
        return isGreen;
    }
    public double getQValue(){
        return qValue;
    }

    //----------------------------------------------SETTERS------------------------------------------------------------

    public void setIsEmpty(boolean isEmpty){
        this.isEmpty = isEmpty;
    }



    //Användes för att ändra nodens färg i förhållande till agentens Q-värde kopplat till noden (I denna implementation så används det högsta q-värdet).
    public void setQColor(){
        if(qValue >= average){
            isGreen = true;
            if(qValue > threshold[3]){
                if(qValue > threshold[4]){
                    if(qValue > threshold[5]){
                        qColor = 255;
                    }else{
                        qColor = 192;
                    }
                }else{
                    qColor = 129;
                }
            }else{
                qColor = 66;
            }

        }else{
            isGreen = false;
            if(qValue < threshold[2]){
                if(qValue < threshold[1]){
                    if(qValue < threshold[0]){
                        qColor = 255;
                    }else{
                        qColor = 192;
                    }
                }else{
                    qColor = 129;

                }
            }else{
                qColor = 66;
            }
        }

    }

    //Sätter nodens Q-värde, denna metod kallas på från Agent-klassen och använder det högsta Q-vädert kopplat till tillståndet(nodenn). Anledningen att sätta
    // ett Q-värde i nod-klassen var för att kunna använda det för att ändra färg på noden.
    public void setQValue(double qValue){
        this.qValue = qValue;
    }




}