import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Main {
    public static final int MAX_CLIENTS_COUNT = 20;
    public static final int PORT = 1000;
    public static Semaphore semaphore;

    public static LinkedList<ServerThread> serverThreadList;

    public static void main(String ... args) throws IOException, InterruptedException {
        semaphore = new Semaphore(MAX_CLIENTS_COUNT);
        serverThreadList = new LinkedList<>();

        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            semaphore.acquire();
            Socket clientSocket = serverSocket.accept();
            serverThreadList.add(new ServerThread(clientSocket));
            System.out.println("Client connected");
        }
    }
}
