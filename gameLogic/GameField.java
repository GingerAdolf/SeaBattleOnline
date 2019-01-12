package gameLogic;

import java.util.ArrayList;
import java.util.Random;

public class GameField {
    /**
     * игровое поле
     */
    private int[][] field;

    /**
     * координаты кораблей
     */
    private ArrayList<Ship> oneDeckShip = new ArrayList<>();
    private ArrayList<Ship> twoDeckShip = new ArrayList<>();
    private ArrayList<Ship> threeDeckShip = new ArrayList<>();
    private ArrayList<Ship> fourDeckShip = new ArrayList<>();

    private Boolean wasThereAMistake = false;

    /**
     * создание игрового поля
     */
    public GameField() {
        this.field = new int[12][12];
        for (int i = 0; i < this.field.length; i++) {
            this.field[0][i] = -11;
            this.field[i][0] = -11;
            this.field[i][this.field.length - 1] = -11;
            this.field[this.field.length - 1][i] = -11;
        }
    }
    public GameField(int[][] field) { this.field = field; }

    public GameField(GameField gameField){
        this.field = gameField.field;
        this.oneDeckShip = gameField.oneDeckShip;
        this.twoDeckShip = gameField.twoDeckShip;
        this.threeDeckShip = gameField.threeDeckShip;
        this.fourDeckShip = gameField.fourDeckShip;
    }

    public int[][] getField(){return this.field;}
    public ArrayList<Ship> getOneDeckShip() {return this.oneDeckShip;}
    public ArrayList<Ship> getTwoDeckShip() {return this.twoDeckShip;}
    public ArrayList<Ship> getThreeDeckShip() {return  this.threeDeckShip;}
    public ArrayList<Ship> getFourDeckShip() {return this.fourDeckShip;}
    /**
     * @param x координата
     * @param y координата
     * @return проверка кораблик ли это
     */
    protected boolean isItShip(int x, int y) { return this.field[y + 1][x + 1] > 0 && this.field[y + 1][x + 1] <= 4; }

    /**
     * @param x координата
     * @param y координата
     * @return проверка можно лт поставить кораблик на это поле
     */
    protected boolean canSetShip(int x, int y) {
        if (this.field[y + 1][x + 1] != 0) {
            return false;
        }
        return this.field[y + 2][x + 2] <= 0 && this.field[y + 2][x] <= 0 && this.field[y][x] <= 0 && this.field[y][x + 2] <= 0;
    }

    /**
     * установка однопалубного корабля
     * @param x координата
     * @param y координата
     */
    protected void setShip(int x, int y) { this.field[y + 1][x + 1] = 1; }
    /**
     * установка выстрела
     * @param x координата
     * @param y координата
     */
    public void setShot(int x, int y) { this.field[y + 1][x + 1] = -10; }
    /**
     * установка промаха
     * @param x координата
     * @param y координата
     */
    public void setMis(int x, int y) { this.field[y + 1][x + 1] = 10; }
    /**
     * удаление клетки корабля
     * @param x координата
     * @param y координата
     */
    protected void removeShip(int x, int y) { this.field[y + 1][x + 1] = 0; }

    public boolean didYouHitTheShip(int x, int y){
        if( this.field[y + 1][x + 1] > 0 &&  this.field[y + 1][x + 1] < 5){
            return true;
        }
        return false;
    }

    /**
     * @param x координата
     * @param y координата
     * @return возвращает значение клетки корабля
     */
    public int getFromPlayerField(int x, int y){ return this.field[y+1][x+1]; }

    /**
     * поиск кораблей на доске
     */
    public void findShip() {
        this.resetShip();
        this.wasThereAMistake = false;
        //однопалубные корабли
        for (int y = 1; y < this.field.length - 1; y++) {
            for (int x = 1; x < this.field.length - 1; x++) {
                if (this.field[y][x] == 1 && this.field[y][x + 1] <= 0 && this.field[y][x - 1] <= 0 && this.field[y + 1][x] <= 0 && this.field[y - 1][x] <= 0) {
                    //это однопалубный
                    Ship ship = new Ship(x - 1, y - 1);
                    this.oneDeckShip.add(ship);
                }
            }
        }
        //горизонтальные корабли
        for (int y = 1; y < this.field.length - 1; y++) {
            for (int x = 1; x < this.field.length - 1; x++) {
                int size = 0;
                int X = 0;
                int Y = 0;
                if (this.field[y][x] > 0 && this.field[y][x + 1] > 0) {
                    X = x - 1;
                    Y = y - 1;
                    while (this.field[y][x + size] > 0) { size++; }
                    for(int i = 0; i < size; i++){this.field[y][x+i] = size;}
                }
                x = x + size;
                if (size > 4) {
                    this.wasThereAMistake = true;
                }
                Ship ship = new Ship(X, Y, size, true);
                if(size == 2){this.twoDeckShip.add(ship);}
                if(size == 3){this.threeDeckShip.add(ship);}
                if(size == 4){this.fourDeckShip.add(ship);}
            }
        }
        for (int x = 1; x < this.field.length - 1; x++) {
            for (int y = 1; y < this.field.length - 1; y++) {
                int size = 0;
                int X = 0;
                int Y = 0;
                if (this.field[y][x] > 0 && this.field[y + 1][x] > 0) {
                    X = x - 1;
                    Y = y - 1;
                    while (this.field[y + size][x] > 0) { size++; }
                    for(int i = 0; i < size; i++){this.field[y + i][x] = size;}
                }
                y = y + size;
                if (size > 4) {
                    this.wasThereAMistake = true;
                }
                Ship ship = new Ship(X, Y, size, false);
                if(size == 2){this.twoDeckShip.add(ship);}
                if(size == 3){this.threeDeckShip.add(ship);}
                if(size == 4){this.fourDeckShip.add(ship);}
            }
        }
    }

    /**
     * удаление координат всех кораблей
     * очистка доски
     */
    private void resetShip(){
        this.oneDeckShip.clear();
        this.twoDeckShip.clear();
        this.threeDeckShip.clear();
        this.fourDeckShip.clear();
    }


    /**
     * @return проверяет корабли на заполнение все ли корабли заполнены
     */
    public boolean correctlyCollectedField(){
        if(this.wasThereAMistake){ return false; }
        if(this.oneDeckShip.size() != 4) {return false;}
        if(this.twoDeckShip.size() != 3) {return false;}
        if(this.threeDeckShip.size() != 2) {return false;}
        return this.fourDeckShip.size() == 1;
    }

    /**
     * @param i размер корабля
     * @return возвращает количество кораблей которые еще можно поставить
     */
    public int sizeSomeShip(int i){
        if(i == 1){ return this.oneDeckShip.size(); }
        if(i == 2){ return this.twoDeckShip.size(); }
        if(i == 3){ return this.threeDeckShip.size();}
        if(i == 4){ return this.fourDeckShip.size();}
        return 0;
    }

    protected void autoCreateShip() {
        for (int k = 0; k < 4; k++){
            int j = 0;
            do {
                int sizeShip = 4 - k;
                int[][] coordinateShip = new int[2][sizeShip];
                Random random = new Random();
                boolean horizontal = random.nextBoolean();
                if (horizontal) {
                    int letter = 1 + random.nextInt(11);
                    int number = 1 + random.nextInt(11 - sizeShip);
                    for (int i = 0; i < sizeShip; i++) {
                        coordinateShip[0][i] = letter;
                        coordinateShip[1][i] = number + i;
                    }
                }
                if (!horizontal) {
                    int letter = 1 + random.nextInt(11 - sizeShip);
                    int number = 1 + random.nextInt(11);
                    for (int i = 0; i < sizeShip; i++) {
                        coordinateShip[0][i] = letter + i;
                        coordinateShip[1][i] = number;
                    }
                }
                if (this.posiblePutShip(coordinateShip)) {
                    this.setShipInPlayerField(coordinateShip);
                    j++;

                }
            } while (j != k + 1);
        }
    }
    private void setShipInPlayerField(int[][] coordShipXY){ for (int i = 0; i < coordShipXY[0].length; i++) { this.setShip(coordShipXY[0][i] - 1,coordShipXY[1][i] - 1); } }


    private boolean posiblePutShip(int[][] coordShipXY){
        for (int i = 0; i < coordShipXY[0].length; i++){
            if(this.field[coordShipXY[1][i]][coordShipXY[0][i]] != 0){ return false; }
            if(this.field[coordShipXY[1][i] + 1][coordShipXY[0][i]] > 0){ return false; }
            if(this.field[coordShipXY[1][i] - 1][coordShipXY[0][i]] > 0){ return false; }
            if(this.field[coordShipXY[1][i]][coordShipXY[0][i] + 1] > 0){ return false; }
            if(this.field[coordShipXY[1][i]][coordShipXY[0][i] - 1] > 0){ return false; }
            if(this.field[coordShipXY[1][i]+1][coordShipXY[0][i]+1] > 0){ return false; }
            if(this.field[coordShipXY[1][i]-1][coordShipXY[0][i]-1] > 0){ return false; }
            if(this.field[coordShipXY[1][i]+1][coordShipXY[0][i]-1] > 0){ return false; }
            if(this.field[coordShipXY[1][i]-1][coordShipXY[0][i]+1] > 0){ return false; }
        }
        return true;
    }


    public void printField() {
        for (int[] aField : this.field) {
            for (int x = 0; x < this.field.length; x++) {
                System.out.print(aField[x] + " ");
            }
            System.out.println();
        }
    }
}