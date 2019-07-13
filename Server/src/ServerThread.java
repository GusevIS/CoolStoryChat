import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    public final String REMOVE_THREAD_COMMAND = "/Log out";
    public final String STOP_CLIENT_COMMAND = "/Stop";
    private static BufferedWriter output;
    private static BufferedReader input;

    public ServerThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String msg;
        try {
            while (true) {
                msg = input.readLine();
                System.out.println(msg);
                if(msg.equals(REMOVE_THREAD_COMMAND)) {
                    sendMsg(STOP_CLIENT_COMMAND);
                    Main.removeServerThread(this);
                    break;
                }

                for(ServerThread thread: Main.serverThreadList)
                    thread.sendMsg(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Main.semaphore.release();
        }
    }

    private void serve() throws IOException {
        while (true) {
            String msg = input.readLine();
            System.out.println(msg);
            if(msg.equals(REMOVE_THREAD_COMMAND)) {
                sendMsg(STOP_CLIENT_COMMAND);
                Main.removeServerThread(this);
                break;
            }

            for(ServerThread thread: Main.serverThreadList)
                thread.sendMsg(msg);
        }
    }

    private void sendMsg(String msg){
        try {
            output.write(msg + "\n");
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
