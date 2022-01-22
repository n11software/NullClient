package n11client.mods.impl;

import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;

public class ModHelloWorld extends ModDraggable {

    private ScreenPosition pos;

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
        return font.getStringWidth("Hello, world! (Dummy)");
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawString("Hello, world!", pos.getAbsoluteX()+1, pos.getAbsoluteY()+1, -1);
    }

    @Override
    public void renderDummy(ScreenPosition pos) {
        font.drawString("Hello, world! (Dummy)", pos.getAbsoluteX()+1, pos.getAbsoluteY()+1, -1);
    }
}
