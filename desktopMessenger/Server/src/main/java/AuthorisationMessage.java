import java.io.Serializable;

public class AuthorisationMessage implements Serializable {
    private String username;
    private String password;
    private MessageFlag flag;

    public AuthorisationMessage(String username, String password, MessageFlag flag){
        this.username = username;
        this.password = password;
        this.flag = flag;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public MessageFlag getFlag(){
        return flag;
    }

    public void setFlag(MessageFlag flag){
        this.flag = flag;
    }
}