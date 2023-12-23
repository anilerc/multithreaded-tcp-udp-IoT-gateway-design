public class Main {
    public static void main(String[] args) {

        Thread valueGenerationThread = new Thread(new ValueGeneration(), "valueGenerator");
        Thread healthCheckThread = new Thread(new HealthCheck(), "healthCheck");

        valueGenerationThread.start();
        healthCheckThread.start();

    }
}
