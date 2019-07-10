package sample;

import javax.jws.soap.SOAPBinding;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseHandler extends Configs{
    Connection dbConnection;

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
}