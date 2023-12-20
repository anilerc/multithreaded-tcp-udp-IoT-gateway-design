import listener.HumidityListener;
import listener.Listener;
import listener.TemperatureListener;
import monitor.Monitor;
import sender.ServerMessenger;

public class Main {
    public static void main(String[] args) {

        ServerMessenger serverMessenger = new ServerMessenger();

        Listener humidityListener = new HumidityListener(serverMessenger);
        Listener temperatureListener = new TemperatureListener(serverMessenger);

        Thread temperature = new Thread(temperatureListener);
        Thread humidity = new Thread(humidityListener);

        Thread humidityHealthChecker = new Thread(new Monitor(serverMessenger, 7000, "HUMIDITY",humidityListener));
        Thread temperatureHealthChecker = new Thread(new Monitor(serverMessenger, 3000, "TEMPERATURE", temperatureListener));

        temperature.start();
        humidity.start();

        humidityHealthChecker.start();
        temperatureHealthChecker.start();

        System.out.println("Gateway is running!");
    }
}