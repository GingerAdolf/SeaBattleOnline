package graphicWindows.gameList;


import graphicWindows.creatingPlayerField.CreatingPlayerField;
import graphicWindows.gameMenu.GameMenu;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import network.Client;
import rwDataFile.ReadingDataFile;

import java.io.IOException;


public class GameList {
    private Node source;
    private String[] freeGameslist;
    private String namePlayer;

    public GameList(Node source) throws IOException {
        this.source = source;
        Client.commandSetGameList();
        String str = Client.readMessage();
        this.freeGameslist = new String[0];
        if(!str.equals("@returnToPage")){
            this.freeGameslist = str.split(" ");
        }
        ReadingDataFile readingDataFile = new ReadingDataFile();
        this.namePlayer = readingDataFile.name();
    }
    public void showGameList(){
        Stage stage = (Stage) source.getScene().getWindow();
        Parent root = this.createGameList();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Client.commandExit();
            Platform.exit();
        });
        stage.show();
    }
    private BorderPane createGameList(){
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800, 400);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(buttonReturn());
        hBox.getChildren().add(buttonNewGame());
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);
        Label nameClient = new Label(this.namePlayer + " can Game with: ");
        nameClient.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(nameClient);
        vBox.getChildren().add(scrollPanel());
        borderPane.setCenter(vBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        return borderPane;
    }
    private Button buttonReturn(){
        Button buttonReturn = new Button("<");
        buttonReturn.setPrefSize(200, 50);
        buttonReturn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            GameMenu gameMenu = new GameMenu((Node) mouseEvent.getSource());
            gameMenu.showGameMenu();
        });
        return buttonReturn;
    }
    private Button buttonNewGame(){
        Button buttonNewGame = new Button("NewGame");
        buttonNewGame.setPrefSize(200, 50);
        buttonNewGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            CreatingPlayerField creatingPlayerField = new CreatingPlayerField((Node) mouseEvent.getSource(), true);
            creatingPlayerField.showCreatingPlayingField(false);
        });
        return buttonNewGame;
    }
    private VBox scrollPanel() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPrefSize(400, 300);
        vBox.setMaxSize(400, 300);
        vBox.setMinSize(400, 300);
        ScrollPane sp = new ScrollPane();
        sp.setMinHeight(300);
        ///
        sp.setContent(new Label("No Online Player"));
        if(this.freeGameslist.length == 0 || this.freeGameslist[0].length() == 0){
            Label lb = new Label("No Online Player");
            lb.setTextFill(Color.BLUE);
            lb.setFont(new Font(20));
            lb.setAlignment(Pos.CENTER);
            sp.setContent(lb);
        }else{
            VBox vButtBox = new VBox();
            vButtBox.setSpacing(10);
            for (String aFreeGameslist : this.freeGameslist) {
                Button button = new Button(aFreeGameslist);
                button.setPrefSize(380, 50);
                button.setAlignment(Pos.CENTER);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    CreatingPlayerField creatingPlayerField = new CreatingPlayerField((Node) mouseEvent.getSource(), false, aFreeGameslist.toString());
                    creatingPlayerField.showCreatingPlayingField(false);
                });
                vButtBox.getChildren().add(button);
                vButtBox.setAlignment(Pos.CENTER);
            }
            sp.setContent(vButtBox);
        }
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        vBox.getChildren().add(sp);
        return vBox;
    }
}
