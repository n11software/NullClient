package n11client.cosmetics;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class CosmeticController {

    public static boolean shouldRenderTopHat(AbstractClientPlayer player) { return (EntityPlayer.getUUID(player.getGameProfile()).equals(UUID.fromString("aade6417-0436-4ba7-92c3-6b1d6799d5e2")) || EntityPlayer.getUUID(player.getGameProfile()).equals(UUID.fromString("b58e26af-dd94-4a32-89e2-e31d9eb9af57")) || EntityPlayer.getUUID(player.getGameProfile()).equals(UUID.fromString("97000a4f-96b8-4072-a5a7-1ca08de5e48e"))); }

    public static float[] getTopHatColor(AbstractClientPlayer player) { return new float[] {0, 0, 0}; }

}
