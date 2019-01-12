package gameLogic;

import java.util.ArrayList;

public class Ship {



    /**
     * класс координаты клетки корабля
     */
    private class Coordinate{
        private int x;
        private int y;
        Coordinate(int x, int y){this.x = x; this.y = y;}
    }

    /**
     * массив координаты одного корабля
     */
    private ArrayList<Coordinate> coordinates;

    /**
     * конструктор однопалубного корабля
     * @param x
     * @param y
     */
    public Ship(int x, int y) {
        this.coordinates = new ArrayList<>();
        this.coordinates.add(new Coordinate(x, y));
    }

    /**
     * конструктор корабля
     * @param x
     * @param y
     * @param size размер корабля
     * @param horizontal направление
     */
    public Ship(int x, int y, int size, boolean horizontal){
        this.coordinates = new ArrayList<>();
        if(horizontal){
            for(int i = 0; i < size; i++){
                this.coordinates.add(new Coordinate(x + i, y));
            }
        }
        if(!horizontal) {
            for(int i = 0; i < size; i++){
                this.coordinates.add(new Coordinate(x, y + i));
            }
        }
    }


}
