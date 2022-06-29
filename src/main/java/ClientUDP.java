import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ClientUDP {
    private DatagramSocket socket;
    private byte[] buffer = new byte[256];
    private int port;

    ClientUDP(int port) throws SocketException {
        socket = new DatagramSocket();
        this.port = port;
    }

    public void sendMessage(String msg) throws IOException {
        buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        packet.setAddress(InetAddress.getByName("localhost"));
        packet.setPort(port);
        socket.send(packet);
    }

    public String getMessage() throws IOException {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void stop() throws IOException {
        sendMessage("end");
        socket.close();
    }
}
