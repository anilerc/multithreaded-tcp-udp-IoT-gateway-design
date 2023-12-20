package listener;

import sender.ServerMessenger;

public abstract class Listener implements Runnable {
    private final ServerMessenger serverMessenger;
    private volatile long lastReceivedTimestamp;

    public Listener(ServerMessenger serverMessenger) {
        this.serverMessenger = serverMessenger;
    }

    public long getLastReceivedTimestamp() {
        return lastReceivedTimestamp;
    }

    public void setLastReceivedTimestamp(long lastReceivedTimestamp) {
        this.lastReceivedTimestamp = lastReceivedTimestamp;
    }

    public ServerMessenger getServerMessenger() {
        return serverMessenger;
    }
}
