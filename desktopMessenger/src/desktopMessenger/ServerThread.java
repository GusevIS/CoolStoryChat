package desktopMessenger;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

class ServerThread extends Thread {
    private DatabaseHandler dbHandler;

    private Socket socket;
    private boolean clientIsConnectedToChat;
    private boolean clientIsActive;
//    private BufferedReader in;
//    private BufferedWriter out;
    private ObjectInputStream in;
    private ObjectOutputStream out;
//    public final String REMOVE_THREAD_COMMAND = "/Log out";
//    public final String STOP_CLIENT_COMMAND = "/Stop";

    public ServerThread(Socket socket) throws IOException {
        dbHandler = new DatabaseHandler();
        this.socket = socket;
        clientIsActive = true;
        clientIsConnectedToChat = false;
//        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        start();
    }

    @Override
    public void run() {
        AuthorisationMessage authMessage;
        ClientMessage clientMessage;
        try {
            while(clientIsActive) {
                if (!clientIsConnectedToChat) {
                    //подключение к чату
                    authMessage = (AuthorisationMessage) in.readObject();
                    switch (authMessage.flag){
                        case SIGN_IN:
                            if(getUser(authMessage.username, authMessage.password)) {
                                out.writeObject(new AuthorisationMessage(authMessage.username, "", MessageFlag.SUCCESSFUL_SIGN_IN));
                                out.flush();
                                clientIsConnectedToChat = true;
                                System.out.println("Client enter to the chat");
                            }else {
                                out.writeObject(new AuthorisationMessage("", "", MessageFlag.FAILED_SIGN_IN));
                                out.flush();
                            }
                            break;

                        case SIGN_UP:
                            if(userExists(authMessage.username)) {
                                out.writeObject(new AuthorisationMessage("", "", MessageFlag.FAILED_SIGN_UP));
                                out.flush();
                            }
                            else {
                                dbHandler.signUpUser(authMessage.username, authMessage.password);
                                out.writeObject(new AuthorisationMessage("", "", MessageFlag.SUCCESSFUL_SIGN_UP));
                                out.flush();
                            }
                            break;
                    }
                } else {
                    //работа в чате
                    clientMessage = (ClientMessage) in.readObject();
                    switch (clientMessage.flag){
                        case DEFAULT_MESSAGE:
                            System.out.println(clientMessage.username + ">> " + clientMessage.message);
                            for(ServerThread thread : Server.getServerList()){
                                thread.sendMessage(clientMessage);
                            }
                            break;

                        case LOG_OUT_FROM_SERVER:
                            clientIsActive = false;
                            Server.removeServerThread(this);
                            out.writeObject(clientMessage);
                            out.flush();
                            break;

                        case LOG_OUT_FROM_CHAT:
                            clientIsConnectedToChat = false;
                            out.writeObject(clientMessage);
                            out.flush();
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        }
    }

    private void sendMessage(ClientMessage message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException ignored) {}
    }

    private boolean getUser(String loginName, String loginPass) {
        ResultSet result = dbHandler.getUser(loginName, loginPass);
        if(result == null) {
            clientIsActive = false;
            Server.removeServerThread(this);
            try {
                out.writeObject(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_SERVER));
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
                Server.removeServerThread(this);
                try {
                    out.writeObject(new ClientMessage("", "", MessageFlag.LOG_OUT_FROM_SERVER));
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