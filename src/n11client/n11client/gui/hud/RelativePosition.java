package n11client.gui.hud;

import n11client.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import static java.lang.Integer.parseInt;

public class RelativePosition {

    private int Sector, x, y;

    public RelativePosition(int Sector, int x, int y) {
        this.Sector = Sector;
        this.x = x;
        this.y = y;
    }

    public RelativePosition(int x, int y) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        RelativePosition rp = new RelativePosition(0, 0, 0);

        int SectWidth = res.getScaledWidth()/3;
        int SectHeight = res.getScaledHeight()/2;
        if (x>=0 && x<=SectWidth && y>=0 && y<=SectHeight) {
            rp = new RelativePosition(0, x, y);
        } else if (x>=SectWidth && x<=SectWidth*2 && y>=0 && y<=SectHeight) {
            if (x-SectWidth < SectWidth/2) {
                rp = new RelativePosition(1, parseInt("-" + (-(x-(SectWidth+SectWidth/2)))), y);
            } else if (x-SectWidth > SectWidth/2) {
                rp = new RelativePosition(1, (x-(SectWidth+SectWidth/2)), y);
            } else {
                rp = new RelativePosition(1, 0, y);
            }
        } else if (x>=SectWidth*2 && x<=SectWidth*3 && y>=0 && y<=SectHeight) {
            rp = new RelativePosition(2, (SectWidth-(x-SectWidth*2)), y);
        } else if (x>=0 && x<=SectWidth && y>=SectHeight && y<=SectHeight*2) {
            rp = new RelativePosition(3, x, (SectHeight-(y-SectHeight)));
        } else if (x>=SectWidth && x<=SectWidth*2 && y>=SectHeight && y<=SectHeight*2) {
            if (x-SectWidth < SectWidth/2) {
                rp = new RelativePosition(4, parseInt("-" + (-(x-(SectWidth+SectWidth/2)))), (SectHeight-(y-SectHeight)));
            } else if (x-SectWidth > SectWidth/2) {
                rp = new RelativePosition(4, (x-(SectWidth+SectWidth/2)), (SectHeight-(y-SectHeight)));
            } else {
                rp = new RelativePosition(4, 0, (SectHeight-(y-SectHeight)));
            }
        } else if (x>=SectWidth*2 && x<=SectWidth*3 && y>=SectHeight && y<=SectHeight*2) {
            rp = new RelativePosition(5, (SectWidth-(x-SectWidth*2)), (SectHeight-(y-SectHeight)));
        }
        this.x = rp.getX();
        this.y = rp.getY();
        this.Sector = rp.getSector();
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
