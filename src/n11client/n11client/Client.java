package n11client;

import n11client.Log;
import n11client.gui.SplashScreen;
import net.arikia.dev.drpc.DiscordUser;

public class Client {
    private static final Client INSTANCE = new Client();
    public static Client getInstance() {
        return INSTANCE;
    }

    public static boolean isOldAnimationsEnabled = true, isBorderlessFullscreenEnabled = true;

    private final DiscordIntegration discordRP = new DiscordIntegration();

    public void init() {
        Log.log("Initializing client...");
        SplashScreen.setProgress(1, "Initializing Discord RPC...");
        discordRP.start();
    }

    public void shutdown() {
        discordRP.shutdown();
    }

    public DiscordIntegration getDiscordRP() {
        return discordRP;
    }
}
