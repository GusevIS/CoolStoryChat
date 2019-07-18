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
    private Client client;

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
        client = Main.getClient();
        inputMsgArea.setWrapText(true);
        outputMsgArea.setWrapText(true);
        outputMsgArea.setEditable(false);

        sendButton.setOnAction(event -> {
            String msg = inputMsgArea.getText().trim();
            if(!msg.isEmpty()){
                client.sendMessage(msg);
                inputMsgArea.setText("");
            }
        });

        LogOutButton.setOnAction(event -> {
            client.logOutFromChat();
            showAuthorisation();
        });

        getChatConnection();
    }

    private void getChatConnection(){
        new Thread(() -> {
            String msg;
            try {
                while (client.isConnectedToChat()) {
                    msg = client.getIn().readLine();
                    ClientMessage messageFromServer = client.getGSON().fromJson(msg, ClientMessage.class);

                    switch (messageFromServer.getFlag()){
                        case DEFAULT_MESSAGE:
                            final String textOutputMsgArea = messageFromServer.getUsername() + ">> " + messageFromServer.getMessage();
                            Platform.runLater(() -> outputMsgArea.appendText(textOutputMsgArea + "\n"));
                            break;

                        case LOG_OUT_FROM_CHAT:
                            client.setConnectedToChat(false);
                            break;

                        case LOG_OUT_FROM_SERVER:
                            client.setConnectedToChat(false);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void showAuthorisation(){
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
    }
}