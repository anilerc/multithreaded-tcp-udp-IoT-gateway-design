public class Main {

    public static void main(String[] args) {

        // Create a new web server.
        WebServer httpServer = new WebServer();

        // Initialize gateway listener and HTTP server threads. Upon receiving new
        // values, the gateway listener will update the database and the http server
        // will expose endpoints such as /humidity, /temperature, /humidity/health and
        // temperature/health.
        Thread gatewayListener = new Thread(new GatewayListener(httpServer), "gatewayListener");
        Thread httpServerThread = new Thread(httpServer, "httpServer");

        gatewayListener.start();
        httpServerThread.start();
    }
}