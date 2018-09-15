package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import code.StageMover;
import code.Table;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WorkScreenController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXButton tableListButton;

    @FXML
    private JFXButton createTblButton;

    @FXML
    private JFXButton insertButton;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton dropButton;

    @FXML
    private JFXButton clearFilterBtn;

    @FXML
    private JFXButton exitButton;

    @FXML
    private TableView<TableObject> filterTableView;

    @FXML
    private VBox tableList;

    @FXML
    private TableView<ObservableList> tableView;

    DBConnection dbConnection;
    DBTableWorker dbtw;
    private StageMover stgm = new StageMover();

    private String username;
    private String password;
    private ArrayList<String> tableArrList = new ArrayList<String>();
    private Table table;
    private String tableSelected = "";
    private ArrayList<String> filterWhere = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showTableList() throws SQLException {
        tableList.getChildren().clear();
        tableArrList = dbtw.getTableList();
        for (String talTemp : tableArrList) {
            JFXButton button = new JFXButton();
            button.setPrefSize(tableList.getPrefWidth(), Region.USE_COMPUTED_SIZE);
            button.setText(talTemp);
            button.setButtonType(JFXButton.ButtonType.FLAT);
            button.getStylesheets().add("/resources/css/Button.css");
            button.getStyleClass().add("jfxbutton");
            button.setOnAction(event -> {
                try {
                    showTable(talTemp, "ALL", null);
                    tableSelected = talTemp;
                    tableView.getSelectionModel().clearSelection();
                    initFilterTableView();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            tableList.getChildren().add(button);
        }
    }

    private void showSelectedTable(ArrayList<String> filterWhere) throws SQLException {
        String where = "";
        for (int i =0; i < table.getColumns(); i++) {
            if (filterWhere.get(i) != null) {
                where += " " + table.getColomnNames().get(i) + " LIKE \'" +
                        filterWhere.get(i)+ "%\' AND";
            }
        }
        where = where.substring(0, where.length()-4);

        showTable(table.getTableName(), "SELECTIVE", where);
    }

    private void initFilterWhere() {
        for (int i = 0; i < table.getColumns(); i++) {
            filterWhere.add(null);
        }
    }

    public void clearFilter() throws SQLException {
        filterWhere.clear();
        initFilterWhere();
        filterTableView.getItems().clear();
        initFilterTableView();
        showTable(table.getTableName(), "ALL", null);
    }

    public void initFilterTableView() {
        filterTableView.getItems().clear();
        ArrayList<String> data = new ArrayList<>();
        filterTableView.setEditable(true);
        filterTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("columnName"));
        filterTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("data"));
        filterTableView.getColumns().get(0).setSortable(false);
        filterTableView.getColumns().get(1).setSortable(false);
        implementCellEditing(filterTableView.getColumns().get(1));

        for (String str : table.getColomnNames()) {
            filterTableView.getItems().add(new TableObject(str, "NULL"));
            data.add("NULL");
        }

        initFilterWhere();
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

                        filterWhere.set(t.getTablePosition().getRow(), t.getNewValue());
                        try {
                            showSelectedTable(filterWhere);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                }
        );
    }


    public void showTable(String tableName, String type, String where) throws SQLException {
        table = dbtw.showAll(tableName, type, where);
        tableView.getColumns().clear();

        for (int i = 0; i < table.getColumns(); i++) {
            final int j = i;
            TableColumn tabCol = new TableColumn(table.getColomnNames().get(i));
            tabCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });


            tabCol.setPrefWidth(tableView.getPrefWidth() / table.getColumns());
            tableView.getColumns().add(tabCol);
        }

        tableView.setItems(table.getData());

    }

    public void createTable() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/fxml/CreateTable.fxml"));
            loader.load();

            CreateTableController ctc = loader.getController();
            ctc.setDbConnection(dbConnection);
            ctc.setDbtw(dbtw);
            ctc.setWsc(this);

            Parent root = loader.getRoot();
            Stage ctcStage = new Stage();
            ctcStage.initStyle(StageStyle.UNDECORATED);
            ctcStage.setTitle("Working Screen");
            ctcStage.setScene(new Scene(root));
            stgm.setMovable(root, ctcStage);
            ctcStage.setResizable(false);
            ctcStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertRow() {
        if (tableSelected.equals("")) return;
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/fxml/InsertTable.fxml"));
            loader.load();

            InsertUpdateTableController itc = loader.getController();
            itc.setDbConnection(dbConnection);
            itc.setDbtw(dbtw);
            itc.setWsc(this);
            itc.setTableName(tableSelected);
            itc.setColumnNameAL(table.getColomnNames());
            itc.initTable("INSERT");

            Parent root = loader.getRoot();
            Stage itcStage = new Stage();
            itcStage.initStyle(StageStyle.UNDECORATED);
            itcStage.setTitle("Working Screen");
            itcStage.setScene(new Scene(root));
            stgm.setMovable(root, itcStage);
            itcStage.setResizable(false);
            itcStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow() throws SQLException {
        if (tableView.getSelectionModel().isEmpty()) return;
        String where = "";

        for (int i =0; i < table.getColumns(); i++) {
            where += " " + table.getColomnNames().get(i) + " = \'" +
                    tableView.getSelectionModel().getSelectedItem().get(i) + "\' AND";
        }
        where += where.substring(0, where.length()-4);

        dbtw.tableDelete(tableSelected, where);
        showTable(tableSelected, "ALL", null);

        tableView.getSelectionModel().clearSelection();
    }

    public void updateRow() {
        if (tableView.getSelectionModel().isEmpty()) return;

        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/fxml/UpdateTable.fxml"));
            loader.load();

            InsertUpdateTableController utc = loader.getController();
            utc.setDbConnection(dbConnection);
            utc.setDbtw(dbtw);
            utc.setWsc(this);
            utc.setTableName(tableSelected);
            utc.setColumnNameAL(table.getColomnNames());
            utc.setOldRow(table.getData().get(tableView.getSelectionModel().getSelectedIndex()));
            utc.initTable("UPDATE");


            Parent root = loader.getRoot();
            Stage itcStage = new Stage();
            itcStage.initStyle(StageStyle.UNDECORATED);
            itcStage.setTitle("Working Screen");
            itcStage.setScene(new Scene(root));
            stgm.setMovable(root, itcStage);
            itcStage.setResizable(false);
            itcStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.getSelectionModel().clearSelection();
    }

    public void dropTable() throws SQLException {
        dbtw.tableDrop(tableSelected);
        this.showTableList();
        tableView.getColumns().clear();
        tableView.getItems().clear();
    }

    public void exit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
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

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public DBTableWorker getDbtw() {
        return dbtw;
    }

    public void setDbtw(DBTableWorker dbtw) {
        this.dbtw = dbtw;
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

}
