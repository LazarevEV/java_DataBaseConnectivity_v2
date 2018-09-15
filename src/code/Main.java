package code;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main extends Application {

    private StageMover stgm = new StageMover();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws ClassNotFoundException, SQLException, IOException {
        initScene(primaryStage);
    }

    private void initScene(Stage primaryStage) throws IOException {
        Parent root = new FXMLLoader(this.getClass().getResource("/resources/fxml/logInScreen.fxml")).load();
        primaryStage.setTitle("Log In");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        stgm.setMovable(root, primaryStage);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
