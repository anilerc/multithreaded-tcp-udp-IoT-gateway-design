package listener;

import sender.ServerMessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TemperatureListener extends Listener {

    public TemperatureListener(ServerMessenger serverMessenger) {
        super(serverMessenger);
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(1235);
        } catch (IOException e) {
            System.out.println("Error creating server socket.");
            return;
        }

        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 InputStreamReader inputStream = new InputStreamReader(clientSocket.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStream)) {

                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    getServerMessenger().sendMessageToServer(inputLine);
                    setLastReceivedTimestamp(System.currentTimeMillis());
                }

            } catch (IOException e) {
                System.out.println("Connection lost to temperature sensor.");
            }
        }
    }
}
