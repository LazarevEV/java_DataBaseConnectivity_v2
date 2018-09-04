package code;

import code.sceneControllers.CreateTableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.sql.*;
import java.util.ArrayList;

public class DBTableWorker {
    DBConnection dbConnection;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData rsmd;
    private String tableName = null;

    public DBTableWorker(DBConnection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        statement = this.dbConnection.getConnection().createStatement();
    }

    public void DBTW_init() throws SQLException {
        statement = dbConnection.getConnection().createStatement();
    }

    public ArrayList<String> getTableList() throws SQLException {
        ArrayList<String> tableList = new ArrayList<String>();
        resultSet = statement.executeQuery("SELECT table_name FROM user_tables");
        while (resultSet.next()) {
            tableList.add(resultSet.getString(1));
        }
        return tableList;
    }

    public void showTableListI() throws SQLException { //DOESNT WORK
        DatabaseMetaData dmd = dbConnection.getConnection().getMetaData();
        resultSet = dmd.getTables(null, null, null, null);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(3));
        }
    }

    public void tableCreate(ArrayList<CreateTableController.TableObject> tblObjAL, String tableName) throws SQLException {
        String code = "";
        for (CreateTableController.TableObject tblObj : tblObjAL) {
            code += tblObj.getColumnName() + "  " + tblObj.getDataType() + ",\n";
        }
        code = code.substring(0, code.length()-2);
        String selectTable = "CREATE TABLE " + tableName + " (\n" + code + "\n)";
        System.out.println("\n" + selectTable + "\n");
        resultSet = statement.executeQuery(selectTable);
        System.out.println("TABLE " + tableName + " CREATED!");
    }

    public void tableDrop(String tableName) throws SQLException {
        resultSet = statement.executeQuery("DROP TABLE " + tableName);
        System.out.println("TABLE " + tableName + " HAS BEEN DROPPED");
    }

    public void tableUpdate() {

    }

    public void tableInsert(String tableName, String value) throws SQLException {
        String query = "INSERT INTO " + tableName + " VALUES (" + value + ")";
        System.out.println(query);
        resultSet = statement.executeQuery(query);
    }

    public Table showAll(String tableName) throws SQLException {
        resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        rsmd = resultSet.getMetaData();

        int colomns = rsmd.getColumnCount();
        ArrayList<String> colomnNames = new ArrayList<>();

        for (int i = 1; i <= colomns; i++) colomnNames.add(rsmd.getColumnName(i));


//        resultSet.close();
//        statement.close();
        return new Table(tableName, colomns, colomnNames, getData(resultSet));
    }

    public ObservableList<ObservableList> getData(ResultSet resultSet) throws SQLException {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=resultSet.getMetaData().getColumnCount(); i++){
                if (resultSet.getString(i) == null) row.add("NULL");
                else row.add(resultSet.getString(i));
            }
            data.add(row);
        }

        return data;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
