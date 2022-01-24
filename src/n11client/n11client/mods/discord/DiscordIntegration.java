package n11client.mods.discord;

import n11client.Log;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordIntegration {
        private boolean running = true;
        private long created = 0;

        public void start() {
            this.created = System.currentTimeMillis();
            DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
                @Override
                public void apply(DiscordUser user)  {
                    Log.log("Fetched Discord User: " + user.username + "#" + user.discriminator + ".");
                    update("Booting up...", "");
                }
            }).build();

            DiscordRPC.discordInitialize("933611057350598667", handlers, true);

            new Thread("Discord RPC Callback Handler") {
                    @Override
                    public void run() {
                        while(running) {
                            DiscordRPC.discordRunCallbacks();
                        }
                    }
            }.start();
        }

        public void shutdown() {
            DiscordRPC.discordShutdown();
            running = false;
        }

        public void update(String firstLine, String secondLine) {
            DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
            b.setBigImage("large", "");
            b.setDetails(firstLine);
            b.setStartTimestamps(created);

            DiscordRPC.discordUpdatePresence(b.build());
        }
}
