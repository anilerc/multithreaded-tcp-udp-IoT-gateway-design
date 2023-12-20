import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Random;

public class ValueGeneration implements Runnable {
    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket();

            InetAddress gatewayIpAddress = InetAddress.getByName("127.0.0.1");

            while (true) {
                Random random = new Random();
                int randomHumidityValue = 40 + random.nextInt(51); // between 40 and 90

                if (randomHumidityValue > 80) {
                    var timestamp = LocalDateTime.now().toString();

                    byte[] bytesToSend = getPayload(new String[]{"VALUE", "HUMIDITY", Integer.toString(randomHumidityValue), timestamp});

                    DatagramPacket packetToSend = new DatagramPacket(bytesToSend, bytesToSend.length, gatewayIpAddress,
                            1234);

                    udpSocket.send(packetToSend);
                }

                Thread.sleep(1000);
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
