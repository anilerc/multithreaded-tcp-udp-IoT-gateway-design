import listener.HumidityListener;
import listener.Listener;
import listener.TemperatureListener;
import monitor.Monitor;
import sender.ServerMessenger;

public class Main {
    public static void main(String[] args) {

        // serverMessenger is used to forward data to server by the sensors.
        ServerMessenger serverMessenger = new ServerMessenger();

        // Sensor listeners constantly receive new data and forward them to the server
        Listener humidityListener = new HumidityListener(serverMessenger);
        Listener temperatureListener = new TemperatureListener(serverMessenger);

        // Concurrent threads to listen temperature and humidity
        Thread temperature = new Thread(temperatureListener, "temperatureListener");
        Thread humidity = new Thread(humidityListener, "humidityListener");

        // Concurrent threds to perform interval-based health checks on temperature and
        // humidity sensors
        Thread temperatureHealthChecker = new Thread(
                new Monitor(serverMessenger, 3000, "TEMPERATURE", temperatureListener), "temperatureHealthCheck");
        Thread humidityHealthChecker = new Thread(new Monitor(serverMessenger, 7000, "HUMIDITY", humidityListener),
                "humidityHealthCheck");

        temperature.start();
        humidity.start();

        humidityHealthChecker.start();
        temperatureHealthChecker.start();

        System.out.println("Gateway is running!");
    }
}