package n11client.mods.clock;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import n11client.mods.cps.CPSSettings;

import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class Clock extends ModDraggable {

    private final ClockSettings config = new ClockSettings(this, new File("N11"));
//
    private ScreenPosition pos = ScreenPosition.fromRelative(config.pos);

    public ClockSettings getSettings() {
        return config;
    }

    public boolean hr24 = config.hr24;

    public boolean is24hr() { return hr24; }

    public RelativePosition getPos() { return pos.getRelativePos(); }

    SimpleDateFormat formatter = new SimpleDateFormat(hr24 ? "HH:mm" : "h:mm aa");
    public String getLocalTime() {
        return formatter.format(new java.util.Date());
    }

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
        return font.getStringWidth(getLocalTime());
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawStringWithShadow(getLocalTime(), pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue, 255).getRGB());
    }
}

