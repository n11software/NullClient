package n11client.mods.clock;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;

import java.text.SimpleDateFormat;

public class Clock extends ModDraggable {

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    public String getLocalTime() {
        return formatter.format(new java.util.Date());
    }

    private RelativePosition rp = new RelativePosition(1, -font.getStringWidth(getLocalTime())/2-1, 20);
    private ScreenPosition pos = ScreenPosition.fromRelative(rp);

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
        font.drawStringWithShadow(getLocalTime(), pos.getAbsoluteX(), pos.getAbsoluteY(), 0xFFFF5555);
    }
}

