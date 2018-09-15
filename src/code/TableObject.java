package code;

import javafx.beans.property.SimpleStringProperty;

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
