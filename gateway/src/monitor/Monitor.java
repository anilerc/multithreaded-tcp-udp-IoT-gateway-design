package monitor;

import listener.Listener;
import sender.ServerMessenger;

import java.time.LocalDateTime;

public class Monitor implements Runnable {
    private final String name;
    private final ServerMessenger serverMessenger;
    private final long sleepTime;
    private final Listener listener;

    public Monitor(ServerMessenger serverMessenger, long sleepTime, String name, Listener listener) {
        this.serverMessenger = serverMessenger;
        this.sleepTime = sleepTime;
        this.name = name;
        this.listener = listener;
    }

    // Performing health checks based on an interval, checking if the sensors sent
    // data or not
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
                long lastReceived = listener.getLastReceivedTimestamp();
                if (System.currentTimeMillis() - lastReceived > sleepTime) {
                    serverMessenger.sendMessageToServer(buildPayload("DOWN"));
                } else {
                    serverMessenger.sendMessageToServer(buildPayload("UP"));

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("monitor.Monitor thread interrupted");
                return;
            }
        }
    }

    public String buildPayload(String result) {
        String separator = "|";
        String[] tokens = { "HEALTHCHECK", this.name, result, LocalDateTime.now().toString() };
        return String.join(separator, tokens);
    }
}
