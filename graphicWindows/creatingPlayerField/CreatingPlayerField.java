package graphicWindows.creatingPlayerField;



import errorMessages.ErrorMessages;
import gameBoardGraphics.Board;
import gameBoardGraphics.Cell;
import graphicWindows.gameList.GameList;
import graphicWindows.gameWindowOnline.GameWindowOnline;
import graphicWindows.waitingOpponent.WaitingOpponent;
import javafx.application.Platform;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import network.Client;

import java.io.IOException;

public class CreatingPlayerField {

    /**
     * содание доски для игры
     */
    private Board board;

    /**
     * параметнры окна
     */
    private Node source;

    /**
     * чья это доска игрока или опонента
     */
    private boolean isItPlayer;

    /**
     * имя опонента
     */
    private String opponentName;

    private Stage stage;

    public CreatingPlayerField(Node source, boolean isItPlayer){
        this.source = source;
        this.isItPlayer = isItPlayer;
        this.opponentName = null;
    }
    public CreatingPlayerField(Node source, boolean isItPlayer, String opponentName){
        this.source = source;
        this.isItPlayer = isItPlayer;
        this.opponentName = opponentName;
    }

    public void showCreatingPlayingField(boolean autoCreateShip){
        stage = (Stage) this.source.getScene().getWindow();
        Parent root = this.createShowCreatingPlayingField(autoCreateShip);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Client.commandExit();
            Platform.exit();
        });
        stage.show();
    }
    private BorderPane createShowCreatingPlayingField(boolean autoCreateShip){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800, 400);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        hBox.getChildren().add(this.setBoard(autoCreateShip));

        VBox vBoxButton = new VBox();
        vBoxButton.setAlignment(Pos.CENTER);

        vBoxButton.getChildren().add(buttonReturnSettingGame());

        vBoxButton.getChildren().add(buttonAutoCreate());

        vBoxButton.getChildren().add(buttonDelete());

        vBoxButton.getChildren().add(buttonOk());

        hBox.getChildren().add(vBoxButton);
        borderPane.setCenter(hBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        return borderPane;
    }

    private VBox vBoxShipExample(boolean first){
        int one = 0; int two = 0; int three = 0; int four = 0;
        if(!first){
            one = this.board.sizeSomeShip(1); two = this.board.sizeSomeShip(2); three = this.board.sizeSomeShip(3); four = this.board.sizeSomeShip(4);
        }
        VBox vBox = new VBox();
        vBox.getChildren().add(shipExamples(1, one));
        vBox.getChildren().add(shipExamples(2, two));
        vBox.getChildren().add(shipExamples(3, three));
        vBox.getChildren().add(shipExamples(4, four));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        return vBox;
    }

    private HBox shipExamples(int numberOfDecks, int quantity){
        HBox hBox = new HBox();
        for(int i = 0; i < numberOfDecks; i++){
            Cell cell = new Cell(0, 0);
            cell.setShip();
            hBox.getChildren().add(cell);
        }
        int quantityShips = 5 - quantity - numberOfDecks;
        if(quantityShips < 0){
            quantityShips = 0;
        }
        Label lb = new Label("   x" + Integer.toString(quantityShips));
        lb.setTextFill(Color.BLUE);
        lb.setFont(new Font("Arial", 14));
        hBox.getChildren().add(lb);
        return hBox;
    }


    private Button buttonReturnSettingGame(){
        Button returnSettingGame = new Button("<");
        returnSettingGame.setPrefSize(50, 50);
        returnSettingGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                GameList gameList = new GameList((Node)mouseEvent.getSource());
                gameList.showGameList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return returnSettingGame;
    }
    private Button buttonAutoCreate(){
        Button buttonAutoCeate = new Button("Auto");
        buttonAutoCeate.setPrefSize(50, 50);
        buttonAutoCeate.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            CreatingPlayerField creatingPlayerField = new CreatingPlayerField((Node) mouseEvent.getSource(), isItPlayer, opponentName);
            creatingPlayerField.showCreatingPlayingField(true);
        });
        return buttonAutoCeate;
    }
    private Button buttonDelete(){
        Button buttonDelete = new Button("del");
        buttonDelete.setPrefSize(50, 50);
        buttonDelete.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            CreatingPlayerField creatingPlayerField = new CreatingPlayerField((Node) mouseEvent.getSource(), isItPlayer, opponentName);
            creatingPlayerField.showCreatingPlayingField(false);
        });
        return buttonDelete;
    }
    private Button buttonOk(){
        Button buttonOk = new Button("Ok");
        buttonOk.setPrefSize(50, 50);
        buttonOk.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            this.board.findShip();
            if(this.board.correctlyCollectedField()){
                if(isItPlayer){
                    Client.commandNewGame();
                    WaitingOpponent waitingOpponent = null;
                    try {
                        waitingOpponent = new WaitingOpponent((Node) mouseEvent.getSource(), this.board);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    waitingOpponent.showWaitingForAnOpponent();
                    //переходна страницу ожидания
                }
                if(!isItPlayer){
                    Client.commandStartGame(opponentName);
                    Client.commandMyName();
                    String playerName = null;
                    try {
                        playerName = Client.readMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //отправка имени опоненту
                    //перход на сттраницу игры
                    GameWindowOnline gameWindowOnline = new GameWindowOnline(stage, playerName, opponentName, board, false);
                    gameWindowOnline.showGameWindowOnline();
                }
            }
            if(!this.board.correctlyCollectedField()){
                ErrorMessages errorMessages = new ErrorMessages();
                errorMessages.showWindowErrorCreateBoard("Error create board");
            }
        });
        return buttonOk;
    }
    private HBox setBoard(boolean autoCreateShip){
        HBox hBox = new HBox(20);
        hBox.getChildren().add(0, vBoxShipExample(true));
        this.board = new Board(autoCreateShip, mouseEvent -> {
            Cell cell = (Cell) mouseEvent.getSource();
            this.board.clickOnTheCellInCreateMode(cell);
            this.board.findShip();
            this.board.printField();
            hBox.getChildren().set(0, vBoxShipExample(false));
        });
        hBox.getChildren().add( this.board.getBoard());
        return hBox;
    }

}