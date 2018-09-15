package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import code.TableObject;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InsertUpdateTableController implements Initializable {
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
    private ArrayList<Integer> editedCellId = new ArrayList<>();
    private ObservableList<String> oldRow;

    private String tableName;
    private String value = "";
    private String where = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void updateRow() throws SQLException {
        if (!isDataEmpty(data)) return;

        String value = "";

        for (int a : editedCellId) {
            System.out.println(data.get(a));
            if (data.get(a).equals("NULL")) {
                value += (", " + columnNameAL.get(a) + "=" + data.get(a));
            } else {
                value += (", " + columnNameAL.get(a) + "=\'" + data.get(a) + "\'");
            }
        }
        editedCellId.clear();
        value = value.substring(2);
        dbtw.tableUpdate(tableName, value, where);
        wsc.showTable(tableName, "ALL", null);

        dataInit();
        getWhere();
    }

    private boolean isDataEmpty(ArrayList<String> data) {
        for (String str: data) {
            if (str!=null) return true;
        }
        return false;
    }

    private void dataInit() {
        for (int i = 0; i <data.size(); i++) {
            data.set(i, null);
        }
    }

    public void getWhere() {
        where = "";
        for (int i =0; i < columnNameAL.size(); i++) {
            where += " " + columnNameAL.get(i) + " = \'" +
                    tableView.getColumns().get(1).getCellData(i) + "\' AND";
        }
        where = where.substring(0, where.length()-4);
    }

    @FXML
    public void insertRow() throws SQLException {
        for (String str : data) {
            if (str == "NULL") {
                value += str + ", ";
            } else {
                value += ("\'" + str + "\', ");
            }

        }
        value = value.substring(0, value.length() - 2);
        System.out.println("Value: " + value);
        dbtw.tableInsert(tableName, value);
        wsc.showTable(tableName, "ALL", null);
        clearData();
    }

    public void initTable(String type) {
        tableView.setEditable(true);
        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("columnName"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
        implementCellEditing(tableView.getColumns().get(1));

        if (type.equals("INSERT")) {
            for (String str : columnNameAL) {
                tableView.getItems().add(new TableObject(str, "NULL"));
                data.add("NULL");
            }
        }

        if (type.equals("UPDATE")) {
            for (int i =0; i<columnNameAL.size(); i++) {
               tableView.getItems().add(new TableObject(columnNameAL.get(i), (String) oldRow.get(i)));
               data.add(null);
            }
        }

        getWhere();
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
                        editedCellId.add(t.getTablePosition().getRow());
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

    public ObservableList<String> getOldRow() {
        return oldRow;
    }

    public void setOldRow(ObservableList<String> oldRow) {
        this.oldRow = oldRow;
    }
}

