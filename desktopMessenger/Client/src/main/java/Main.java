import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static final Gson gson = new GsonBuilder().create();
    private static Client client;

    @Override
    public void start(Stage primaryStage) throws Exception{
        client = new Client();
        client.getServerConnection();
        Parent root = FXMLLoader.load(getClass().getResource("/authorisationForm.fxml"));
        primaryStage.setTitle("CoolStoryChat");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    @Override
    public void stop(){
        if(client.getOut() != null){
            try {
                String msg = gson.toJson(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_SERVER));
                client.getOut().write(msg + "\n");
                client.getOut().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Client getClient(){
        return client;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
