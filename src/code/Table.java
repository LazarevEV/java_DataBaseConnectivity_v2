package code;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Table {
    private String tableName;
    private int colomns;
    private ArrayList<String> colomnNames;
    private ObservableList<ObservableList> data;

    public Table(String tableName, int colomns, ArrayList<String> colomnNames, ObservableList<ObservableList> data) {
        this.tableName = tableName;
        this.colomns = colomns;
        this.colomnNames = colomnNames;
        this.data = data;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getColumns() {
        return colomns;
    }

    public void setColomns(int colomns) {
        this.colomns = colomns;
    }

    public ArrayList<String> getColomnNames() {
        return colomnNames;
    }

    public void setColomnNames(ArrayList<String> colomnNames) {
        this.colomnNames = colomnNames;
    }

    public ObservableList<ObservableList> getData() {
        return data;
    }

    public void setData(ObservableList<ObservableList> data) {
        this.data = data;
    }
}
