package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {

    // Enum defining the types of operations a thread can perform: SENDING or
    // RECEIVING
    public enum OperationType {
        SENDING,
        RECEIVING
    }

    // * Logs the operation to console and to the file (send/receive) performed by a
    // thread.
    public static void logOperation(Thread thread, byte[] payload, OperationType operationType) {
        var message = new String(payload);
        String operation = switch (operationType) {
            case SENDING -> "sent";
            case RECEIVING -> "received";
        };

        var log = thread.getName() + " thread " + operation + " " + message;
        System.out.println(log);
        appendLog(log);
    }

    // * Appends a given text to the log file.
    public static void appendLog(String text) {
        String filePath = "gateway-logs.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.out.println(text + " was not written to the log file due to an exception");
        }
    }
}
