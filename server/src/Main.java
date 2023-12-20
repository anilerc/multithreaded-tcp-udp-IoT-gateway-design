import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static ConcurrentLinkedQueue<Object> humidityHealthChecks = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<Object> temperatureHealthChecks = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<Object> humidityValues = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<Object> temperatureValues = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {

        Thread gatewayListenerThread = new Thread(new GatewayListener(), "gatewayListener");
        Thread webExposeThread = new Thread(new WebServer(), "webExposer");

        gatewayListenerThread.start();
        webExposeThread.start();
    }
}