import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GatewayListener implements Runnable {

    // This thread will be responsible from listening to the gateway and updating
    // the database.

    private final WebServer webServer;
    private boolean handshakeEstablished;

    public GatewayListener(WebServer webServer) {
        this.webServer = webServer;
        this.handshakeEstablished = false;
    }

    // Constantly listen to the gateway over TCP and the custom defined protocol.
    @Override
    public void run() {
        try {
            System.out.println("Gateway listener is running.");
            ServerSocket serverSocket = new ServerSocket(1236);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                var inputStream = clientSocket.getInputStream();
                var outputStream = clientSocket.getOutputStream();

                var reader = new InputStreamReader(inputStream);
                var writer = new PrintWriter(outputStream, true);

                BufferedReader bufferedReader = new BufferedReader(reader);

                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null) {
                    processIncomingData(inputLine, writer);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Upon receiving a new data from the gateway, establish handshake or
    // process&parse the data if connection is already done
    private void processIncomingData(String input, PrintWriter writer) {
        if ("INITIALIZE".equals(input)) {
            System.out.println("Received INITIALIZE. Sending ACKNOWLEDGEMENT...");
            writer.println("ACKNOWLEDGEMENT");
            this.handshakeEstablished = true;
            System.out.println("ACK sent to gateway. Handshake is done. Server ready to accept data.");
        } else if (handshakeEstablished) {
            Helper.logOperation(Thread.currentThread(), input.getBytes());
            parsePayload(input);
        }
    }

    // Parse the payload to understand whether if the incoming data is a health
    // check or new value update. Then, add the new data to the database.
    // The updated data will be displayed by the web server in the form of an HTML
    // file over localhost HTTP.
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
                    webServer.getHumidityValues().add(valueString);
                } else if ("TEMPERATURE".equals(type)) {
                    webServer.getTemperatureValues().add(valueString);
                }
                break;

            case "HEALTHCHECK":
                String healthCheckString = "Health: " + data + " - at timestamp: " + timestamp;
                if ("HUMIDITY".equals(type)) {
                    webServer.getHumidityHealthChecks().add(healthCheckString);
                } else if ("TEMPERATURE".equals(type)) {
                    webServer.getTemperatureHealthChecks().add(healthCheckString);
                }
                break;
        }
    }

}
