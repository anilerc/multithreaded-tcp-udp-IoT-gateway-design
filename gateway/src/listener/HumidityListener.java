package listener;

import helper.Helper;
import sender.ServerMessenger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class HumidityListener extends Listener {

    public HumidityListener(ServerMessenger serverMessenger) {
        super(serverMessenger);
    }

    // Listening to the humidity sensor over a constant UDP connection:
    @Override
    public void run() {
        try {
            DatagramSocket udpSocket = new DatagramSocket(1234);
            byte[] receivedData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
                udpSocket.receive(receivePacket);
                setLastReceivedTimestamp(System.currentTimeMillis()); // Setting the "lastReceived" timestamp to later
                                                                      // use for periodic health checks
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Helper.logOperation(Thread.currentThread(), message.getBytes(), Helper.OperationType.SENDING);
                if (!message.split("\\|")[0].equals("HEALTHCHECK"))
                    getServerMessenger().sendMessageToServer(message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
