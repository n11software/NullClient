package n11client.mods.fps;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;

import java.awt.*;
import java.io.File;

public class ModFPS extends ModDraggable {

    private final FPSSettings config = new FPSSettings(this, new File("N11"));

    private ScreenPosition pos = ScreenPosition.fromRelative(config.pos);

    public FPSSettings getSettings() {
        return config;
    }

    public RelativePosition getPos() { return pos.getRelativePos(); }

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
        return font.getStringWidth("FPS: " + mc.getDebugFPS());
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawStringWithShadow("FPS: " + mc.getDebugFPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue).getRGB());
    }
}
