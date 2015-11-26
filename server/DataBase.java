package server;

import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
    private Statement stmt;

    public DataBase() {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser(Constants.mysqlUserName);
            dataSource.setPassword(Constants.mysqlPassword);
            dataSource.setServerName(Constants.mysqlServerName);
            dataSource.setDatabaseName(Constants.mysqlDatabase);

            Connection connection = dataSource.getConnection();
            stmt = (Statement) connection.createStatement();
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Can't connect to Mysql Server");
        }
    }

    public boolean checkCredentials(String userName, String password){

        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT Password FROM users WHERE Name = '" + userName + "'");
            if (rs.first()) return rs.getString("Password").equals(password);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    public boolean addNewUser(String userName, String password){
        try {
            stmt.execute("INSERT INTO users (Name, Password, Money) VALUES ('" + userName + "','" + password + "'," + 100 + ");");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeUser(String userName){
        try {
            stmt.execute("DELETE FROM users WHERE Name='" + userName + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMoney(String userName, int money){
        try {
            stmt.execute("UPDATE users SET Money=" + money + " WHERE Name = '" + userName + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMoney(String userName){
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT Money FROM users WHERE Name = '" + userName + "'");
            if (rs.first()) return rs.getInt("Money");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
