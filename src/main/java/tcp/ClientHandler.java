package tcp;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
        client = socket;
    }

    @SneakyThrows
    @Override
    public void run() {

        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        String tmp;

        while (!Objects.equals(tmp = in.readLine(), "end")) {
            out.println(tmp);
        }

        stop();
    }

    public void stop() throws IOException {
        out.close();
        in.close();
        client.close();
    }
}
