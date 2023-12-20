package sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerMessenger {

    private boolean isHandshakeDone;
    private Socket tcpSocket;
    private PrintWriter dataSender;
    private BufferedReader dataReceiver;

    public ServerMessenger() {
        try {
            this.tcpSocket = new Socket("127.0.0.1", 1236);
            this.dataSender = new PrintWriter(tcpSocket.getOutputStream(), true);
            this.dataReceiver = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Gateway connection error");
            System.exit(1);
        }
    }

    public void sendMessageToServer(String message) {
        if (this.isHandshakeDone) {
            dataSender.println(message);
            System.out.println("Sent to server from gateway: " + message);
        } else {
            System.out.println("Data can only be sent after connection is established with handshake! Establishing connection now...");
            establishConnection();
        }
    }

    public void establishConnection() {
        try {
            // Sending INITIALIZE message to server
            dataSender.println("INITIALIZE");
            System.out.println("Sent 'INITIALIZE' to server to start handshake.");

            // Waiting for the server's response
            String response = dataReceiver.readLine();
            if ("ACKNOWLEDGEMENT".equals(response)) {
                // If the response is ACKNOWLEDGEMENT, handshake is done
                isHandshakeDone = true;
                System.out.println("Received 'ACKNOWLEDGEMENT' from server, handshake completed");
            } else {
                System.out.println("Handshake failed, response received: " + response);
            }
        } catch (IOException e) {
            System.out.println("Error during connection establishment: " + e.getMessage());
        }
    }

    public boolean getIsHandshakeDone() {
        return this.isHandshakeDone;
    }

    public void setIsHandshakeDone(boolean isHandshakeDone) {
        this.isHandshakeDone = isHandshakeDone;
    }
}
