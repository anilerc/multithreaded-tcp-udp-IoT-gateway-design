import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;


public class HealthCheck implements Runnable {
    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            InetAddress gatewayIpAddress = InetAddress.getByName("127.0.0.1");

            while (true) {
                byte[] payload = getPayload(new String[]{"HEALTHCHECK","HUMIDITY","UP", LocalDateTime.now().toString()});
                DatagramPacket packetToSend = new DatagramPacket(payload, payload.length, gatewayIpAddress, 1234);
                udpSocket.send(packetToSend);

                Thread.sleep(3000);
            }
        } catch (IOException e) {
            throw new RuntimeException("Connection failure!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } 
    }

    public static byte[] getPayload(String[] tokens) {
        String separator = "|";
        String str = String.join(separator, tokens);

        return str.getBytes();
    }
}
