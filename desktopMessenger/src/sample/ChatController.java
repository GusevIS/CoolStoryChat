package sample;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class ChatController {

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
    void initialize() {
        sendButton.setOnAction(event -> {
            //Send message to server
        });

        smileChoiceButton.setOnAction(event -> {
            //Choose smile
        });


    }
}