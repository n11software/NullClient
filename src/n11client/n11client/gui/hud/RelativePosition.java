package n11client.gui.hud;

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
        // ABSOLUTE PAIN, WILL NEVER DO THIS AGAIN... I hope. :(

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        RelativePosition rp = new RelativePosition(0, 0, 0);

        int SectWidth = res.getScaledWidth()/3;
        int SectHeight = res.getScaledHeight()/3;
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
            if (y-SectHeight < SectHeight/2) {
                rp = new RelativePosition(3, x, -parseInt("-" + (-(y-(SectHeight+SectHeight/2)))));
            } else if (y-SectHeight > SectHeight/2) {
                rp = new RelativePosition(3, x, -(y-(SectHeight+SectHeight/2)));
            } else {
                rp = new RelativePosition(3, x, 0);
            }
        } else if (x>=SectWidth && x<=SectWidth*2 && y>=SectHeight && y<=SectHeight*2) {
            if (x-SectWidth <= SectWidth/2 && y-SectHeight >= SectHeight/2) {
                rp = new RelativePosition(4, parseInt("-" + (-(x-(SectWidth+SectWidth/2)))), -(y-(SectHeight+SectHeight/2)));
            } else if (x-SectWidth <= SectWidth/2 && y-SectHeight <= SectHeight/2) {
                rp = new RelativePosition(4, parseInt("-" + (-(x-(SectWidth+SectWidth/2)))), -(y-(SectHeight+SectHeight/2)));
            } else if (x-SectWidth >= SectWidth/2 && y-SectHeight <= SectHeight/2) {
                rp = new RelativePosition(4, -(-(x-(SectWidth+SectWidth/2))), -(y-(SectHeight+SectHeight/2)));
            } else if (x-SectWidth >= SectWidth/2 && y-SectHeight >= SectHeight/2) {
                rp = new RelativePosition(4, -(-(x-(SectWidth+SectWidth/2))), -(y-(SectHeight+SectHeight/2)));
            }
        } else if (x>=SectWidth*2 && x<=SectWidth*3 && y>=SectHeight && y<=SectHeight*2) {
            if (y-SectHeight < SectHeight/2) {
                rp = new RelativePosition(5, (SectWidth-(x-SectWidth*2)), -parseInt("-" + (-(y-(SectHeight+SectHeight/2)))));
            } else if (y-SectHeight > SectHeight/2) {
                rp = new RelativePosition(5, (SectWidth-(x-SectWidth*2)), -(y-(SectHeight+SectHeight/2)));
            } else {
                rp = new RelativePosition(5, (SectWidth-(x-SectWidth*2)), 0);
            }
        } else if (x>=0 && x<=SectWidth && y>=SectHeight*2 && y<=SectHeight*3) {
            rp = new RelativePosition(6, x, (SectHeight-(y-SectHeight*2)));
        } else if (x>=SectWidth && x<=SectWidth*2 && y>=SectHeight*2 && y<=SectHeight*3) {
            if (x-SectWidth < SectWidth/2) {
                rp = new RelativePosition(7, parseInt("-" + (-(x-(SectWidth+SectWidth/2)))), (SectHeight-(y-SectHeight*2)));
            } else if (x-SectWidth > SectWidth/2) {
                rp = new RelativePosition(7, (x-(SectWidth+SectWidth/2)), (SectHeight-(y-SectHeight*2)));
            } else {
                rp = new RelativePosition(7, 0, (SectHeight-(y-SectHeight*2)));
            }
        } else if (x>=SectWidth*2 && x<=SectWidth*3 && y>=SectHeight*2 && y<=SectHeight*3) {
            rp = new RelativePosition(8, (SectWidth-(x-SectWidth*2)), (SectHeight-(y-SectHeight*2)));
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
        else if (Sector == 1 || Sector == 7) return (SectWidth+SectWidth/2)+x;
        else if (Sector == 2 || Sector == 8 || Sector == 5) return (SectWidth-(x-SectWidth*2));
        else if (Sector == 6 || Sector == 3) return x;
        else if (Sector == 4) return ((x+(SectWidth+SectWidth/2)));
        return 0;
    }

    public int getAbsoluteY() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int SectHeight = res.getScaledHeight()/3;
        if (Sector <= 2) return y;
        else if (Sector == 3 || Sector == 5) return -(y-(SectHeight+SectHeight/2));
        else if (Sector == 4) return -(y-(res.getScaledHeight()/2));
        else return (SectHeight-(y-SectHeight*2));
    }

}
