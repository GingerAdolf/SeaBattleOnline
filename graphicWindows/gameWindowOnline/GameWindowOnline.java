package graphicWindows.gameWindowOnline;

import gameBoardGraphics.Board;
import gameBoardGraphics.Cell;
import graphicWindows.gameMenu.GameMenu;
import graphicWindows.youWin.YouWin;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Client;

import java.io.IOException;

public class GameWindowOnline {
    private String playerName;
    private String opponentName;


    private Board playerBoard;
    private Board opponentBoard;

    private Stage stage;

    private boolean youFirst;

    private int numberShips = 0;
    private int numberShipsKill = 0;

    public GameWindowOnline(Stage stage, String playerName, String opponentName, Board playerBoard, boolean youFirst){
        this.stage = stage;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerBoard = playerBoard;
        this.opponentBoard = new Board();
        this.youFirst = youFirst;
    }
    private GameWindowOnline(Node source, String playerName, String opponentName, Board playerBoard, Board opponentBoard, boolean youFirst, int numberShip, int numberShipsKill){
        this.stage = (Stage) source.getScene().getWindow();
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.youFirst = youFirst;
        this.numberShips = numberShip;
        this.numberShipsKill = numberShipsKill;
    }

    private GameWindowOnline(Stage stage, String playerName, String opponentName, Board playerBoard, Board opponentBoard, boolean youFirst, int numberShip, int numberShipsKill){
        this.stage = stage;
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.playerBoard = playerBoard;
        this.opponentBoard = opponentBoard;
        this.youFirst = youFirst;
        this.numberShips = numberShip;
        this.numberShipsKill = numberShipsKill;
    }

    public void showGameWindowOnline() {
        Parent root = this.createGameWindowOnline();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //если нажал а закрытие окна сообщение  проиграл
        stage.setOnCloseRequest(event -> {
            Client.commandLoser(opponentName);
            Client.commandExit();
            Platform.exit();
        });
        stage.show();
    }
    private BorderPane createGameWindowOnline() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800, 400);
        VBox vBoxPlayer = new VBox();
        vBoxPlayer.getChildren().add(new Label(this.playerName + " " + youFirst));
        vBoxPlayer.getChildren().add(this.setPlayerBoard());
        VBox vBoxOponent = new VBox();
        vBoxOponent.getChildren().add(new Label(this.opponentName));
        vBoxOponent.getChildren().add(this.setOpponentBoard());
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        hBox.getChildren().add(vBoxPlayer);
        hBox.getChildren().add(vBoxOponent);
        hBox.getChildren().add(buttonReturnToMain());
        borderPane.setCenter(hBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.FLORALWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        return borderPane;
    }
    private Button buttonReturnToMain(){
        Button button = new Button("loser");
        button.setPrefSize(60, 60);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                Client.commandLoser(opponentName);
                    stage.close();
                    YouWin youWin = new YouWin();
                    youWin.showYouWin(false);
                    numberShipsKill = 20;
                    numberShips = 20;

            }
        });
        if(youFirst){
            button.setDisable(true);
        }
        return button;
    }
    private HBox setOpponentBoard() {
        HBox hBox = new HBox(20);
        if(this.youFirst){
            this.opponentBoard = new Board(this.opponentBoard, mouseEvent -> {
                Cell cell = (Cell) mouseEvent.getSource();
                Client.commandShot(opponentName, cell.coordX(), cell.coordY());
                try {
                    String str = Client.readMessage();
                    System.out.println(str);
                    if(str.equals("@yes")){
                        numberShips++;
                        if(numberShips == 20){
                            //отправим сообщение чтор он проиграл
                            YouWin youWin = new YouWin((Node) mouseEvent.getSource());
                            youWin.showYouWin(true);
                        } else {
                            opponentBoard.setShot(cell.coordX(), cell.coordY());
                            GameWindowOnline gameWindowOnline = new GameWindowOnline((Node) mouseEvent.getSource(), playerName, opponentName, playerBoard, opponentBoard, true, numberShips, numberShipsKill);
                            gameWindowOnline.showGameWindowOnline();
                        }
                    }
                    if(str.equals("@no")){
                        opponentBoard.setMis(cell.coordX(), cell.coordY());
                        GameWindowOnline gameWindowOnline = new GameWindowOnline((Node) mouseEvent.getSource(), playerName, opponentName, playerBoard, opponentBoard, false, numberShips, numberShipsKill);
                        gameWindowOnline.showGameWindowOnline();
                    }
                    if(str.equals("@youWin")){
                        YouWin youWin = new YouWin((Node) mouseEvent.getSource());
                        youWin.showYouWin(true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
        if(!this.youFirst){
            this.opponentBoard = new Board(this.opponentBoard);
        }

        hBox.getChildren().add(this.opponentBoard.getBoard());
        return hBox;
    }

    private HBox setPlayerBoard(){
        HBox hBox = new HBox(20);
        if(this.youFirst){
            this.playerBoard = new Board(playerBoard);
        }
        if(!this.youFirst){
            this.playerBoard = new Board(playerBoard);
            Task<Void> task = new Task<Void>() {
                protected Void call() {
                    String str = "";
                    try {
                        str = Client.readMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String[] strs = str.split( " ");
                    int x = Integer.parseInt(strs[0]);
                    int y = Integer.parseInt(strs[1]);
                    Timeline fiveSecondsWonder = new Timeline();
                    if(!playerBoard.didYouHitTheShip(x, y)){
                        playerBoard.setMis(x, y);
                        Client.commandNo(opponentName);
                        playerBoard.printField();
                        fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(10), event -> {
                            GameWindowOnline gameWindowOnline = new GameWindowOnline(stage, playerName, opponentName, playerBoard, opponentBoard,true, numberShips, numberShipsKill);
                            gameWindowOnline.showGameWindowOnline();
                        }));
                    }
                    if(playerBoard.didYouHitTheShip(x, y)){
                        playerBoard.setShot(x, y);
                        Client.commandYes(opponentName);
                        fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(10), event -> {
                            numberShipsKill++;
                            if(numberShipsKill == 20){
                               stage.close();
                               YouWin youWin = new YouWin();
                               youWin.showYouWin(false);

                            }else {
                                GameWindowOnline gameWindowOnline = new GameWindowOnline(stage, playerName, opponentName, playerBoard, opponentBoard, false, numberShips, numberShipsKill);
                                gameWindowOnline.showGameWindowOnline();
                            }
                        }));
                    }
                    fiveSecondsWonder.play();
                    return null;
                }
            };
            new Thread(task).start();
        }
        hBox.getChildren().add(this.playerBoard.getBoard());
        return hBox;
    }

}
