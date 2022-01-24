package n11client.mods.keystrokes;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModKeystrokes extends ModDraggable {

    public static enum KeyStrokesMode {

        WASD(Key.W, Key.A, Key.S, Key.D),
        WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB),
        WASD_SPACE(Key.W, Key.A, Key.S, Key.D, new Key("Space", Minecraft.getMinecraft().gameSettings.keyBindSneak, 1, 41, 58, 18)),
        WASD_SPACE_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, new Key("Space", Minecraft.getMinecraft().gameSettings.keyBindSneak, 1, 61, 58, 18));

        private final Key[] keys;
        private int width = 0, height = 0;

        private KeyStrokesMode(Key... keysIn) {
            this.keys = keysIn;
            for (Key key: keys) {
                this.width = Math.max(this.width, key.getX() + key.getWidth());
                this.height = Math.max(this.height, key.getY() + key.getHeight());
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public Key[] getKeys() {
            return keys;
        }
    }

    private static class Key {

        private static final Key W = new Key("W", Minecraft.getMinecraft().gameSettings.keyBindLeft, 21, 1, 18, 18);
        private static final Key A = new Key("A", Minecraft.getMinecraft().gameSettings.keyBindBack, 1, 21, 18, 18);
        private static final Key S = new Key("S", Minecraft.getMinecraft().gameSettings.keyBindRight, 21, 21, 18, 18);
        private static final Key D = new Key("D", Minecraft.getMinecraft().gameSettings.keyBindJump, 41, 21, 18, 18);

        private static final Key LMB = new Key("LMB", Minecraft.getMinecraft().gameSettings.keyBindPickBlock, 1, 41, 28, 18);
        private static final Key RMB = new Key("RMB", Minecraft.getMinecraft().gameSettings.keyBindDrop, 31, 41, 28, 18);

        private final String name;
        private final KeyBinding keyBind;
        private final int x, y, width, height;

        public Key(String name, KeyBinding keyBind, int x, int y, int width, int height) {
            this.name = name;
            this.keyBind = keyBind;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean isDown() { return keyBind.isKeyDown(); }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String getName() {
            return name;
        }
    }

    private RelativePosition rp = new RelativePosition(0, 5, 5+font.FONT_HEIGHT+20);
    private ScreenPosition pos = ScreenPosition.fromRelative(rp);

    private KeyStrokesMode mode = KeyStrokesMode.WASD_SPACE_MOUSE;

    public void ResizeEvent() {
        pos.setRelativePos(new RelativePosition(pos.getRelativePos().getSector(), pos.getRelativePos().getX(), pos.getRelativePos().getY()));
    }

    public void setMode(KeyStrokesMode mode) {
        this.mode = mode;
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
        return mode.getWidth();
    }

    @Override
    public int getHeight() {
        return mode.getHeight();
    }

    @Override
    public void render(ScreenPosition pos) {
        GL11.glPushMatrix();
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_BLEND);

        for (Key key: mode.getKeys()) {
            int textWidth = font.getStringWidth(key.getName());
//            Gui.drawRect(pos.getAbsoluteX()+key.getX(), pos.getAbsoluteY()+key.getY(), pos.getAbsoluteX()+key.getX()+key.getWidth(), pos.getAbsoluteY()+key.getY()+key.getHeight(), key.isDown() ? new Color(255, 255, 255, 102).getRGB() : new Color(0, 0, 0, 102).getRGB());
            font.drawStringWithShadow(key.getName(), pos.getAbsoluteX() + key.getX() + key.getWidth()/2-textWidth/2, pos.getAbsoluteY() + key.getY() + key.getHeight()/2-4, key.isDown() ? -1 : 0xFFFF5555);
        }

        if (blend) GL11.glEnable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
