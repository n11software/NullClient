package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.armorstatus.ModArmorStatus;
import n11client.mods.clock.Clock;
import n11client.mods.cps.CPS;
import n11client.mods.fps.ModFPS;
import n11client.mods.keystrokes.ModKeystrokes;
import n11client.mods.togglesprintsneak.ModToggleSprintSneak;
import n11client.mods.togglesprintsneak.ToggleSprintSneakSettings;
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
        ArmorStatus.setEnabled(ArmorStatus.getSettings().enabled);
        manager.register(ArmorStatus);
        FPS = new ModFPS();
        FPS.setEnabled(FPS.getSettings().enabled);
        manager.register(FPS);
        KeyStrokes = new ModKeystrokes();
        KeyStrokes.setEnabled(KeyStrokes.getSettings().enabled);
        manager.register(KeyStrokes);
        ToggleSprintSneak = new ModToggleSprintSneak();
        ToggleSprintSneak.setEnabled(ToggleSprintSneak.getSettings().enabled);
        manager.register(ToggleSprintSneak);
        ClockMod = new Clock();
        ClockMod.setEnabled(ClockMod.getSettings().enabled);
        manager.register(ClockMod);
        CPSMod = new CPS();
        CPSMod.setEnabled(CPSMod.getSettings().enabled);
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
        getArmorStatus().getSettings().pos = getArmorStatus().getPos();
        getArmorStatus().getSettings().enabled = getArmorStatus().isEnabled();
        getArmorStatus().getSettings().itemCount = getArmorStatus().showItemCount;
        getArmorStatus().getSettings().vertical = getArmorStatus().isVertical;
        getArmorStatus().getSettings().rightAligned = getArmorStatus().isRightAligned;
        getArmorStatus().getSettings().durability = getArmorStatus().isShowingDurability;
        getArmorStatus().getSettings().durabilityText = getArmorStatus().isShowingDurabilityText;
        getArmorStatus().getSettings().save();
        getKeyStrokes().getSettings().setPos(getKeyStrokes().getPos());
        getKeyStrokes().getSettings().enabled = getKeyStrokes().isEnabled();
        getKeyStrokes().getSettings().save();
        getToggleSprintSneak().getSettings().pos = getToggleSprintSneak().getPos();
        getToggleSprintSneak().getSettings().enabled = getToggleSprintSneak().isEnabled();
        getToggleSprintSneak().getSettings().sneakToggle = getToggleSprintSneak().isSneakToggle;
        getToggleSprintSneak().getSettings().sprintToggle = getToggleSprintSneak().isSprintToggle;
        getToggleSprintSneak().getSettings().save();
        getFPS().getSettings().pos = getFPS().getPos();
        getFPS().getSettings().enabled = getFPS().isEnabled();
        getFPS().getSettings().save();
        getCPSMod().getSettings().pos = getCPSMod().getPos();
        getCPSMod().getSettings().enabled = getCPSMod().isEnabled();
        getCPSMod().getSettings().save();
        getClockMod().getSettings().pos = getClockMod().getPos();
        getClockMod().getSettings().enabled = getClockMod().isEnabled();
        getClockMod().getSettings().hr24 = getClockMod().is24hr();
        getClockMod().getSettings().save();
    }

    public static ModArmorStatus getArmorStatus() { return ArmorStatus; }
    public static ModFPS getFPS() { return FPS; }
    public static ModKeystrokes getKeyStrokes() { return KeyStrokes; }
    public static ModToggleSprintSneak getToggleSprintSneak() { return ToggleSprintSneak; }
    public static Clock getClockMod() { return ClockMod; }
    public static CPS getCPSMod() { return CPSMod; }
}
