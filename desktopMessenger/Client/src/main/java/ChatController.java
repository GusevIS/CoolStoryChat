import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChatController {
    private final Gson gson = new GsonBuilder().create();
    private static String clientName;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea outputMsgArea;

    @FXML
    private TextArea inputMsgArea;

    @FXML
    private Button LogOutButton;

    @FXML
    void initialize(){
        inputMsgArea.setWrapText(true);
        outputMsgArea.setWrapText(true);
        outputMsgArea.setEditable(false);

        sendButton.setOnAction(event -> {
            String msg = inputMsgArea.getText().trim();
            if(!msg.isEmpty()){
                try {
                    String authMsg = gson.toJson(new ClientMessage(getClientName(), msg, MessageFlag.DEFAULT_MESSAGE));
                    AuthorisationController.getOut().write(authMsg + "\n");
                    AuthorisationController.getOut().flush();
                    inputMsgArea.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        LogOutButton.setOnAction(event -> {
            try {
                System.out.println("log out from chat");
                String authMsg = gson.toJson(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_CHAT));
                AuthorisationController.getOut().write(authMsg + "\n");
                AuthorisationController.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LogOutButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/authorisationForm.fxml"));

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
            String msg;
            try {
                while (AuthorisationController.getClientIsConnectedToChat()) {
                    msg = AuthorisationController.getIn().readLine();
                    ClientMessage messageFromServer = gson.fromJson(msg, ClientMessage.class);

                    switch (messageFromServer.getFlag()){
                        case DEFAULT_MESSAGE:
                            final String textOutputMsgArea = messageFromServer.getUsername() + ">> " + messageFromServer.getMessage();
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
            } catch (IOException e) {
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