import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Main {
    public static final int MAX_CLIENTS_COUNT = 20;
    public static final int PORT = 5000;
    public static Semaphore semaphore;

    public static LinkedList<ServerThread> serverThreadList = new LinkedList<>();

    public static void main(String ... args) throws IOException, InterruptedException {
        System.out.println("server started");
        semaphore = new Semaphore(MAX_CLIENTS_COUNT);

        ServerSocket serverSocket = new ServerSocket(PORT);
        try {
            while (true) {
                semaphore.acquire();
                Socket clientSocket = serverSocket.accept();
                try {
                    serverThreadList.add(new ServerThread(clientSocket));
                    System.out.println("Client connected");
                }catch (IOException e){
                    clientSocket.close();
                }
            }
        }finally {
            serverSocket.close();
        }
    }

    public static void removeServerThread(ServerThread thread){
        serverThreadList.remove(thread);
    }
}
