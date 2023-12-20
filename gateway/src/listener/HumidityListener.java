package listener;

import sender.ServerMessenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class HumidityListener extends Listener {

    public HumidityListener(ServerMessenger serverMessenger) {
        super(serverMessenger);
    }

    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(1234);
            byte[] receivedData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
                udpSocket.receive(receivePacket);
                setLastReceivedTimestamp(System.currentTimeMillis());
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (!message.split("\\|")[0].equals("HEALTHCHECK"))
                    getServerMessenger().sendMessageToServer(message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
