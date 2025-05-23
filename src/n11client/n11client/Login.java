package n11client;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import n11client.utils.Log;
import n11client.utils.SessionChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLogin;
import net.minecraft.client.gui.GuiLoginMicrosoft;
import net.minecraft.client.gui.GuiMainMenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Login {

    public static String Token = "";
    public static String UUID = "";
    public static String Username = "";
    public static boolean done = false;

    public static void getSessionMicrosoft() throws Exception {
        HTTPServer.start();
    }
    public File configFile;

    public static void microsoftLoginDone(String Token, String UUID, String Username, GuiLogin loginGUI) throws IOException {
        Login.Token = Token;
        Login.UUID = UUID;
        Login.Username = Username;
        SessionChanger.getInstance().setUser(Login.Username, Login.Token, Login.UUID);
        File tokenFile = new File(Minecraft.getMinecraft().mcDataDir, "n11client_token.json");
        // parse json
        if (tokenFile.exists()) {
            // read the file
            String json = "";
            try {
                json = new String(java.nio.file.Files.readAllBytes(tokenFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(json).getAsJsonObject();

            JsonObject sessions = obj.getAsJsonObject("sessions");
            int nextKey = 0;
            boolean createSessions = false;
            if (sessions != null) {
                TreeSet<Integer> keys = new TreeSet<>();
                for (Map.Entry<String, JsonElement> entry : sessions.entrySet()) {
                    try {
                        if (entry.getValue().getAsJsonObject().get("uuid").getAsString().contains(UUID)) {
                            System.out.println(entry.getKey());
                            break;
                        }
                        keys.add(Integer.parseInt(entry.getKey()));
                    } catch (NumberFormatException ignored) {}
                }
                nextKey = keys.isEmpty() ? 0 : keys.last() + 1;
            } else {
                sessions = new JsonObject();
                createSessions = true;
            }
            // add the session
            JsonObject session = new JsonObject();
            session.addProperty("accessToken", Token);
            session.addProperty("username", Username);
            session.addProperty("uuid", UUID);

            sessions.add(nextKey + "", session);
            if (createSessions) obj.add("sessions", sessions);
            obj.addProperty("active", nextKey + "");
            String newFile = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
            try (OutputStream os = new FileOutputStream(tokenFile)) {
                os.write(newFile.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // create the file
            JsonObject obj = new JsonObject();
            JsonObject sessions = new JsonObject();
            JsonObject session = new JsonObject();
            session.addProperty("accessToken", Token);
            session.addProperty("username", Username);
            session.addProperty("uuid", UUID);
            sessions.add("0", session);
            obj.add("sessions", sessions);
            obj.addProperty("active", "0");
            String newFile = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
            try (OutputStream os = new FileOutputStream(tokenFile)) {
                os.write(newFile.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Token saved to " + tokenFile.getAbsolutePath());
        loginGUI.MSCB();
        done = true;
    }
}
