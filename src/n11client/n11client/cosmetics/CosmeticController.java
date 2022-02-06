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

    public static Map<String, Boolean> topHatOwners = new HashMap<String, Boolean>();

    public static boolean shouldRenderTopHat(AbstractClientPlayer player) {
        if (!topHatOwners.containsKey(EntityPlayer.getUUID(player.getGameProfile()).toString())) {
            try {
                URL url = new URL("https://github.com/Null-N11/N11-Cosmetics/blob/main/" + EntityPlayer.getUUID(player.getGameProfile()) + "/tophat");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
                if (http.getResponseCode() == 200) topHatOwners.put(EntityPlayer.getUUID(player.getGameProfile()).toString(), true);
                else topHatOwners.put(EntityPlayer.getUUID(player.getGameProfile()).toString(), false);
                http.disconnect();
            } catch (IOException error) {
                Log.log(error.getMessage());
            }
        }
        return topHatOwners.get(EntityPlayer.getUUID(player.getGameProfile()).toString());
    }

    public static float[] getTopHatColor(AbstractClientPlayer player) { return new float[] {0, 0, 0}; }

}
