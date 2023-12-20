public class Main {

    public static void main(String[] args) {

        WebServer httpServer = new WebServer();
        Thread gatewayListenerThread = new Thread(new GatewayListener(httpServer), "gatewayListener");
        Thread webExposeThread = new Thread(httpServer, "httpServer");

        gatewayListenerThread.start();
        webExposeThread.start();
    }
}