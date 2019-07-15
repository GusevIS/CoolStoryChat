package desktopMessenger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorisationController {
    public static boolean clientIsConnectedToChat;
    private Socket clientSocket;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;

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
    void initialize() throws IOException {
        clientIsConnectedToChat = false;

        signInButton.setOnAction(event -> {
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty()) {
                try {
                    out.writeObject(new AuthorisationMessage(loginName, loginPass, MessageFlag.SIGN_IN));
                    out.flush();
                    AuthorisationMessage authMessage;
                    authMessage = (AuthorisationMessage) in.readObject();
                    switch (authMessage.flag) {
                        case SUCCESSFUL_SIGN_IN:
                            clientIsConnectedToChat = true;
                            ChatController.setClientName(authMessage.username);

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

                            break;

                        case FAILED_SIGN_IN:
                            errorsLabel.setText("Incorrect username and/or password");
                            break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else
                errorsLabel.setText("Please, enter username and password");
        });

        signUpButton.setOnAction(event -> {
            String loginName = loginField.getText().trim();
            String loginPass = passwordField.getText().trim();

            if(!loginName.isEmpty() && !loginPass.isEmpty()) {
                try {
                    out.writeObject(new AuthorisationMessage(loginName, loginPass, MessageFlag.SIGN_UP));
                    out.flush();

                    AuthorisationMessage authMessage;
                    authMessage = (AuthorisationMessage) in.readObject();

                    switch (authMessage.flag) {
                        case SUCCESSFUL_SIGN_UP:
                            errorsLabel.setText("Successful registration");
                            break;

                        case FAILED_SIGN_UP:
                            errorsLabel.setText("User with the same name already exists");
                            break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else
                errorsLabel.setText("Please, enter username and password");
        });
        getServerConnection();
    }

    private void getServerConnection() throws IOException {
        clientSocket = new Socket("localhost", 4999);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }


    private void setErrorForm(){
        signInButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("errorForm.fxml"));

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