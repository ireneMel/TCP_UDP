import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tcp.ClientTCP;
import tcp.ServerTCP;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TCPTest {
    public static Logger log = Logger.getLogger(TCPTest.class.getName());
    private static int port = 6666;
    private static ServerTCP serverTCP;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @BeforeEach
    public void init() throws IOException {
        serverTCP = new ServerTCP(port);
        Thread serverThread = new Thread(serverTCP);
        serverThread.start();
    }

    @Test
    public void initialTest() throws IOException {
        ClientTCP clientTCP = new ClientTCP();
        clientTCP.start(port);

        clientTCP.sendMessage("a");
        clientTCP.sendMessage("b");
        clientTCP.sendMessage("c");
        clientTCP.sendMessage("end");

        assertEquals("a", clientTCP.getMessage());
        assertEquals("b", clientTCP.getMessage());
        assertEquals("c", clientTCP.getMessage());

//        Assertions.assertEquals("message from client", clientTCP.getMessage());
//        clientTCP.stop();
    }

    @Test
    public void sequentTest() throws IOException {
        ClientTCP clientTCP = new ClientTCP();
        clientTCP.start(port);

        clientTCP.sendMessage("a");
        assertEquals("a", clientTCP.getMessage());
        clientTCP.sendMessage("b");
        assertEquals("b", clientTCP.getMessage());
        clientTCP.sendMessage("c");
        assertEquals("c", clientTCP.getMessage());
        clientTCP.stop();
    }

    @Test
    public void oneMessageTest() throws IOException {
        ClientTCP clientTCP = new ClientTCP();
        clientTCP.start(port);

        clientTCP.sendMessage("a");
        assertEquals("a", clientTCP.getMessage());
        clientTCP.stop();
    }

    @Test
    public void partSendingMessageTest() throws IOException, ExecutionException, InterruptedException {
        ClientTCP clientTCP = new ClientTCP();
        clientTCP.start(port);

        Future<String> result = executorService.submit(() -> {
            try {
                return clientTCP.getMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        clientTCP.sendOneMessage("a");
        clientTCP.sendOneMessage("b");
        clientTCP.sendOneMessage("c");
        assertEquals("abc", result.get());
        clientTCP.stop();
    }

    @AfterEach
    public void shutdown() throws IOException {
        serverTCP.stop();
    }
}
