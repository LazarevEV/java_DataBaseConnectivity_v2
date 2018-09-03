package code.sceneControllers;

import code.Table;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private int columnAmount;
    private String tableName;

    private ArrayList<TableObject> tblObjAL = new ArrayList<>();

    public void updateTableView() {
    }

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

    public void createTable() {

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
}

