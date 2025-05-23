package net.minecraft.entity.boss;

import n11client.Client;
import n11client.mods.ModInstances;

public final class BossStatus
{
    public static float healthScale;
    public static int statusBarTime;
    public static String bossName;
    public static boolean hasColorModifier;

    public static void setBossStatus(IBossDisplayData displayData, boolean hasColorModifierIn)
    {
        if (ModInstances.getBossbar().isEnabled()) {
            healthScale = displayData.getHealth() / displayData.getMaxHealth();
            statusBarTime = 100;
            bossName = displayData.getDisplayName().getFormattedText();
            hasColorModifier = hasColorModifierIn;
        }
    }
}
