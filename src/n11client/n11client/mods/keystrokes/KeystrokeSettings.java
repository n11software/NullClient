package n11client.mods.keystrokes;

import com.google.gson.Gson;
import n11client.utils.Log;
import n11client.utils.N11JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")

public class KeystrokeSettings {
    public ModKeystrokes theMod;
    public File configFile;
    public int x;
    public int y;
    public  boolean enabled = true;
    public  boolean mouseButtons = true;
    public  boolean spaceBar = true;
    public  boolean WASD = true;

    public  int red = 255;
    public  int green = 255;
    public  int blue = 255;
    public  int pressedRed = 0;
    public  int pressedGreen = 0;
    public  int pressedBlue = 0;

    public  boolean keyBackground = true;
    public  int keyBackgroundAlpha = 120;
    public  int keyBackgroundRed = 0;
    public  int keyBackgroundGreen = 0;
    public  int keyBackgroundBlue = 0;
    public  int keyBackgroundPressedRed = 255;
    public  int keyBackgroundPressedGreen = 255;
    public  int keyBackgroundPressedBlue = 255;

    Gson gson = new Gson();

    public KeystrokeSettings(ModKeystrokes mod, File directory) {
        if (!directory.exists()) directory.mkdirs();
        theMod = mod;
        configFile = new File(directory, "keystrokes.json");
    }

    public void load() {
        try {
            if (!configFile.getParentFile().exists() || !configFile.exists()) {
                save();
                return;
            }

            BufferedReader f = new BufferedReader(new FileReader(configFile));
            List<String> options = f.lines().collect(Collectors.toList());
            if (options.isEmpty()) return;
            String builder = String.join("", options);
            if (builder.trim().length() > 0) parseSettings(new N11JsonObject(builder.trim()));
            f.close();
        } catch (Exception ex) {
            Log.warn("Could not load config file! {}");
            save();
        }
    }

    public void save() {
        try {
            if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdirs();
            if (!configFile.exists() && !configFile.createNewFile()) return;

            N11JsonObject object = new N11JsonObject();
            object.addProperty("x", x);
            object.addProperty("y", y);
            object.addProperty("red", red);
            object.addProperty("green", green);
            object.addProperty("blue", blue);
            object.addProperty("pressedRed", pressedRed);
            object.addProperty("pressedGreen", pressedGreen);
            object.addProperty("pressedBlue", pressedBlue);
            object.addProperty("enabled", enabled);
            object.addProperty("mouseButtons", mouseButtons);
            object.addProperty("spaceBar", spaceBar);
            object.addProperty("keyBackground", keyBackground);
            object.addProperty("WASD", WASD);
            object.addProperty("keyBackgroundAlpha", keyBackgroundAlpha);
            object.addProperty("keyBackgroundRed", keyBackgroundRed);
            object.addProperty("keyBackgroundGreen", keyBackgroundGreen);
            object.addProperty("keyBackgroundBlue", keyBackgroundBlue);
            object.addProperty("keyBackgroundPressedRed", keyBackgroundPressedRed);
            object.addProperty("keyBackgroundPressedGreen", keyBackgroundPressedGreen);
            object.addProperty("keyBackgroundPressedBlue", keyBackgroundPressedBlue);
            object.writeToFile(configFile);
        } catch (Exception ex) {
            Log.warn(String.format("Could not save config file! (\"%s\")", configFile.getName()));
        }
    }

    public void parseSettings(N11JsonObject object) {
        x = object.optInt("x");
        y = object.optInt("y");
        red = object.optInt("red");
        green = object.optInt("green");
        blue = object.optInt("blue");
        pressedRed = object.optInt("pressedRed");
        pressedGreen = object.optInt("pressedGreen");
        pressedBlue = object.optInt("pressedBlue");
        enabled = object.optBoolean("enabled");
        mouseButtons = object.optBoolean("mouseButtons");
        spaceBar = object.optBoolean("spaceBar");
        WASD = object.optBoolean("WASD");
        keyBackground = object.optBoolean("keyBackground");
        keyBackgroundAlpha = object.optInt("keyBackgroundAlpha");
        keyBackgroundRed = object.optInt("keyBackgroundRed");
        keyBackgroundGreen = object.optInt("keyBackgroundGreen");
        keyBackgroundBlue = object.optInt("keyBackgroundBlue");
        keyBackgroundPressedRed = object.optInt("keyBackgroundPressedRed");
        keyBackgroundPressedGreen = object.optInt("keyBackgroundPressedGreen");
        keyBackgroundPressedBlue = object.optInt("keyBackgroundPressedBlue");
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getRed() { return red; }
    public void setRed(int red) { this.red = red; }
    public int getGreen() { return green; }
    public void setGreen(int green) { this.green = green; }
    public int getBlue() { return blue; }
    public void setBlue(int blue) { this.blue = blue; }
    public int getPressedRed() { return pressedRed; }
    public void setPressedRed(int red) { pressedRed = red; }
    public int getPressedGreen() { return pressedGreen; }
    public void setPressedGreen(int green) { pressedGreen = green; }
    public int getPressedBlue() { return pressedBlue; }
    public void setPressedBlue(int blue) { pressedBlue = blue; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isMouseButtons() { return mouseButtons; }
    public void setMouseButtons(boolean mouseButtons) { this.mouseButtons = mouseButtons; }
    public boolean isSpaceBar() { return spaceBar; }
    public void setSpaceBar(boolean spaceBar) { this.spaceBar = spaceBar; }
    public boolean isWASD() { return WASD; }
    public void setWASD(boolean WASD) { this.WASD = WASD; }
    public boolean isKeyBackground() { return keyBackground; }
    public void setKeyBackground(boolean keyBackground) { this.keyBackground = keyBackground; }
    public int getKeyBackgroundAlpha() { return keyBackgroundAlpha; }
    public void setKeyBackgroundAlpha(int keyBackgroundAlpha) { this.keyBackgroundAlpha = keyBackgroundAlpha; }
    public int getKeyBackgroundRed() { return keyBackgroundRed; }
    public void setKeyBackgroundRed(int keyBackgroundRed) { this.keyBackgroundRed = keyBackgroundRed; }
    public int getKeyBackgroundGreen() { return keyBackgroundGreen; }
    public void setKeyBackgroundGreen(int keyBackgroundGreen) { this.keyBackgroundGreen = keyBackgroundGreen; }
    public int getKeyBackgroundBlue() { return keyBackgroundBlue; }
    public void setKeyBackgroundBlue(int keyBackgroundBlue) { this.keyBackgroundBlue = keyBackgroundBlue; }
    public int getKeyBackgroundPressedRed() { return keyBackgroundPressedRed; }
    public void setKeyBackgroundPressedRed(int keyBackgroundPressedRed) { this.keyBackgroundPressedRed = keyBackgroundPressedRed; }
    public int getKeyBackgroundPressedGreen() { return keyBackgroundPressedGreen; }
    public void setKeyBackgroundPressedGreen(int keyBackgroundPressedGreen) { this.keyBackgroundPressedGreen = keyBackgroundPressedGreen; }
    public int getKeyBackgroundPressedBlue() { return keyBackgroundPressedBlue; }
    public void setKeyBackgroundPressedBlue(int keyBackgroundPressedBlue) { this.keyBackgroundPressedBlue = keyBackgroundPressedBlue; }
}
