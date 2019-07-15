package desktopMessenger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChatController {
    private static String clientName;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea outputMsgArea;

    @FXML
    private TextArea inputMsgArea;

    @FXML
    private ComboBox<?> smileChoiceButton;

    @FXML
    private Button LogOutButton;

    @FXML
    void initialize() throws IOException {
        inputMsgArea.setWrapText(true);
        outputMsgArea.setWrapText(true);
        outputMsgArea.setEditable(false);

        sendButton.setOnAction(event -> {
            String msg = inputMsgArea.getText().trim();
            if(!msg.isEmpty()){
                try {
                    AuthorisationController.out.writeObject(new ClientMessage(getClientName(), msg, MessageFlag.DEFAULT_MESSAGE));
                    AuthorisationController.out.flush();
                    inputMsgArea.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        smileChoiceButton.setOnAction(event -> {
            //Choose smile
        });

        LogOutButton.setOnAction(event -> {
            try {
                System.out.println("log out from chat");
                AuthorisationController.out.writeObject(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_CHAT));
                AuthorisationController.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LogOutButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("authorisationForm.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });

        getServerConnection();
    }

    private void getServerConnection(){
        new Thread(() -> {
            ClientMessage messageFromServer;
            try {
                while (AuthorisationController.getClientIsConnectedToChat()) {
                    messageFromServer = (ClientMessage) AuthorisationController.in.readObject();

                    switch (messageFromServer.flag){
                        case DEFAULT_MESSAGE:
                            final String textOutputMsgArea = messageFromServer.username + ">> " + messageFromServer.message;
                            Platform.runLater(() -> outputMsgArea.appendText(textOutputMsgArea + "\n"));
                            break;

                        case LOG_OUT_FROM_CHAT:
                            AuthorisationController.setClientIsConnectedToChat(false);
                            break;

                        case LOG_OUT_FROM_SERVER:
                            AuthorisationController.setClientIsConnectedToChat(false);
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void setClientName(String name){
        clientName = name;
    }

    public String getClientName(){
        return clientName;
    }
}