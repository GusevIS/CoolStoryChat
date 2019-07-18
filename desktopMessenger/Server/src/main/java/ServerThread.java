import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.google.gson.*;

class ServerThread extends Thread {
    private DatabaseHandler dbHandler;
    private Server server;
    private final Gson GSON = new GsonBuilder().create();
    private boolean clientIsConnectedToChat;
    private boolean clientIsActive;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerThread(Server server, Socket socket) throws IOException {
        this.server = server;
        dbHandler = new DatabaseHandler();
        clientIsActive = true;
        clientIsConnectedToChat = false;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        AuthorisationMessage authMessage;
        ClientMessage clientMessage;
        String msg;
        try {
            while(clientIsActive) {
                if (!clientIsConnectedToChat) {
                    msg = in.readLine();
                    authMessage = GSON.fromJson(msg, AuthorisationMessage.class);
                    switch (authMessage.getFlag()){
                        case SIGN_IN:
                            if(getUser(authMessage.getUsername(), authMessage.getPassword())) {
                                String authMsg = GSON.toJson(new AuthorisationMessage(authMessage.getUsername(), "", MessageFlag.SUCCESSFUL_SIGN_IN));
                                out.write(authMsg + "\n");
                                out.flush();
                                clientIsConnectedToChat = true;
                                System.out.println("Client enter to the chat");
                            }else {
                                String authMsg = GSON.toJson(new AuthorisationMessage("", "", MessageFlag.FAILED_SIGN_IN));
                                out.write(authMsg + "\n");
                                out.flush();
                            }
                            break;

                        case SIGN_UP:
                            if(userExists(authMessage.getUsername())) {
                                String authMsg = GSON.toJson(new AuthorisationMessage("", "", MessageFlag.FAILED_SIGN_UP));
                                out.write(authMsg + "\n");
                                out.flush();
                            }
                            else {
                                dbHandler.signUpUser(authMessage.getUsername(), authMessage.getPassword());
                                String authMsg = GSON.toJson(new AuthorisationMessage("", "", MessageFlag.SUCCESSFUL_SIGN_UP));
                                out.write(authMsg + "\n");
                                out.flush();
                            }
                            break;
                    }
                } else {
                    msg = in.readLine();
                    clientMessage = GSON.fromJson(msg, ClientMessage.class);
                    switch (clientMessage.getFlag()){
                        case DEFAULT_MESSAGE:
                            System.out.println(clientMessage.getUsername() + ">> " + clientMessage.getMessage());
                            server.sendToEveryone(msg);
                            break;

                        case LOG_OUT_FROM_SERVER:
                            clientIsActive = false;
                            server.removeServerThread(this);

                            out.write(msg + "\n");
                            out.flush();
                            break;

                        case LOG_OUT_FROM_CHAT:
                            clientIsConnectedToChat = false;
                            out.write(msg + "\n");
                            out.flush();
                            break;
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    public void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private boolean getUser(String loginName, String loginPass) {
        ResultSet result = dbHandler.getUser(loginName, loginPass);
        if(result == null) {
            clientIsActive = false;
            server.removeServerThread(this);
            try {
                String authMsg = GSON.toJson(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_SERVER));
                out.write(authMsg + "\n");
                out.flush();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int counter = 0;
        try {
            while (result.next()) {
                counter++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return (counter != 0);
    }

    private boolean userExists(String loginName) {
        ResultSet result = dbHandler.getUser(loginName);
        if(result == null) {
            if(result == null) {
                clientIsActive = false;
                server.removeServerThread(this);
                try {
                    String authMsg = GSON.toJson(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_SERVER));
                    out.write(authMsg + "\n");
                    out.flush();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        int counter = 0;
        try {
            while (result.next()) {
                counter++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return (counter != 0);
    }
}