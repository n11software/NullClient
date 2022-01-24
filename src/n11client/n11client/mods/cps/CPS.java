package n11client.mods.cps;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class CPS extends ModDraggable {

    private final List<Long> clicks = new ArrayList<Long>();
    private boolean wasPressed;
    private long lastPressed;

    private RelativePosition rp = new RelativePosition(7, -100 - getWidth(), 1 + font.FONT_HEIGHT);
    private ScreenPosition pos = ScreenPosition.fromRelative(rp);

    public void ResizeEvent() {
        pos.setRelativePos(new RelativePosition(pos.getRelativePos().getSector(), pos.getRelativePos().getX(), pos.getRelativePos().getY()));
    }

    @Override
    public int getWidth() {
        return font.getStringWidth("[0 | 0]");
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        final boolean isPressed = Mouse.isButtonDown(0);

        if (isPressed != this.wasPressed) {
            this.lastPressed = System.currentTimeMillis();
            this.wasPressed = isPressed;
            if (isPressed) {
                this.clicks.add(this.lastPressed);
            }
        }

        font.drawStringWithShadow("CPS: " + getCPS(), pos.getAbsoluteX(), pos.getAbsoluteY(), 0xFFFFFF);
    }

    private int getCPS() {
        final long time = System.currentTimeMillis();
        this.clicks.removeIf(aLong -> aLong + 1000 < time);
        return this.clicks.size();
    }

    @Override
    public void save(ScreenPosition pos) {
        this.pos = pos;
    }

    @Override
    public ScreenPosition load() {
        return pos;
    }
}
