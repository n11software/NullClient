package n11client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class HTTPServer {

    private static HttpServer server;

    public static void start() throws Exception {
        server = HttpServer.create(new InetSocketAddress(25564), 0);
        server.createContext("/", new Handler());
        server.setExecutor(null);
        server.start();
    }

    public static void stop() {
        server.stop(0);
    }

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "<script>close();</script>You can close this window!";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            stop();
            Login.microsoftLoginDone(t.getRequestURI().getQuery().substring(5));
        }
    }

}
