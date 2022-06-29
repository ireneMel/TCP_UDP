import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UDPTest {
    private ExecutorService executors = Executors.newFixedThreadPool(5);
    private int port = 6667;

//    @BeforeEach
//    public void init() {
//
//    }

    @Test
    public void echoTest() throws Exception {
        ServerUDP serverUDP = new ServerUDP(port);
        executors.execute(serverUDP);

        ClientUDP clientUDP = new ClientUDP(port);
        clientUDP.sendMessage("first");
        String response = clientUDP.getMessage();

        Assertions.assertEquals("first", response);

        clientUDP.stop();
    }

    @Test
    public void multiEchoTest() throws Exception {
        ServerUDP serverUDP = new ServerUDP(port);
        executors.execute(serverUDP);
        List<Future<String>> list = new ArrayList<>();

        for (int i = 499; i >= 0; i--) {
            Integer finalI = i;
            list.add(executors.submit(() -> client(finalI)));
        }

        for (int i = 0; i <= 499; i++) {
            Assertions.assertEquals(Integer.toString(499 - i), list.get(i).get());
        }

        new ClientUDP(port).stop();
    }

    public String client(Integer i) {
        try {
            ClientUDP clientUDP = new ClientUDP(port);
            clientUDP.sendMessage(i.toString());
            return clientUDP.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
