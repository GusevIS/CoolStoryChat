import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket clientSocket;
    private static BufferedWriter out;

    public ServerThread(Socket clientSocket){
        this.clientSocket = clientSocket;
        start();
    }

    @Override
    public void run() {
        try {
            serve(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Main.semaphore.release();
        }
    }

    private static void serve(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        while (true) {
            String msg = in.readLine();
            System.out.println(msg);
            if(msg.equals("Log out"))
                break;

            for(ServerThread thread: Main.serverThreadList)
                thread.sendMsg(msg);
        }
    }

    private void sendMsg(String msg) throws IOException {
        out.write(msg + "\n");
        out.flush();
    }
}
