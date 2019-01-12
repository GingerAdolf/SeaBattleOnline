package graphicWindows.gameMenu;


import graphicWindows.gameList.GameList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import network.Client;
import rwDataFile.ReadingDataFile;
import rwDataFile.WritingDataFile;

import java.io.IOException;

public class GameMenu {
    //ресурс пердыдущего окна
    private Node source;
    //имя клиента
    private String playerName;
    private TextField textField;

    //конструктор по умолчанию
    public GameMenu(){
        ReadingDataFile readingDataFile = new ReadingDataFile();
        this.playerName = readingDataFile.name();
    }
    //конструктор возврата действия
    public GameMenu(Node sourse){
        ReadingDataFile readingDataFile = new ReadingDataFile();
        this.playerName = readingDataFile.name();
        this.source = sourse;
    }

    //метод показа лкна меню
    public void showGameMenu() {
        Stage stage = (Stage) this.source.getScene().getWindow();
        Parent root = createGameMenu();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //метод создающий меню
    public BorderPane createGameMenu() {
        BorderPane  borderPane = new BorderPane();
        borderPane.setPrefSize(800, 400);
        VBox vBox = new VBox();
        vBox.setPrefSize(300, 300);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label());
        vBox.getChildren().add(buttonPlay());
        vBox.getChildren().add(textField());
        vBox.getChildren().add(buttonExit());
        vBox.setSpacing(5);
        borderPane.setCenter(vBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        return borderPane;
    }
    //создание лайбла  и его свойства
    private Label label() {
        Label lb = new Label("SeaBattleOnline");
        lb.setTextFill(Color.RED);
        lb.setFont(new Font("Arial", 30));
        lb.setPadding(new Insets(0, 0, 40, 0));
        return lb;
    }
    //кнопка начала ингры
    private Button buttonPlay(){
        Button play = new Button("Play");
        play.setPrefSize(300, 50);
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            WritingDataFile writingDataFile = new WritingDataFile();
            writingDataFile.writingDataFile(this.textField.getText());
            //отправка имени на сервер
            Client.commandNewName(this.textField.getText());
            //перересовка оекна
            try {
                GameList gameList = new GameList((Node)mouseEvent.getSource());
                gameList.showGameList();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return play;
    }
    //поле в которое можно ввести имя
    private HBox textField() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefSize(300, 50);
        Label lb = new Label("Player Name : ");
        this.textField = new TextField( this.playerName);
        hBox.getChildren().add(lb);
        hBox.getChildren().add(textField);
        return hBox;
    }
    //кнопка выхода
    private Button buttonExit(){
        Button play = new Button("Exit");
        play.setPrefSize(300, 50);
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Client.commandExit();
            Node source = (Node) mouseEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.hide();
            Platform.exit();
        });
        return play;
    }
}
