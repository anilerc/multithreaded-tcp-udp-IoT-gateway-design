import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {
    public static void logOperation(String message) {
        System.out.println("Temperature sensor sent " + message + " to the gateway.");
        appendLog(message);
    }

    public static void appendLog(String text) {
        String filePath = "temperature-logs.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(text + " was not written to the log file due to an exception");
        }
    }
}
