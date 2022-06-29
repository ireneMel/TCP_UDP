import org.junit.jupiter.api.*;
import tcp.ClientTCP;
import tcp.ServerTCP;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiTCPTest {
    private static int port = 6666;
    private static ServerTCP serverTCP;

    @BeforeAll
    public static void init() throws IOException {
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
//        clientTCP.sendMessage("end");

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

    @AfterAll
    public static void shutdown() throws IOException {
        serverTCP.stop();
    }
}
