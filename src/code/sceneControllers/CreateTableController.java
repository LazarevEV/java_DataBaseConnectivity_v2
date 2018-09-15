package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import code.TableObject;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;

public class CreateTableController {

    @FXML
    private TextField clmnAmountField;

    @FXML
    private TextField tblNameField;

    @FXML
    private TextField clmnNameField;

    @FXML
    private TextField dataTypeField;

    @FXML
    private TableView<TableObject> tableView;

    @FXML
    private JFXButton setColumn;

    @FXML
    private JFXButton createTblButton;

    @FXML
    private JFXButton exitButton;

    DBConnection dbConnection;
    DBTableWorker dbtw;

    private String tableName;
    private WorkScreenController wsc = new WorkScreenController();

    private ArrayList<TableObject> tblObjAL = new ArrayList<>();



    private boolean checkField() {
        int a = 0;

        if (tblNameField.getText().equals("")) {
            tblNameField.setPromptText("Set TableName");
            tblNameField.setStyle("-fx-prompt-text-fill: red");
            a++;
        }

        if (clmnNameField.getText().equals("")) {
            clmnNameField.setPromptText("Set TableName");
            clmnNameField.setStyle("-fx-prompt-text-fill: red");
            a++;
        }

        if (dataTypeField.getText().equals("")) {
            dataTypeField.setPromptText("Set Data");
            dataTypeField.setStyle("-fx-prompt-text-fill: red");
            a++;
        }

        if (a!=0) return false;

        return true;
    }

    private void clearPromtErrors() {
        tblNameField.setPromptText("");
        clmnNameField.setPromptText("");
        dataTypeField.setPromptText("");
    }

    public void setColumn() {
        clearPromtErrors();

        if (!checkField()) return;

        String columnName = clmnNameField.getText();
        String dataType = dataTypeField.getText();

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("columnName"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));

        tableView.getItems().add(new TableObject(columnName, dataType));

        tblObjAL.add(new TableObject(columnName, dataType));

        clmnNameField.clear();
        dataTypeField.clear();

    }

    public void createTable() throws SQLException {
        clearPromtErrors();
        System.out.println(tblNameField.getText());

        if (!checkField()) return;

        tableName = tblNameField.getText();

        dbtw.tableCreate(tblObjAL, tableName);
        wsc.showTableList();
        tableView.getItems().clear();
        tblNameField.clear();
    }

    public void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


    public DBConnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public DBTableWorker getDbtw() {
        return dbtw;
    }

    public void setDbtw(DBTableWorker dbtw) {
        this.dbtw = dbtw;
    }

    public WorkScreenController getWsc() {
        return wsc;
    }

    public void setWsc(WorkScreenController wsc) {
        this.wsc = wsc;
    }
}

