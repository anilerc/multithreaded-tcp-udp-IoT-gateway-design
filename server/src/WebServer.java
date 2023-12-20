import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WebServer implements Runnable {

    @Override
    public void run() {
        System.out.println("Mevcut humidity değerleri" + Main.humidityValues);
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(8082), 0);
            httpServer.setExecutor(null);

            httpServer.createContext("/humidity", (httpExchange) -> {
                System.out.println("Generationda kullanılan değerler" +  Main.humidityValues);
                var response = generateHTML("Humidity Values", Main.humidityValues);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.createContext("/temperature", (httpExchange) -> {
                var response = generateHTML("Temperature Values", Main.temperatureValues);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.createContext("/humidity/health", (httpExchange) -> {
                var response = generateHTML("Humidity Health Checks", Main.humidityHealthChecks);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.createContext("/temperature/health", (httpExchange) -> {
                var response = generateHTML("Temperature Health Checks", Main.temperatureHealthChecks);
                sendHttpResponse(httpExchange, response);
            });

            httpServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder generateHTML(String title, ConcurrentLinkedQueue<Object> dataToRender) {
        StringBuilder response = new StringBuilder("<html><body><h1>"+title+"</h1><ul>");
        for (Object object : dataToRender) {
            response.append("<li>").append(object).append("</li>");
        }
        response.append("</ul></body></html>");

        return response;
    }

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


    
}
