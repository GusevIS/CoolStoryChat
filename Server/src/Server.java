import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    public static final int PORT = 4999;
    public static LinkedList<ServerThread> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    System.out.println("client connected");
                    serverList.add(new ServerThread(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    public static void removeServerThread(ServerThread thread){
        serverList.remove(thread);
    }
}