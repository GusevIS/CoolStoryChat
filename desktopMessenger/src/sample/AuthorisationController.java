package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    void initialize() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        signInButton.setOnAction(event -> {
            //Sign in
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty())
                signInUser(loginName, loginPass);


            signInButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("chatForm.fxml"));

            try{
                loader.load();
            } catch (IOException e){
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });

        signUpButton.setOnAction(event -> {
            //Sign up

            dbHandler.signUpUser(loginField.getText(), passwordField.getText());
        });
    }

    private void signInUser(String loginName, String loginPass) {


    }
}