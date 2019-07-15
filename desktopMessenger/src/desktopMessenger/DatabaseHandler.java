package desktopMessenger;

import java.sql.*;

public class DatabaseHandler extends Configs{
    private Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String URL = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?verifyServerCertificate=false"+
                "&useSSL=false"+
                "&requireSSL=false"+
                "&useLegacyDatetimeCode=false"+
                "&amp"+
                "&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(URL, dbUser, dbPass);
        return  dbConnection;
    }

    public void signUpUser(String userName, String userPass) {
        String insert = "INSERT INTO " + UserFields.USER_TABLE + "("
                + UserFields.USERS_NAME + "," + UserFields.USERS_PASSWORD + ")"
                + "VALUES(?,?)";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPass);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUser(String userName, String userPass){
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + UserFields.USER_TABLE + " WHERE " +
                UserFields.USERS_NAME + "=? AND " + UserFields.USERS_PASSWORD + "=?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPass);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getUser(String userName){
        ResultSet resultSet = null;

        String select = "SELECT * FROM " + UserFields.USER_TABLE + " WHERE " +
                UserFields.USERS_NAME + "=?";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, userName);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

//    public int getUserID(String userName){
//        ResultSet resulSet = null;
//
//        String select = "SELECT * FROM " + UserFields.USER_TABLE + " WHERE " +
//                UserFields.USERS_NAME + "=?";
//
//        try{
//            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
//            preparedStatement.setString(1, userName);
//        }catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
}