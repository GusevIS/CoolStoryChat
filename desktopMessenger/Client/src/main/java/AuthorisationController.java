import java.io.*;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorisationController {
    private static boolean clientIsConnectedToChat;
    private Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;
    private final Gson gson = new GsonBuilder().create();


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
                    String authMsg = gson.toJson(new AuthorisationMessage(loginName, loginPass, MessageFlag.SIGN_IN));
                    out.write(authMsg + "\n");
                    out.flush();

                    String msg;
                    msg = in.readLine();
                    AuthorisationMessage authMessage = gson.fromJson(msg, AuthorisationMessage.class);
                    switch (authMessage.getFlag()) {
                        case SUCCESSFUL_SIGN_IN:
                            clientIsConnectedToChat = true;
                            ChatController.setClientName(authMessage.getUsername());

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

                            break;

                        case FAILED_SIGN_IN:
                            errorsLabel.setText("Incorrect username and/or password");
                            break;
                    }
                } catch (IOException e) {
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
                    String authMsg = gson.toJson(new AuthorisationMessage(loginName, loginPass, MessageFlag.SIGN_UP));
                    out.write(authMsg + "\n");
                    out.flush();

                    String msg;
                    msg = in.readLine();

                    AuthorisationMessage authMessage = gson.fromJson(msg, AuthorisationMessage.class);

                    switch (authMessage.getFlag()) {
                        case SUCCESSFUL_SIGN_UP:
                            errorsLabel.setText("Successful registration");
                            break;

                        case FAILED_SIGN_UP:
                            errorsLabel.setText("User with the same name already exists");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                errorsLabel.setText("Please, enter username and password");
        });
        getServerConnection();
    }

    private void getServerConnection() throws IOException {
        clientSocket = new Socket("localhost", 4990);
        setOut(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        setIn(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
    }

    public static void setIn(BufferedReader input){
        in = input;
    }

    public static BufferedReader getIn(){
        return in;
    }

    public static void setOut(BufferedWriter output){
        out = output;
    }

    public static BufferedWriter getOut(){
        return out;
    }

    public static void setClientIsConnectedToChat(boolean value){
        clientIsConnectedToChat = value;
    }

    public static boolean getClientIsConnectedToChat(){
        return clientIsConnectedToChat;
    }
}