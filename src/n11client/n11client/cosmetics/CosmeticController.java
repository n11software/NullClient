package n11client.cosmetics;

import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticController {

    public static boolean shouldRenderTopHat(AbstractClientPlayer player) { return (player.getName() == "FiReLScar" || player.getName() == "4tl0renz0" || player.getName() == "myst303"); }

    public static float[] getTopHatColor(AbstractClientPlayer player) { return new float[] {0, 0, 0}; }

}
