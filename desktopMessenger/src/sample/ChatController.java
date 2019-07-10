package sample;

import java.io.IOException;
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
    void initialize() {
        sendButton.setOnAction(event -> {
            //Send message to server
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


    }
}