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
        try {
            ServerSocket serverSocket = new ServerSocket(1235);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                var inputStream = clientSocket.getInputStream();

                var reader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(reader);

                String inputLine;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    getServerMessenger().sendMessageToServer(inputLine);
                    setLastReceivedTimestamp(System.currentTimeMillis());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
