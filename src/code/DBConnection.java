package code;

import java.sql.*;

public class DBConnection {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private String localhost = "MSI";
    private String dbName = "oracleDB";
    private String url = "jdbc:oracle:thin:@" + localhost + ":1521:" + dbName;

    public void setConnection(String user, String password) throws ClassNotFoundException, SQLException {
        //set Oracle Java DataBase Connectivity Driver
        Class.forName("oracle.jdbc.driver.OracleDriver");
        //create Connection
        connection = DriverManager.getConnection(url, user, password);


        //create Statement Object
        //statement = connection.createStatement();
        //execute query (запрос)
        //resultSet = statement.executeQuery("SELECT * FROM branch");


    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

}
