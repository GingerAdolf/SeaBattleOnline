package sample;


import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import graphicWindows.gameMenu.GameMenu;

import network.Client;

public class Main extends Application {
    public void start(Stage primaryStage){
        GameMenu gameMenu = new GameMenu();
        BorderPane root = gameMenu.createGameMenu();
        primaryStage.setTitle("SeaBattleOnline");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        Client client = new Client();
    }
    public static void main(String[] args) { launch(args); }
}
