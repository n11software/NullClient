package n11client;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.client.Minecraft;

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
            String css = "body { display: flex; height: 100%; flex-direction: column; justify-content: center; align-items: center; background-color: #2D2D2A; }\n.title { font-size: 22pt; font-weight: bold; color: #EDF6F9; font-family: Arial; }\n.subtitle { font-size: 16pt; color: #C7CEDB; font-family: Arial; }";
            String response = "<html><head><title>N11 Client</title></head><body><span class=\"title\">Logged In!</span><span class=\"subtitle\">You can close this window!</span></body><style>" + css + "</style></html>";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            stop();
            Login.microsoftLoginDone(t.getRequestURI().getQuery().substring(5), Client.getInstance().loginGUI);
        }
    }

}
