package n11client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ScreenPosition {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private int x, y;

    public ScreenPosition(double x, double y) {
        setRelative(x, y);
    }

    public ScreenPosition(int x, int y) {
        setAbsolute(x, y);
    }

    public static ScreenPosition fromRelativePosition(double x, double y) {
        return new ScreenPosition(x, y);
    }

    public static ScreenPosition fromAbsolute(int x, int y) {
        return new ScreenPosition(x, y);
    }

    public static ScreenPosition fromRelative(RelativePosition rp) {
        ScreenPosition pos = new ScreenPosition(rp.getAbsoluteX(), rp.getAbsoluteY());
        pos.setRelativePos(rp);
        return pos;
    }

    public int getAbsoluteX() {
        return x;
    }

    public int getAbsoluteY() {
        return y;
    }

    public double getRelativeX() {
        ScaledResolution sr = new ScaledResolution(mc);
        return x / sr.getScaledWidth_double();
    }

    public double getRelativeY() {
        ScaledResolution sr = new ScaledResolution(mc);
        return y / sr.getScaledHeight_double();
    }

    public void setAbsolute(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setRelative(double x, double y) {
        ScaledResolution sr = new ScaledResolution(mc);
        this.x = (int) (sr.getScaledWidth() / x);
        this.y = (int) (sr.getScaledHeight() / y);
    }

    public void setRelativePos(RelativePosition pos) {
        this.x = pos.getAbsoluteX();
        this.y = pos.getAbsoluteY();
    }

}
