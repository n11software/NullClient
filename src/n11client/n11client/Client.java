package n11client;

import n11client.Log;
import net.arikia.dev.drpc.DiscordUser;

public class Client {
    private static final Client INSTANCE = new Client();
    public static Client getInstance() {
        return INSTANCE;
    }

    private final DiscordIntegration discordRP = new DiscordIntegration();

    public void init() {
        Log.log("Initializing client...");
        discordRP.start();
    }

    public void shutdown() {
        discordRP.shutdown();
    }

    public DiscordIntegration getDiscordRP() {
        return discordRP;
    }
}
