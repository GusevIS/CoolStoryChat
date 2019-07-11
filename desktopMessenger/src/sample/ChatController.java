package sample;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChatController {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

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
    private ChoiceBox<?> smileChoiceButton;


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
                    out.write(msg + "\n");
                    out.flush();
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

    private void getServerConnection() throws IOException {
        clientSocket = new Socket("localhost", 1000);

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        new Thread(() -> {
            String msgFromServer;
            try {
                while (true) {
                    msgFromServer = in.readLine();
                    if (msgFromServer.equals("stop"))
                        break;
                    outputMsgArea.setText(msgFromServer + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}