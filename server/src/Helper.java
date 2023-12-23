import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {

    public static void logOperation(Thread thread, byte[] payload) {
        var message = new String(payload);
        var log = thread.getName() + " thread received " +  message;
        System.out.println(log);
        appendLog(log);
    }

    public static void appendLog(String text) {
        String filePath = "server-logs.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(text + " was not written to the log file due to an exception");
        }
    }
}
