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
        	if (t.getRequestURI().getPath().contains("success")) {
        		String query = t.getRequestURI().getQuery();
        		String Token="", UUID="", Username="";
        		
        		String[] pairs = query.split("&");
        		for (String pair : pairs) {
        		    String[] keyValue = pair.split("=", 2); // limit=2 to avoid splitting values with '='
        		    String key = keyValue[0];
        		    String value = keyValue.length > 1 ? keyValue[1] : "";
        		    if (key.contains("token")) Token = value;
        		    else if (key.contains("uuid")) UUID = value;
        		    else if (key.contains("username")) Username = value;
        		}
				// Return a html page that closes the tab
				String css = "body { display: flex; height: 100%; flex-direction: column; justify-content: center; align-items: center; background-color: #2D2D2A; }\n.title { font-size: 22pt; font-weight: bold; color: #EDF6F9; font-family: Arial; }\n.subtitle { font-size: 16pt; color: #C7CEDB; font-family: Arial; }";
				String js = "<script>\n"
						+ "window.close();\n"
						+ "</script>";
				String response = "<html><head><title>N11 Client</title></head><body><span class=\"title\">Logged In!</span><span class=\"subtitle\">You can close this window!</span></body><style>" + css + "</style>"+js+"</html>";
				t.sendResponseHeaders(200, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
                stop();
                Login.microsoftLoginDone(Token, UUID, Username, Client.getInstance().loginGUI);
        	} else {
	            String css = "body { display: flex; height: 100%; flex-direction: column; justify-content: center; align-items: center; background-color: #2D2D2A; }\n.title { font-size: 22pt; font-weight: bold; color: #EDF6F9; font-family: Arial; }\n.subtitle { font-size: 16pt; color: #C7CEDB; font-family: Arial; }";
	            String js = "<script>\n"
	            		+ "const key = new URLSearchParams(window.location.search).get('code');\n"
	            		+ "window.location.href = \"https://n11.dev:25564/auth?code=\" + key;\n"
	            		+ "</script>";
	            String response = "<html><head><title>N11 Client</title></head><body><span class=\"title\">Please Wait!</span><span class=\"subtitle\">You can close this window!</span></body><style>" + css + "</style>"+js+"</html>";
	            t.sendResponseHeaders(200, response.length());
	            OutputStream os = t.getResponseBody();
	            os.write(response.getBytes());
	            os.close();
        	}
        }
    }

}
