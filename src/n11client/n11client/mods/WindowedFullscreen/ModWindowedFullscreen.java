package n11client.mods.WindowedFullscreen;

import n11client.mods.Mod;

import java.io.File;

public class ModWindowedFullscreen extends Mod {

    private final WindowedFullscreenSettings config = new WindowedFullscreenSettings(this, new File("N11"));

    public WindowedFullscreenSettings getSettings() {
        return config;
    }
}
