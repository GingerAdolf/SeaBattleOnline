package gameBoardGraphics;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    private int x;
    private int y;
    public Cell(int x, int y){
        super(25, 25);
        this.x = x;
        this.y = y;
        setFill(Color.BLUE);
        setStroke(Color.BLACK);
    }
    public void setShip(){
        setFill(Color.WHITE);
    }
    public void removeShip(){ setFill(Color.BLUE); }

    public int coordX(){return this.x;}
    public int coordY(){return this.y;}

    public void missed() { setFill(Color.RED); }
    public void tryShot(){
        setFill(Color.GREEN);
    }
}
