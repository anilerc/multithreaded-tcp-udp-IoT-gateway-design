import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class GatewayListener implements Runnable {

    @Override
    public void run() {
        try {

            ServerSocket serverSocket = new ServerSocket(1236);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                var inputStream = clientSocket.getInputStream();
                var outputStream = clientSocket.getOutputStream();

                var reader = new InputStreamReader(inputStream);
                var writer = new PrintWriter(outputStream, true);

                BufferedReader bufferedReader = new BufferedReader(reader);

                String inputLine;
                boolean handshakeEstablished = false;

                while ((inputLine = bufferedReader.readLine()) != null) {
                    if ("INITIALIZE".equals(inputLine)) {
                        System.out.println("Received INITIALIZE. Sending ACKNOWLEDGEMENT...");
                        writer.println("ACKNOWLEDGEMENT");
                        handshakeEstablished = true;
                        System.out.println("ACK send to gateway. Handshake is done. Server ready to accept data.");
                    } else if (handshakeEstablished) {
                        System.out.println("SERVER: Data received. Parsing and processing now.");
                        parsePayload(inputLine);
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parsePayload(String payload) {
        String[] tokens = payload.split("\\|");
        String type = tokens[1];
        String data = tokens[2];
        String timestamp = tokens[3];

        switch (tokens[0]) {
            case "VALUE":
                int parsedData = Integer.parseInt(data);
                String valueString = "Value: " + parsedData + " - at timestamp: " + timestamp;
                if ("HUMIDITY".equals(type)) {
                    Main.humidityValues.add(valueString);
                } else if ("TEMPERATURE".equals(type)) {
                    Main.temperatureValues.add(valueString);
                }
                break;

            case "HEALTHCHECK":
                String healthCheckString = "Health: " + data + " - at timestamp: " + timestamp;
                if ("HUMIDITY".equals(type)) {
                    Main.humidityHealthChecks.add(healthCheckString);
                } else if ("TEMPERATURE".equals(type)) {
                    Main.temperatureHealthChecks.add(healthCheckString);
                }
                break;
        }
    }

}
