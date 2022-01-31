package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.armorstatus.ModArmorStatus;
import n11client.mods.clock.Clock;
import n11client.mods.cps.CPS;
import n11client.mods.fps.ModFPS;
import n11client.mods.keystrokes.ModKeystrokes;
import n11client.mods.togglesprintsneak.ModToggleSprintSneak;
import n11client.utils.Log;

public class ModInstances {

    private static ModArmorStatus ArmorStatus;
    private static ModFPS FPS;
    private static ModKeystrokes KeyStrokes;
    private static ModToggleSprintSneak ToggleSprintSneak;
    private static Clock ClockMod;
    private static CPS CPSMod;

    public static void register(HUDManager manager) {
        ArmorStatus = new ModArmorStatus();
        manager.register(ArmorStatus);
        FPS = new ModFPS();
        manager.register(FPS);
        KeyStrokes = new ModKeystrokes();
        manager.register(KeyStrokes);
        ToggleSprintSneak = new ModToggleSprintSneak();
        manager.register(ToggleSprintSneak);
        ClockMod = new Clock();
        manager.register(ClockMod);
        CPSMod = new CPS();
        manager.register(CPSMod);
    }

    public static void ResizeEvent() {
        ArmorStatus.ResizeEvent();
        FPS.ResizeEvent();
        KeyStrokes.ResizeEvent();
        ToggleSprintSneak.ResizeEvent();
        ClockMod.ResizeEvent();
        CPSMod.ResizeEvent();
    }

    public static void unregister() {
        getKeyStrokes().getSettings().setPos(getKeyStrokes().getPos());
        getKeyStrokes().getSettings().save();
    }

    public static ModArmorStatus getArmorStatus() { return ArmorStatus; }
    public static ModFPS getFPS() { return FPS; }
    public static ModKeystrokes getKeyStrokes() { return KeyStrokes; }
    public static ModToggleSprintSneak getToggleSprintSneak() { return ToggleSprintSneak; }
    public static Clock getClockMod() { return ClockMod; }
    public static CPS getCPSMod() { return CPSMod; }
}
