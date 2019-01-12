package graphicWindows.waitingOpponent;

import gameBoardGraphics.Board;
import graphicWindows.gameList.GameList;
import graphicWindows.gameWindowOnline.GameWindowOnline;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import network.Client;


import java.io.IOException;


public class WaitingOpponent {
    private Node source;
    private Board board;

    private String opponentName;
    private String playerName;

    private Stage stage;

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            return null;
        }
        public void run(){
            try {
                while (true){
                    String massage = Client.readMessage();
                    //сообщение о возврате на страницу
                    if(massage.equals("@returnToPage")){
                        Client.commandOverGame();
                        break;
                    }
                    String strs[] = massage.split(" ");
                    //сообщение о начале игры с игроком имя которого
                    if(strs[0].equals("@startGame")) {
                        opponentName = strs[1];

                        Client.commandOverGame();

                        Task<Void> taskOp = new Task<Void>() {
                            protected Void call() throws Exception {
                                Timeline fiveSecondsWonder = new Timeline();
                                fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(1), event -> {
                                    GameWindowOnline gameWindowOnline = new GameWindowOnline(stage, playerName, opponentName, board, true);
                                    gameWindowOnline.showGameWindowOnline();
                                }));
                                fiveSecondsWonder.play();
                                return null;
                            }
                        };
                        new Thread(taskOp).start();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };





    public WaitingOpponent(Node source,Board board) throws IOException {
        this.source = source;
        this.board = board;
        Client.commandMyName();
        this.playerName = Client.readMessage();

    }
    public void showWaitingForAnOpponent(){
        stage = (Stage) this.source.getScene().getWindow();
        Parent root = createShowWaitingForAnOpponent();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event ->{
            Client.commandReturnToPage();
            Client.commandExit();
            Platform.exit();
        });
        stage.show();
    }
    private BorderPane createShowWaitingForAnOpponent(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800, 400);
        HBox hBox = new HBox();

        final Label label = new Label("Please waiting opponent");
        label.setTextFill(Color.BLACK);
        label.setFont(new Font("Arial", 30));
        label.setPadding(new Insets(0, 0, 40, 0));

        final ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefSize(300, 100);

        Thread taskThread = new Thread(task);
        taskThread.start();

        final Button returnSetting = new Button("<");
        returnSetting.setPrefSize(100, 100);
        returnSetting.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Client.commandReturnToPage();
            try {
                GameList gameList = new GameList((Node) mouseEvent.getSource());
                gameList.showGameList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        hBox.getChildren().add(returnSetting);
        hBox.getChildren().add(progressBar);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);



        VBox vBox = new VBox();
        vBox.getChildren().add(label);
        vBox.getChildren().add(hBox);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        return borderPane;
    }
}

