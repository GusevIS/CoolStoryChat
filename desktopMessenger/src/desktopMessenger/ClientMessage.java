package desktopMessenger;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    public String username;
    public String message;
    public MessageFlag flag;

    public ClientMessage(String username, String message, MessageFlag flag){
        this.username = username;
        this.message = message;
        this.flag = flag;
    }
}
