package n11client.mods.cps;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import n11client.mods.fps.FPSSettings;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CPS extends ModDraggable {

    private final List<Long> lclicks = new ArrayList<Long>();
    private final List<Long> rclicks = new ArrayList<Long>();
    private boolean lwasPressed;
    private long llastPressed;
    private boolean rwasPressed;
    private long rlastPressed;

    private final CPSSettings config = new CPSSettings(this, new File("N11"));

    private ScreenPosition pos = ScreenPosition.fromRelative(config.pos);

    public CPSSettings getSettings() {
        return config;
    }

    public RelativePosition getPos() { return pos.getRelativePos(); }

    public void ResizeEvent() {
        pos.setRelativePos(new RelativePosition(pos.getRelativePos().getSector(), pos.getRelativePos().getX(), pos.getRelativePos().getY()));
    }

    @Override
    public int getWidth() {
        return font.getStringWidth(config.left&& config.right?"[999 | 999]":"[999]");
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        if (config.left) {
            final boolean isPressed = Mouse.isButtonDown(0);

            if (isPressed != this.lwasPressed) {
                this.llastPressed = System.currentTimeMillis();
                this.lwasPressed = isPressed;
                if (isPressed) {
                    this.lclicks.add(this.llastPressed);
                }
            }
        }
        if (config.right) {
            final boolean isPressed = Mouse.isButtonDown(1);

            if (isPressed != this.rwasPressed) {
                this.rlastPressed = System.currentTimeMillis();
                this.rwasPressed = isPressed;
                if (isPressed) {
                    this.rclicks.add(this.rlastPressed);
                }
            }
        }

        if (config.right&&config.left) font.drawStringWithShadow("[" + getCPSLeft() + " | " + getCPSRight() + "]", (int)(pos.getAbsoluteX()+(getWidth()/2)-(font.getStringWidth(("[" + getCPSLeft() + " | " + getCPSRight() + "]"))/2)), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue, 255).getRGB());
        else font.drawStringWithShadow("[" + (config.left ? getCPSLeft() : getCPSRight()) + "]", (int)(pos.getAbsoluteX()+(getWidth()/2)-(font.getStringWidth("[" + (config.left ? getCPSLeft() : getCPSRight()) + "]")/2)), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue, 255).getRGB());
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
