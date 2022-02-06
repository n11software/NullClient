package n11client.mods.borderlessfullscreen;

import n11client.event.EventTarget;
import n11client.event.impl.TickEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

public class ModBorderlessFullscreen {

    boolean lastFullscreen = false;

    @EventTarget
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            boolean fullScreenNow = Minecraft.getMinecraft().isFullScreen();

            if (this.lastFullscreen != fullScreenNow) {
                this.fix(fullScreenNow);
                this.lastFullscreen = fullScreenNow;
            }

        }
    }

    public void fix(boolean fullscreen) {
        try {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
                Display.setResizable(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
                Display.setResizable(true);
                Dimension e = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((e.getWidth() - (double) Display.getWidth()) / 2.0D);
                int y = (int) ((e.getHeight() - (double) Display.getHeight()) / 2.0D);

                Display.setLocation(x, y);
            }
        } catch (LWJGLException lwjglexception) {
            lwjglexception.printStackTrace();
        }

    }

}
