import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    private final int PORT = 4990;
    private LinkedList<ServerThread> serverList;

    public Server(){
        serverList = new LinkedList<>();
    }

    public void startServer() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    System.out.println("client connected");
                    serverList.add(new ServerThread(this, socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    public synchronized void sendToEveryone(String message){
        for (ServerThread thread: serverList) {
            thread.sendMessage(message);
        }
    }

    public LinkedList<ServerThread> getServerList(){
        return serverList;
    }

    public synchronized void removeServerThread(ServerThread thread){
        serverList.remove(thread);
    }
}