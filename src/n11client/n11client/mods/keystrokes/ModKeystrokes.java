package n11client.mods.keystrokes;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import n11client.utils.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.File;

public class ModKeystrokes extends ModDraggable {

    private final KeystrokeSettings config = new KeystrokeSettings(this, new File("N11"));

    public KeystrokeSettings getSettings() {
        return config;
    }

    public static class KeyStrokesMode {

        private final Key[] keys;
        private int width = 0, height = 0;

        public KeyStrokesMode(Key... keysIn) {
            this.keys = keysIn;
            for (Key key: keys) {
                if (key == null) continue;
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

        private static final Key W = new Key("W", Minecraft.getMinecraft().gameSettings.keyBindForward, 26, 2, 22, 22);
        private static final Key A = new Key("A", Minecraft.getMinecraft().gameSettings.keyBindLeft, 2, 26, 22, 22);
        private static final Key S = new Key("S", Minecraft.getMinecraft().gameSettings.keyBindBack, 26, 26,22, 22);
        private static final Key D = new Key("D", Minecraft.getMinecraft().gameSettings.keyBindRight, 50, 26, 22, 22);

        private static final Key LMB = new Key("LMB", 0, 2, 50, 34, 18);
        private static final Key RMB = new Key("RMB", 1, 38, 50, 34, 18);

        private static final Key SPACE = new Key("-----", Minecraft.getMinecraft().gameSettings.keyBindJump, 2, 70, 70, 18);

        private final String name;
        private final KeyBinding keyBind;
        private final int x, y, width, height;
        private final int b;

        public Key(String name, KeyBinding keyBind, int x, int y, int width, int height) {
            this.name = name;
            this.keyBind = keyBind;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.b = -1;
        }

        public Key(String name, int b, int x, int y, int width, int height) {
            this.name = name;
            this.b = b;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.keyBind = null;
        }

        public boolean isDown() { return this.keyBind == null ? Mouse.isButtonDown(this.b) : keyBind.isKeyDown(); }

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

    private ScreenPosition pos = ScreenPosition.fromRelative(config.getPos());

    private KeyStrokesMode mode = new KeyStrokesMode(config.isWASD() ? Key.W : null, config.isWASD() ? Key.A : null, config.isWASD() ? Key.S : null, config.isWASD() ? Key.D : null,
            config.isMouseButtons() ? Key.LMB : null, config.isMouseButtons() ? Key.RMB : null, config.isSpaceBar() ? Key.SPACE : null);

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

    public RelativePosition getPos() { return pos.getRelativePos(); }

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
        for (Key key: mode.getKeys()) {
            if (key == null) continue;
            int textWidth = font.getStringWidth(key.getName());
            if (config.keyBackground) Gui.drawRect(pos.getAbsoluteX()+key.getX(), pos.getAbsoluteY()+key.getY(), pos.getAbsoluteX()+key.getX()+key.getWidth(), pos.getAbsoluteY()+key.getY()+key.getHeight(), key.isDown() ? new Color(config.keyBackgroundPressedRed, config.keyBackgroundPressedGreen, config.keyBackgroundPressedBlue, config.keyBackgroundAlpha).getRGB() : new Color(config.keyBackgroundRed, config.keyBackgroundGreen, config.keyBackgroundBlue, config.keyBackgroundAlpha).getRGB());
            if (key.keyBind != Minecraft.getMinecraft().gameSettings.keyBindJump) font.drawStringWithShadow(key.getName(), (int)(pos.getAbsoluteX() + key.getX() + key.getWidth()/2-textWidth/2), (int)(pos.getAbsoluteY() + key.getY() + key.getHeight()/2-4), key.isDown() ? new Color(config.pressedRed, config.pressedGreen, config.pressedBlue, 255).getRGB() : new Color(config.red, config.green, config.blue, 255).getRGB());
            else Gui.drawRect((int)(pos.getAbsoluteX() + key.getX() + key.getWidth()/2-textWidth/2), (int)(pos.getAbsoluteY() + key.getY() + key.getHeight()/2-1), (int)(pos.getAbsoluteX() + key.getX() + key.getWidth()/2-textWidth/2)+textWidth, (int)(pos.getAbsoluteY() + key.getY() + key.getHeight()/2+1), key.isDown() ? new Color(config.pressedRed, config.pressedGreen, config.pressedBlue, 255).getRGB() : new Color(config.red, config.green, config.blue, 255).getRGB());
        }
        GL11.glPopMatrix();
    }
}
