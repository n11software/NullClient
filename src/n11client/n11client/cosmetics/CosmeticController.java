package n11client.cosmetics;

import com.sun.org.apache.xpath.internal.operations.Bool;
import n11client.utils.Log;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CosmeticController {

    public static class HatChecker extends Thread {
        public EntityPlayer player;

        HatChecker(EntityPlayer player) { this.player = player; }

        public void run() {
            try {
                URL url = new URL("https://cosmetics-n11.firelscar1.repl.co/" + EntityPlayer.getUUID(player.getGameProfile()) + "/tophat");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                if (http.getResponseCode() == 200) topHatOwners.put(EntityPlayer.getUUID(player.getGameProfile()).toString(), true);
                else topHatOwners.put(EntityPlayer.getUUID(player.getGameProfile()).toString(), false);
                http.disconnect();
            } catch (IOException error) {
                Log.log(error.getMessage());
            }
        }
    }

    public static Map<String, Boolean> topHatOwners = new HashMap<String, Boolean>();

    public static boolean shouldRenderTopHat(AbstractClientPlayer player) {
        if (!topHatOwners.containsKey(EntityPlayer.getUUID(player.getGameProfile()).toString())) {
            HatChecker checker = new HatChecker(player);
            checker.start();
        }
        return topHatOwners.get(EntityPlayer.getUUID(player.getGameProfile()).toString());
    }

    public static float[] getTopHatColor(AbstractClientPlayer player) { return new float[] {0, 0, 0}; }

}
