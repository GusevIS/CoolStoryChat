package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorisationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Label errorsLabel;

    @FXML
    void initialize() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        signInButton.setOnAction(event -> {
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty()) {
                if (getUser(loginName, loginPass)) {
                    signInButton.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("chatForm.fxml"));

                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent root = loader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else
                    errorsLabel.setText("Incorrect username and/or password");
            } else
                errorsLabel.setText("Please, enter username and password");
        });

        signUpButton.setOnAction(event -> {
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty()) {
                if (!userExists(loginName)) {
                    dbHandler.signUpUser(loginName, loginPass);
                    errorsLabel.setText("Successful registration");
                }
                else
                    errorsLabel.setText("User with the same name already exists");
            } else
                errorsLabel.setText("Please, enter username and password");
        });
    }

    private boolean getUser(String loginName, String loginPass) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUser(loginName, loginPass);

        int counter = 0;
        try {
            while (result.next()) {
                counter++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return (counter != 0);
    }

    private boolean userExists(String loginName) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet result = dbHandler.getUser(loginName);

        int counter = 0;
        try {
            while (result.next()) {
                counter++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return (counter != 0);
    }
}