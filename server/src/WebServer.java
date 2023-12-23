import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebServer implements Runnable {

    // Generate HTML files and deliver the HTTP response with the latest state of
    // sensor value and health check data.
    @Override
    public void run() {
        System.out.println("Web server thread is running.");
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
            httpServer.setExecutor(null);

            httpServer.createContext("/humidity", (httpExchange) -> {
                var response = generateHTML("Humidity Values", humidityValues);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.createContext("/temperature", (httpExchange) -> {
                var response = generateHTML("Temperature Values", temperatureValues);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.createContext("/humidity/health", (httpExchange) -> {
                var response = generateHTML("Humidity Health Checks", humidityHealthChecks);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.createContext("/temperature/health", (httpExchange) -> {
                var response = generateHTML("Temperature Health Checks", temperatureHealthChecks);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Function to render HTML files based on the data to render and file title.
    private StringBuilder generateHTML(String title, ConcurrentLinkedQueue<String> dataToRender) {
        StringBuilder response = new StringBuilder("<html><body><h1>" + title + "</h1><ul>");
        for (Object object : dataToRender) {
            response.append("<li>").append(object).append("</li>");
        }
        response.append("</ul></body></html>");

        return response;
    }

    // Send back the response over HTTP and deliver the HTML file.
    private void sendHttpResponse(HttpExchange httpExchange, StringBuilder response) {
        try {
            httpExchange.sendResponseHeaders(200, response.toString().getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Using thread-safe ConcurrentLinkedQueue data structure as the in-memory
    // database.
    private final ConcurrentLinkedQueue<String> humidityHealthChecks;
    private final ConcurrentLinkedQueue<String> temperatureHealthChecks;
    private final ConcurrentLinkedQueue<String> humidityValues;
    private final ConcurrentLinkedQueue<String> temperatureValues;

    // Constructors and getters
    public WebServer() {
        this.humidityHealthChecks = new ConcurrentLinkedQueue<>();
        this.temperatureHealthChecks = new ConcurrentLinkedQueue<>();
        this.humidityValues = new ConcurrentLinkedQueue<>();
        this.temperatureValues = new ConcurrentLinkedQueue<>();
    }

    public ConcurrentLinkedQueue<String> getHumidityHealthChecks() {
        return humidityHealthChecks;
    }

    public ConcurrentLinkedQueue<String> getTemperatureHealthChecks() {
        return temperatureHealthChecks;
    }

    public ConcurrentLinkedQueue<String> getHumidityValues() {
        return humidityValues;
    }

    public ConcurrentLinkedQueue<String> getTemperatureValues() {
        return temperatureValues;
    }
}
