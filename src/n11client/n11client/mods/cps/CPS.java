package n11client.mods.cps;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class CPS extends ModDraggable {

    private final List<Long> lclicks = new ArrayList<Long>();
    private final List<Long> rclicks = new ArrayList<Long>();
    private boolean lwasPressed;
    private long llastPressed;
    private boolean rwasPressed;
    private long rlastPressed;

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
        {
            final boolean isPressed = Mouse.isButtonDown(0);

            if (isPressed != this.lwasPressed) {
                this.llastPressed = System.currentTimeMillis();
                this.lwasPressed = isPressed;
                if (isPressed) {
                    this.lclicks.add(this.llastPressed);
                }
            }
        }
        {
            final boolean isPressed = Mouse.isButtonDown(1);

            if (isPressed != this.rwasPressed) {
                this.rlastPressed = System.currentTimeMillis();
                this.rwasPressed = isPressed;
                if (isPressed) {
                    this.rclicks.add(this.rlastPressed);
                }
            }
        }

        font.drawStringWithShadow("[" + getCPSLeft() + " | " + getCPSRight() + "]", pos.getAbsoluteX(), pos.getAbsoluteY(), 0xFFFFFF);
    }

    private int getCPSLeft() {
        final long time = System.currentTimeMillis();
        this.lclicks.removeIf(aLong -> aLong + 1000 < time);
        return this.lclicks.size();
    }

    private int getCPSRight() {
        final long time = System.currentTimeMillis();
        this.rclicks.removeIf(aLong -> aLong + 1000 < time);
        return this.rclicks.size();
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
