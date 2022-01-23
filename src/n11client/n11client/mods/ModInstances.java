package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.impl.ModArmorStatus;
import n11client.mods.impl.ModFPS;

public class ModInstances {

    private static ModArmorStatus ArmorStatus;
    private static ModFPS FPS;

    public static  void register(HUDManager manager) {
        ArmorStatus = new ModArmorStatus();
        manager.register(ArmorStatus);
        FPS = new ModFPS();
        manager.register(FPS);
    }

    public static ModArmorStatus getArmorStatus() { return ArmorStatus; }
    public static ModFPS getFPS() { return FPS; }
}
