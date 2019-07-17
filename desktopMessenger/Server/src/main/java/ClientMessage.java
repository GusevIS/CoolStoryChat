import java.io.Serializable;

public class ClientMessage implements Serializable {
    private String username;
    private String message;
    private MessageFlag flag;

    public ClientMessage(String username, String message, MessageFlag flag){
        this.username = username;
        this.message = message;
        this.flag = flag;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String password){
        this.message = password;
    }

    public MessageFlag getFlag(){
        return flag;
    }

    public void setFlag(MessageFlag flag){
        this.flag = flag;
    }
}