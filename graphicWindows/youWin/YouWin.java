package graphicWindows.youWin;


import graphicWindows.gameMenu.GameMenu;
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


public class YouWin {
    private Node source;
    private Stage stage;
    public YouWin(Node source){
        this.source = source;
    }

    public YouWin() {
        this.stage = new Stage();
    }

    public void showYouWin(boolean win) {
        if(win) {
            stage = (Stage) this.source.getScene().getWindow();
        }
        Parent root = createYouWin(win);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    private BorderPane createYouWin(boolean win) {
        BorderPane  borderPane = new BorderPane();
        borderPane.setPrefSize(800, 400);
        VBox vBox = new VBox();
        vBox.setPrefSize(300, 300);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(label(win));
        vBox.getChildren().add(buttonExitMain());
        vBox.setSpacing(5);
        borderPane.setCenter(vBox);
        borderPane.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        return borderPane;
    }

    private Button buttonExitMain() {
        Button play = new Button("return Main");
        play.setPrefSize(300, 50);
        play.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            GameMenu gameMenu = new GameMenu((Node) mouseEvent.getSource());
            gameMenu.showGameMenu();
        });
        return play;
    }

    private Label label(boolean win) {
        String text = "";
        if(win){
            text = "you WIN!";
        }
        if(!win){
            text = "you Loser!";
        }
        Label lb = new javafx.scene.control.Label(text);
        lb.setTextFill(Color.RED);
        lb.setFont(new Font("Arial", 30));
        lb.setPadding(new Insets(0, 0, 40, 0));
        return lb;
    }

}
