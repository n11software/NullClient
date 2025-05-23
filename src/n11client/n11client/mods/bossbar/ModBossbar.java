package n11client.mods.bossbar;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.Mod;
import n11client.mods.ModDraggable;

import java.awt.*;
import java.io.File;

public class ModBossbar extends Mod {

    private final BossbarSettings config = new BossbarSettings(this, new File("N11"));

    public BossbarSettings getSettings() {
        return config;
    }
}
