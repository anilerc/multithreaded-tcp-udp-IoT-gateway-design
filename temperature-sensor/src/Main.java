import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // Temperature sensor generated new values every second and sends to gateway
        // Uses TCP socket programming
        try {
            Socket tcpSocket = new Socket("127.0.0.1", 1235);

            PrintWriter dataSender = new PrintWriter(tcpSocket.getOutputStream(), true);

            while (true) {
                Random random = new Random();
                int randomTemperatureValue = 20 + random.nextInt(11);
                var timestamp = LocalDateTime.now().toString();

                var payload = getPayload(
                        new String[] { "VALUE", "TEMPERATURE", Integer.toString(randomTemperatureValue), timestamp });
                dataSender.println(payload);
                Helper.logOperation(payload);
                Thread.sleep(1000);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getPayload(String[] tokens) {
        String separator = "|";
        return String.join(separator, tokens);

    }
}