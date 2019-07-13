import java.io.*;
import java.net.Socket;

class ServerThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    public final String REMOVE_THREAD_COMMAND = "/Log out";
    public final String STOP_CLIENT_COMMAND = "/Stop";

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }
    @Override
    public void run() {
        String msg;
        try {

            while (true) {
                msg = in.readLine();
                System.out.println(msg);
                if(msg.equals(REMOVE_THREAD_COMMAND)) {
                    send(STOP_CLIENT_COMMAND);
                    Server.removeServerThread(this);
                    break;
                }
                for (ServerThread client : Server.serverList) {
                    client.send(msg);
                }
            }
        } catch (IOException e) {
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}