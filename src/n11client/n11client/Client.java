package n11client;

import n11client.event.EventManager;
import n11client.event.EventTarget;
import n11client.event.impl.RenderEvent;
import n11client.event.impl.TickEvent;
import n11client.gui.SplashScreen;
import n11client.gui.hud.HUDManager;
import n11client.mods.ModInstances;
import n11client.mods.discord.DiscordIntegration;
import n11client.utils.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLogin;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.applet.Applet;
import java.awt.*;

public class Client {
    private static final Client INSTANCE = new Client();
    public static Client getInstance() {
        return INSTANCE;
    }

    public static boolean isOldAnimationsEnabled = true, isFullbrightEnabled = true;

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

        if (!isFullbrightEnabled) Minecraft.getMinecraft().gameSettings.gammaSetting = 1;
        else Minecraft.getMinecraft().gameSettings.gammaSetting = (((Minecraft.getMinecraft().gameSettings.gammaSetting+13)%28) + 1);
    }

    public GuiLogin loginGUI;

    public static void loginMicrosoft() {
        try {
            Login.getSessionMicrosoft();
        } catch (Exception e) {
            Log.log("Error!");
        }
    }

    public void shutdown() {
        discordRP.shutdown();
        ModInstances.unregister();
    }

    public DiscordIntegration getDiscordRP() {
        return discordRP;
    }

    @EventTarget
    public void onTick(RenderEvent event) {
        if (Minecraft.getMinecraft().gameSettings.keyBindModMenu.isPressed()) HUDMan.openConfigScreen();
    }
}
