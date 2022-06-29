package tcp;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP implements Runnable {
    public static Logger log = Logger.getLogger(ServerTCP.class.getName());

    private ServerSocket serverSocket;
    ExecutorService executors = Executors.newFixedThreadPool(3);

    public ServerTCP(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    @Override
    public void run() {
//        try {
        while (true) {
            executors.execute(new ClientHandler(serverSocket.accept()));
        }
//        } finally {
//            stop();
//        }
    }

    public void stop() throws IOException {
        serverSocket.close();
    }
}
