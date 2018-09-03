package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LogInScreenController implements Initializable {

    @FXML
    private TextField userTF;

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField passTF;

    private String username;
    private String password;

    private DBConnection dbConnection = new DBConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void logIn() throws SQLException, ClassNotFoundException, IOException {
        setUsername(userTF.getText());
        setPassword(passTF.getText());
        LogInCheck logInCheck = new LogInCheck();

        if (logInCheck.check(username, password)) {
            dbConnection.setConnection(username, password);
            System.out.println("CORRECT");
            openWorkScreen();
            ((Stage) loginButton.getScene().getWindow()).close();
        } else {
            System.out.println("Username or password is incorrect! Try again!");
            userTF.setText(null);
            passTF.setText(null);
        }
    }

    private void openWorkScreen() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/WorkScreen.fxml"));
        loader.load();

        WorkScreenController wsc = loader.getController();
        wsc.setUsername(username);
        wsc.setPassword(password);
        wsc.setDbConnection(dbConnection);
        wsc.setDbtw(new DBTableWorker(dbConnection));

        Parent root = loader.getRoot();
        Stage wscStage = new Stage();
        wscStage.setTitle("Working Screen");
        wscStage.setScene(new Scene(root));
        wscStage.setResizable(false);
        wscStage.show();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DBConnection getDbConnection() {
        return dbConnection;
    }
}
