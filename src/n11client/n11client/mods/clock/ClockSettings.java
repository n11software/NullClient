package n11client.mods.clock;

import com.google.gson.Gson;
import n11client.gui.hud.RelativePosition;
import n11client.mods.cps.CPS;
import n11client.utils.Log;
import n11client.utils.N11JsonObject;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ClockSettings {
    public Clock theMod;
    public File configFile;
    public RelativePosition pos = new RelativePosition(1, -Minecraft.getMinecraft().fontRendererObj.getStringWidth(new SimpleDateFormat("HH:mm").format(new java.util.Date()))/2-1, 20);
    public boolean enabled = true;
    public boolean hr24 = true;

    public int red = 255;
    public int green = 255;
    public int blue = 255;

    Gson gson = new Gson();

    public ClockSettings(Clock mod, File directory) {
        if (!directory.exists()) directory.mkdirs();
        theMod = mod;
        configFile = new File(directory, "clock.json");
        this.load();
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
            object.addProperty("sector", pos.getSector());
            object.addProperty("x", pos.getX());
            object.addProperty("y", pos.getY());
            object.addProperty("red", red);
            object.addProperty("green", green);
            object.addProperty("blue", blue);
            object.addProperty("enabled", enabled);
            object.addProperty("24hr", hr24);
            object.writeToFile(configFile);
        } catch (Exception ex) {
            Log.warn(String.format("Could not save config file! (\"%s\")", configFile.getName()));
        }
    }

    public void parseSettings(N11JsonObject object) {
        pos = new RelativePosition(object.optInt("sector"), object.optInt("x"), object.optInt("y"));
        red = object.optInt("red");
        green = object.optInt("green");
        blue = object.optInt("blue");
        enabled = object.optBoolean("enabled");
        hr24 = object.optBoolean("24hr");
    }
}
