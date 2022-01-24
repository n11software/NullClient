package n11client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jdk.nashorn.internal.parser.JSONParser;
import n11client.event.EventManager;
import n11client.event.EventTarget;
import n11client.event.impl.RenderEvent;
import n11client.gui.SplashScreen;
import n11client.gui.hud.HUDManager;
import n11client.mods.ModInstances;
import net.minecraft.client.Minecraft;
import net.optifine.util.Json;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Client {
    private static final Client INSTANCE = new Client();
    public static Client getInstance() {
        return INSTANCE;
    }

    public static boolean isOldAnimationsEnabled = true, isBorderlessFullscreenEnabled = true;

    private final DiscordIntegration discordRP = new DiscordIntegration();

    private HUDManager HUDMan;

    public void init() {
        Log.log("Initializing client...");
        SplashScreen.setProgress(1, "Initializing Discord RPC...");
        discordRP.start();
        EventManager.register(this);
    }


    public void start() throws Exception {
        HUDMan = HUDManager.getInstance();
        ModInstances.register(HUDMan);

//        Log.log(Login.getSessionMicrosoft());
    }

    public void shutdown() {
        discordRP.shutdown();
    }

    public DiscordIntegration getDiscordRP() {
        return discordRP;
    }

    @EventTarget
    public void onTick(RenderEvent event) {
        if (Minecraft.getMinecraft().gameSettings.keyBindModMenu.isPressed()) HUDMan.openConfigScreen();
    }
}
