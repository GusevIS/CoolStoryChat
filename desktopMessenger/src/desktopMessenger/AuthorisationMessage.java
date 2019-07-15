package desktopMessenger;

import java.io.Serializable;

public class AuthorisationMessage implements Serializable {
    public String username;
    public String password;
    public MessageFlag flag;

    public AuthorisationMessage(String username, String password, MessageFlag flag){
        this.username = username;
        this.password = password;
        this.flag = flag;
    }
}
