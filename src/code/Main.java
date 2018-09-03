package code;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main extends Application {

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
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
