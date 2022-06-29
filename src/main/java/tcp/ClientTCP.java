package tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTCP  {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        socket = new Socket("127.0.0.1", port);

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void sendOneMessage(String msg) {
        out.print(msg);
    }

    public String getMessage() throws IOException {
        String msg = in.readLine();
        return msg;
    }

    public void stop() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}
