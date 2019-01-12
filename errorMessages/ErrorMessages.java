package errorMessages;

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
import javafx.stage.Stage;
import network.Client;


public class ErrorMessages{
    public void showWindowError(String message){
        Stage stage = new Stage();
        stage.setTitle("Error");
        Parent root = createWindowError(message);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        stage.show();
    }
    public void showWindowErrorCreateBoard(String message){
        Stage stage = new Stage();
        stage.setTitle("Error");
        Parent root = createWindowErrorCreateBoard(message);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            stage.hide();
        });
        stage.show();
    }

    private Parent createWindowErrorCreateBoard(String message) {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(200, 100);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        Label lb = new Label(message);
        Button bt = new Button("Ok");
        bt.setPrefSize(40, 20);
        bt.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Node source = (Node) mouseEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.hide();
        });
        vBox.getChildren().add(lb);
        vBox.getChildren().add(bt);
        pane.setCenter(vBox);
        pane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        return pane;
    }


    private BorderPane createWindowError(String message){
        BorderPane pane = new BorderPane();
        pane.setPrefSize(200, 100);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        Label lb = new Label(message);
        Button bt = new Button("Ok");
        bt.setPrefSize(40, 20);
        bt.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Node source = (Node) mouseEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.hide();
            Platform.exit();
        });
        vBox.getChildren().add(lb);
        vBox.getChildren().add(bt);
        pane.setCenter(vBox);
        pane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        return pane;
    }




}
