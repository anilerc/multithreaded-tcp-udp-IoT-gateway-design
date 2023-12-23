package sender;

import helper.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerMessenger {

    private boolean isHandshakeDone; // Flag is set to 1 after ACKNOWLEDGEMENT
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

    // Sends message to the server ONLY after the handshake is completed. Otherwise,
    // it will try to establish a connection.
    public void sendMessageToServer(String message) {
        if (this.isHandshakeDone) {
            dataSender.println(message);
            Helper.logOperation(Thread.currentThread(), message.getBytes(), Helper.OperationType.SENDING);
            System.out.println("Gateway sent " + message + " to the server.");
        } else {
            System.out.println(
                    "Data can only be sent after connection is established with handshake! Establishing connection now...");
            establishConnection();
        }
    }

    // Sends "INITIALIZE" to server and waits for an "ACKNOWLEDGEMENT" response.
    // After this point, handshake is completed and the flag is set to 1.
    // From now on, data can be send.
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
}
