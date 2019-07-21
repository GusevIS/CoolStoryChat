import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;

public class Client{
    private boolean connectedToChat;
    private Socket clientSocket;
    private String clientName;
    private BufferedReader in;
    private BufferedWriter out;
    private final Gson GSON = new GsonBuilder().create();

    public Client(){
        connectedToChat = false;
    }

    public void getServerConnection() throws IOException {
        clientSocket = new Socket("localhost", 4990);
        setOut(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
        setIn(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
    }

    public MessageFlag signIn(String loginName, String loginPass){
        AuthorisationMessage authMessage = null;
        try {
            String authMsg = GSON.toJson(new AuthorisationMessage(loginName, loginPass, MessageFlag.SIGN_IN));
            out.write(authMsg + "\n");
            out.flush();

            String msg;
            msg = in.readLine();
            authMessage = GSON.fromJson(msg, AuthorisationMessage.class);
            if (authMessage.getFlag() == MessageFlag.SUCCESSFUL_SIGN_IN) {
                connectedToChat = true;
                clientName = authMessage.getUsername();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authMessage.getFlag();
    }

    public MessageFlag signUp(String loginName, String loginPass){
        AuthorisationMessage authMessage = null;
        try {
            String authMsg = GSON.toJson(new AuthorisationMessage(loginName, loginPass, MessageFlag.SIGN_UP));
            out.write(authMsg + "\n");
            out.flush();

            String msg;
            msg = in.readLine();
            authMessage = getGSON().fromJson(msg, AuthorisationMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authMessage.getFlag();
    }

    public void sendMessage(String message){
        try {
            String msg = GSON.toJson(new ClientMessage(clientName, message, MessageFlag.DEFAULT_MESSAGE));
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOutFromChat(){
        try {
            System.out.println("log out from chat");
            String authMsg = GSON.toJson(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_CHAT));
            out.write(authMsg + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnectedToChat() {
        return connectedToChat;
    }

    public void setConnectedToChat(boolean connectedToChat) {
        this.connectedToChat = connectedToChat;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    public Gson getGSON() {
        return GSON;
    }
}
