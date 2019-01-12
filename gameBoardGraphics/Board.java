package gameBoardGraphics;

import gameLogic.GameField;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Board  extends GameField {

    /**
     * получившаяся доска
     */
    private VBox vBoxBoard;

    /**
     * @return возвращает нарисованое игровое поле
     */
    public VBox getBoard(){return this.vBoxBoard;}

    /**
     * конструктор доски с нажатием
     * @param handler нажатие на клетку
     */
    public Board(boolean autoCreate, EventHandler<? super MouseEvent> handler){
        super();
        VBox YBox = new VBox();
        if(autoCreate){
            super.autoCreateShip();
        }
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(super.isItShip(x, y)){
                    cell.setShip();
                }
                cell.setOnMouseClicked(handler);
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);

    }
    //тут все ок

    public Board(Board board, EventHandler<? super MouseEvent> handler){
        super(board.returnSuper());
        VBox YBox = new VBox();
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(super.getFromPlayerField(x, y) == 0){
                    cell.setOnMouseClicked(handler);
                }
                if(super.getFromPlayerField(x, y) < 0){
                    cell.tryShot();
                }
                if(super.getFromPlayerField(x, y) == 10){
                    cell.missed();
                }

                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);

    }

    public Board(Board board){
        super(board.returnSuper());
        VBox YBox = new VBox();
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(super.isItShip(x, y)){
                    cell.setShip();
                }
                if(super.getFromPlayerField(x, y) < 0){
                    cell.tryShot();
                }
                if(super.getFromPlayerField(x, y) == 10){
                    cell.missed();
                }
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);

    }

    public GameField returnSuper(){
        return this;
    }

    /**
     * конструктор без нажатия
     */
    public Board(){
        super();
        VBox YBox = new VBox();
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);
    }


    /**
     * метоод который позволяет ставить или снимать кораблик
     * @param cell клетка на которую тыкнули
     */
    public void clickOnTheCellInCreateMode(Cell cell){
        if(super.canSetShip(cell.coordX(), cell.coordY())){
            super.setShip(cell.coordX(), cell.coordY());
            cell.setShip();
        } else {
            super.removeShip(cell.coordX(), cell.coordY());
            cell.removeShip();
        }

    }



    /*















    private VBox vBoxBoard;
    private GameField gameField = new GameField();



    public GameField getGameField(){return this.gameField;}
    public void resetShip(){this.gameField.resetShip();}
    public boolean correctlyCollectedField(){return this.gameField.correctlyCollectedField();}
    public int sizeSomeShip(int i){return this.gameField.sizeSomeShip(i);}
    public VBox getVBoxBoard(){return this.vBoxBoard;}
    public void shot(int x, int y){
        if(this.gameField.getFromPlayerField(x, y) > 0){
            if(this.gameField.getFromPlayerField(x, y) == 1){this.gameField.setInPlayerField(x, y, -1);}
            if(this.gameField.getFromPlayerField(x, y) == 2){this.gameField.setInPlayerField(x, y, -2);}
            if(this.gameField.getFromPlayerField(x, y) == 3){this.gameField.setInPlayerField(x, y, -3);}
            if(this.gameField.getFromPlayerField(x, y) == 4){this.gameField.setInPlayerField(x, y, -4);}
        }
        if(this.gameField.getFromPlayerField(x, y) == 0){
            this.gameField.setInPlayerField(x, y, 10);
        }
    }
    public void clickOnTheCellInCreateMode(Cell cell){
        if(this.gameField.canSetShip(cell.coordX(), cell.coordY())){
            this.gameField.setShip(cell.coordX(), cell.coordY());
            cell.setShip();
        }
        else{
            this.gameField.removeShip(cell.coordX(), cell.coordY());
            cell.removeShip();
        }
    }
    public boolean didYouHitTheShip(int x, int y) {
        if(this.gameField.getFromPlayerField(x, y) > 0 && this.gameField.getFromPlayerField(x, y) < 5){
            return true;
        }
        if(this.gameField.getFromPlayerField(x, y) == 0){
            return false;
        }
        return false;
    }
    public void setInBoard(int x, int y, int value){
        this.gameField.setInPlayerField(x, y, value);
    }

    //создает пустое поле
    public Board(){
        VBox YBox = new VBox();
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);
    }
    //поле когда на него нельзя тыкать
    public Board(Board board, boolean view){
        this.gameField = board.getGameField();
        VBox YBox = new VBox();
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(view){
                    if(this.gameField.getFromPlayerField(x, y) > 0){
                        cell.setShip();
                    }
                    if(this.gameField.getFromPlayerField(x, y) < 0){
                        cell.tryShot();
                    }
                    if(this.gameField.getFromPlayerField(x, y) == 10){
                        cell.missed();
                    }
                }
                if(!view){
                    if(this.gameField.getFromPlayerField(x, y) < 0){
                        cell.tryShot();
                    }
                    if(this.gameField.getFromPlayerField(x, y) == 10){
                        cell.missed();
                    }
                }
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);
    }

    //создает поле на которое можно тыкать для создания поля
    public Board(boolean autoCreate, EventHandler<? super MouseEvent> handler){
        VBox YBox = new VBox();
        if(autoCreate){
            this.gameField.autoCreateShip();
        }
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(this.gameField.isItShip(x, y)){
                    cell.setShip();
                }
                cell.setOnMouseClicked(handler);
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);
    }
    public Board(boolean autoCreate){
        VBox YBox = new VBox();
        if(autoCreate){
            this.gameField.autoCreateShip();
        }
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(this.gameField.isItShip(x, y)){
                    cell.setShip();
                }
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);
    }
    //создает игровое поле на которое можно тыкнуть чтобы сходить
    public Board(Board board, EventHandler<? super MouseEvent> handler){
        VBox YBox = new VBox();
        this.gameField = board.getGameField();
        for(int y = 0; y < 10; y++){
            HBox hBox = new HBox();
            for(int x = 0; x < 10; x++){
                Cell cell = new Cell(x, y);
                if(this.gameField.getFromPlayerField(x, y) < 0){
                    cell.tryShot();
                }
                if(this.gameField.getFromPlayerField(x, y) == 10) {
                    cell.missed();
                }
                if(this.gameField.getFromPlayerField(x, y) == 0){
                    cell.setOnMouseClicked(handler);
                }
                hBox.getChildren().add(cell);
            }
            YBox.getChildren().add(hBox);
        }
        this.vBoxBoard = YBox;
        this.vBoxBoard.setAlignment(Pos.CENTER);
    }









    public void printBoard(){ this.gameField.printField(); }


    public boolean canShotShisCell(int x, int y) {
        if(this.gameField.getFromPlayerField(x, y) >= 0 && gameField.getFromPlayerField(x, y) <= 4){ return true; }
        return false;
    }
    */
}
