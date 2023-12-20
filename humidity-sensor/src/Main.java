public class Main {
    public static void main(String[] args) {

        Thread valueGenerationThread = new Thread(new ValueGeneration());
        Thread healthCheckThread = new Thread(new HealthCheck());

        valueGenerationThread.start();
        healthCheckThread.start();

    }

    public static byte[] getPayload(String[] tokens) {
        String separator = "|";
        String str = String.join(separator, tokens);

        return str.getBytes();
    }
}
