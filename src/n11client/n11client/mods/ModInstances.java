package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.impl.ModArmorStatus;

public class ModInstances {

    private static ModArmorStatus ArmorStatus;

    public static  void register(HUDManager manager) {
        ArmorStatus = new ModArmorStatus();
        manager.register(ArmorStatus);
    }

    public static ModArmorStatus getArmorStatus() { return ArmorStatus; }
}
