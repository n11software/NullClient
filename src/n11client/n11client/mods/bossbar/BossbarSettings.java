package n11client.mods.bossbar;

import com.google.gson.Gson;
import n11client.gui.hud.RelativePosition;
import n11client.utils.Log;
import n11client.utils.N11JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class BossbarSettings {
    public ModBossbar theMod;
    public File configFile;
    public boolean enabled = false;

    Gson gson = new Gson();

    public BossbarSettings(ModBossbar mod, File directory) {
        if (!directory.exists()) directory.mkdirs();
        theMod = mod;
        configFile = new File(directory, "bossbar.json");
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
            object.addProperty("enabled", enabled);
            object.writeToFile(configFile);
        } catch (Exception ex) {
            Log.warn(String.format("Could not save config file! (\"%s\")", configFile.getName()));
        }
    }

    public void parseSettings(N11JsonObject object) {
        enabled = object.optBoolean("enabled");
    }
}
