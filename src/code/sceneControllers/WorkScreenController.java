package code.sceneControllers;

import code.DBConnection;
import code.DBTableWorker;
import code.Table;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    private VBox tableList;

    @FXML
    private TableView<ObservableList> tableView;

    DBConnection dbConnection;
    DBTableWorker dbtw;

    private String username;
    private String password;
    private ArrayList<String> tableArrList = new ArrayList<String>();
    private Table table;

    private String tableSelected = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showTableList() throws SQLException {
        tableList.getChildren().clear();
        tableArrList = dbtw.getTableList();
        for (String talTemp : tableArrList) {
            JFXButton button = new JFXButton();
            System.out.println(tableList.getPrefWidth());
            button.setPrefSize(tableList.getPrefWidth(), Region.USE_COMPUTED_SIZE);
            button.setText(talTemp);
            button.setButtonType(JFXButton.ButtonType.FLAT);
            button.getStylesheets().add("/resources/css/WorkScreen.css");
            button.getStyleClass().add("tableButton");
            button.setOnAction(event -> {
                try {
                    showTable(talTemp);
                    tableSelected = talTemp;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            tableList.getChildren().add(button);
        }
          //TEST SCROLL PANE
//        for (int i = 0; i<10; i++) {
//            JFXButton button = new JFXButton();
//            button.setPrefSize(tableList.getWidth(), Region.USE_COMPUTED_SIZE);
//            button.setText("Button "+i);
//            button.setButtonType(JFXButton.ButtonType.FLAT);
//            button.getStylesheets().add("/resources/css/WorkScreen.css");
//            button.getStyleClass().add("tableButton");
//            tableList.getChildren().add(button);
//        }
    }

    private void showTableI(String tableName) throws SQLException {
        table = dbtw.showAll(tableName);
        tableView.getColumns().clear();
        ArrayList<TableColumn> tabColAL = new ArrayList<>();
        for (String str : table.getColomnNames()) {
            TableColumn tabCol = new TableColumn(str);
            tabCol.setPrefWidth(tableView.getPrefWidth() / table.getColumns());
            tableView.getColumns().add(tabCol);
        }
    }

    public void showTable(String tableName) throws SQLException {
        table = dbtw.showAll(tableName);
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
            Stage wscStage = new Stage();
            wscStage.setTitle("Working Screen");
            wscStage.setScene(new Scene(root));
            wscStage.setResizable(false);
            wscStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertTable() {
        if (tableSelected.equals("")) return;
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/resources/fxml/InsertTable.fxml"));
            loader.load();

            InsertTableController itc = loader.getController();
            itc.setDbConnection(dbConnection);
            itc.setDbtw(dbtw);
            itc.setWsc(this);
            itc.setTableName(tableSelected);
            itc.setColumnNameAL(table.getColomnNames());
            itc.initTable();

            Parent root = loader.getRoot();
            Stage itcStage = new Stage();
            itcStage.setTitle("Working Screen");
            itcStage.setScene(new Scene(root));
            itcStage.setResizable(false);
            itcStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTable() {

    }

    public void dropTable() throws SQLException {
        dbtw.tableDrop(tableSelected);
        this.showTableList();
        tableView.getColumns().clear();
        tableView.getItems().clear();
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
}
