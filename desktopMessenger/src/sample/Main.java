package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("authorisationForm.fxml"));
        primaryStage.setTitle("CoolStoryChat");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    @Override
    public void stop(){
        if(ChatController.out != null){
            try {
                ChatController.out.write(ChatController.REMOVE_THREAD_COMMAND + "\n");
                ChatController.out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
