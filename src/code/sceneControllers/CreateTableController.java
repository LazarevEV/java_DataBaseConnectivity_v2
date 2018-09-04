package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
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

    public void setColumn() {
        String columnName = clmnNameField.getText();
        String dataType = dataTypeField.getText();

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("columnName"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("dataType"));

        tableView.getItems().add(new TableObject(columnName, dataType));

        tblObjAL.add(new TableObject(columnName, dataType));

        clmnNameField.clear();
        dataTypeField.clear();

        System.out.println("columnName: " + tblObjAL.get(tblObjAL.size()-1).columnName.getValue() + " || dataType: " + tblObjAL.get(tblObjAL.size()-1).dataType.getValue());
    }

    public void createTable() throws SQLException {
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

    public class TableObject {
        private final SimpleStringProperty columnName;
        private final SimpleStringProperty dataType;

        public TableObject(String columnName, String dataType) {
            this.columnName = new SimpleStringProperty(columnName);
            this.dataType = new SimpleStringProperty(dataType);
        }

        public String getColumnName() {
            return columnName.getValue();
        }

        public void setColumnName(String columnName) {
            this.columnName.set(columnName);
        }

        public String getDataType() {
            return dataType.getValue();
        }

        public void setDataType(String dataType) {
            this.dataType.set(dataType);
        }
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

