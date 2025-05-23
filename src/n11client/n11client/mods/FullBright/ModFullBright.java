package n11client.mods.FullBright;

import n11client.mods.Mod;

import java.io.File;

public class ModFullBright extends Mod {

    private final FullBrightSettings config = new FullBrightSettings(this, new File("N11"));

    public FullBrightSettings getSettings() {
        return config;
    }
}
