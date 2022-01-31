package n11client.mods.togglesprintsneak;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.Mod;
import n11client.mods.ModDraggable;
import n11client.mods.ModInstances;
import n11client.mods.keystrokes.KeystrokeSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;

import java.awt.*;
import java.io.File;

public class ModToggleSprintSneak extends ModDraggable {

    private final ToggleSprintSneakSettings config = new ToggleSprintSneakSettings(this, new File("N11"));

    public ToggleSprintSneakSettings getSettings() {
        return config;
    }

    public RelativePosition getPos() { return pos.getRelativePos(); }

    private ScreenPosition pos = ScreenPosition.fromRelative(config.pos);

    public boolean isSprintToggle = config.sprintToggle, isSneakToggle = config.sneakToggle;
    public boolean sprint = true, sneak = true;

    public int keyHoldTicks = 7;

    public void ResizeEvent() {
        pos.setRelativePos(new RelativePosition(pos.getRelativePos().getSector(), pos.getRelativePos().getX(), pos.getRelativePos().getY()));
    }

    @Override
    public void save(ScreenPosition pos) {
        this.pos = pos;
    }

    @Override
    public ScreenPosition load() {
        return pos;
    }

    @Override
    public int getWidth() {
        return font.getStringWidth("[Sprinting (Toggled)]");
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawStringWithShadow(mc.thePlayer.movementInput.getDisplayText(), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue).getRGB());
    }

    @Override
    public void renderDummy(ScreenPosition pos) {
        font.drawStringWithShadow("[Sprinting (Toggled)]", pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue).getRGB());
    }
}
