package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.impl.ModArmorStatus;
import n11client.mods.impl.ModFPS;
import n11client.mods.impl.ModKeystrokes;
import n11client.mods.impl.ToggleSprintSneak.ModToggleSprintSneak;

public class ModInstances {

    private static ModArmorStatus ArmorStatus;
    private static ModFPS FPS;
    private static ModKeystrokes KeyStrokes;
    private static ModToggleSprintSneak ToggleSprintSneak;

    public static  void register(HUDManager manager) {
        ArmorStatus = new ModArmorStatus();
        manager.register(ArmorStatus);
        FPS = new ModFPS();
        manager.register(FPS);
        KeyStrokes = new ModKeystrokes();
        manager.register(KeyStrokes);
        ToggleSprintSneak = new ModToggleSprintSneak();
        manager.register(ToggleSprintSneak);
    }

    public static ModArmorStatus getArmorStatus() { return ArmorStatus; }
    public static ModFPS getFPS() { return FPS; }
    public static ModKeystrokes getKeyStrokes() { return KeyStrokes; }
    public static ModToggleSprintSneak getToggleSprintSneak() { return ToggleSprintSneak; }
}
