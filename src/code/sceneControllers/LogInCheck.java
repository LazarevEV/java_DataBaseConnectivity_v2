package code.sceneControllers;

import code.DBConnection;

import java.sql.SQLException;

public class LogInCheck {
    private DBConnection dbConnection = new DBConnection();

    public boolean check(String username, String password)  {
        boolean connected = true;
        try {
            dbConnection.setConnection(username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            connected = false;
        }
        try {
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connected;

    }
}
