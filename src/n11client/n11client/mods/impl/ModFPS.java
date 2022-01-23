package n11client.mods.impl;

import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;

public class ModFPS extends ModDraggable {

    private ScreenPosition pos = ScreenPosition.fromAbsolute(5, 5);

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
        return font.getStringWidth("FPS: " + mc.getDebugFPS());
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawStringWithShadow("FPS: " + mc.getDebugFPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), -1);
    }
}
