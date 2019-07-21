import java.io.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorisationController {
    private Client client;

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
        client = Main.getClient();

        signInButton.setOnAction(event -> {
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty()) {
                switch (client.signIn(loginName, loginPass)){
                    case SUCCESSFUL_SIGN_IN:
                        showChat();
                        break;
                    case FAILED_SIGN_IN:
                        errorsLabel.setText("Incorrect username and/or password");
                        break;
                    case DATABASE_OFFLINE:
                        showErrorForm();
                        break;
                }
            } else
                errorsLabel.setText("Please, enter username and password");
        });

        signUpButton.setOnAction(event -> {
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty()) {
                switch (client.signIn(loginName, loginPass)){
                    case SUCCESSFUL_SIGN_UP:
                        errorsLabel.setText("Successful registration");
                        break;
                    case FAILED_SIGN_UP:
                        errorsLabel.setText("User with the same name already exists");
                        break;
                    case DATABASE_OFFLINE:
                        showErrorForm();
                        break;
                }
            } else
                errorsLabel.setText("Please, enter username and password");
        });
    }

    public void showChat(){
        signInButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/chatForm.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void  showErrorForm(){
        signInButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/errorForm.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}