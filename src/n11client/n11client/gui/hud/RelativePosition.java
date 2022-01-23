package n11client.gui.hud;

import n11client.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class RelativePosition {

    private int Sector, x, y;

    public RelativePosition(int Sector, int x, int y) {
        this.Sector = Sector;
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getSector() {
        return Sector;
    }

    public int getAbsoluteX() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int SectWidth = res.getScaledWidth()/3;
        if (Sector == 0) return x;
        else if (Sector == 1 || Sector == 4) return (SectWidth+SectWidth/2)+x;
        else if (Sector == 2 || Sector == 5) return (SectWidth-(x-SectWidth*2));
        else if (Sector == 3) return x;
        return 0;
    }

    public int getAbsoluteY() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int SectHeight = res.getScaledHeight()/2;
        if (Sector <= 2) return y;
        else return (SectHeight-(y-SectHeight));
    }

}
