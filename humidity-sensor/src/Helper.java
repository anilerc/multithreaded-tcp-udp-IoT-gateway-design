import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {

    // General helper methods

    public static byte[] buildPayload(String[] tokens) {
        String separator = "|";
        String str = String.join(separator, tokens);
        return str.getBytes();
    }

    public static void logOperation(Thread thread, byte[] sentPayload) {
        var message = new String(sentPayload);
        System.out.println(thread.getName() + " thread from humidity sensor sent " + message + " to the gateway.");
        appendLog(message);
    }

    public static void appendLog(String text) {
        String filePath = "humidity-logs.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(text + " was not written to the log file due to an exception");
        }
    }
}
