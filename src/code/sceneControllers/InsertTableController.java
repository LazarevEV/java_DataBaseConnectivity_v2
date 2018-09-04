package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import code.Table;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InsertTableController implements Initializable {
    @FXML
    private JFXButton insertRowButton;

    @FXML
    private JFXButton exitButton;

    @FXML
    private TableView<TableObject> tableView;

    DBConnection dbConnection;
    DBTableWorker dbtw;
    private WorkScreenController wsc = new WorkScreenController();

    private ArrayList<String> columnNameAL = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();

    private String tableName;
    private String value = "";
    private String columns = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void insertRow() throws SQLException {
        for (String str : data) {
            value += ("\'" + str + "\', ");
        }
        value = value.substring(0, value.length()-2);
        System.out.println("Value: " + value);
        dbtw.tableInsert(tableName, value);
        wsc.showTable(tableName);
        clearData();
    }

    public void initTable() {
        tableView.setEditable(true);
        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("columnName"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
        implementCellEditing(tableView.getColumns().get(1));

        for (String str : columnNameAL) {
            tableView.getItems().add(new TableObject(str, "NULL"));
            data.add("NULL");
        }
    }

    private void implementCellEditing(TableColumn tblColumn) {
        tblColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tblColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<TableObject, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<TableObject, String> t) {
                        ((TableObject) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setData(t.getNewValue());

                        data.set(t.getTablePosition().getRow(), t.getNewValue());
                        //data.add(t.getNewValue());
                    }

                }
        );
    }

    private void clearData() {
        value = "";
        data.clear();
        tableView.getItems().clear();
        for (String str : columnNameAL) {
            tableView.getItems().add(new TableObject(str, "NULL"));
            data.add("NULL");
        }
    }

    public void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public class TableObject {
        private final SimpleStringProperty columnName;
        private final SimpleStringProperty data;

        public TableObject(String columnName, String data) {
            this.columnName = new SimpleStringProperty(columnName);
            this.data = new SimpleStringProperty(data);
        }

        public String getColumnName() {
            return columnName.get();
        }

        public SimpleStringProperty columnNameProperty() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName.set(columnName);
        }

        public String getData() {
            return data.get();
        }

        public SimpleStringProperty dataProperty() {
            return data;
        }

        public void setData(String data) {
            this.data.set(data);
        }
    }

    public ArrayList<String> getColumnNameAL() {
        return columnNameAL;
    }

    public void setColumnNameAL(ArrayList<String> columnNameAL) {
        this.columnNameAL = columnNameAL;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

