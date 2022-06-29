import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerUDP implements Runnable {
    private DatagramSocket socket;
    private boolean isRunning;

    ServerUDP(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }
    final int size = 8;
    ExecutorService executorService = Executors.newFixedThreadPool(size);

    final byte[][] buffers = new byte[size][256];
    final BlockingQueue<Integer> free = new LinkedBlockingQueue<>();

    @SneakyThrows
    @Override
    public void run() {
        isRunning = true;
        for(int i = 0;i < size; ++i)free.put(i);

        while (isRunning) {
            executorService.execute(() -> {
                try {
                    int id = free.take();

                    byte[] buffer = buffers[id];

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();

                    packet = new DatagramPacket(buffer, buffer.length, address, port);
                    String data = new String(packet.getData(), 0, packet.getLength());

                    if (data.equals("end")) {
                        isRunning = false;
                    } else {
                        socket.send(packet);
                    }

                    free.put(id);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @SneakyThrows
    private void handle() {

    }
}
