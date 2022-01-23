package n11client.mods.impl.ToggleSprintSneak;

import n11client.gui.hud.ScreenPosition;
import n11client.mods.Mod;
import n11client.mods.ModDraggable;
import n11client.mods.ModInstances;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import org.newdawn.slick.Game;

public class ModToggleSprintSneak extends ModDraggable {

    private ScreenPosition pos = ScreenPosition.fromAbsolute(5, 706-font.FONT_HEIGHT-2);

    public boolean isSprintToggle = true, isSneakToggle = false;

    public int keyHoldTicks = 7;

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
        return font.getStringWidth("[Sprinting (Key Toggled)]");
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawStringWithShadow(mc.thePlayer.movementInput.getDisplayText(), pos.getAbsoluteX(), pos.getAbsoluteY(), -1);
    }

    @Override
    public void renderDummy(ScreenPosition pos) {
        font.drawStringWithShadow("[Sprinting (Key Toggled)]", pos.getAbsoluteX(), pos.getAbsoluteY(), -1);
    }
}
