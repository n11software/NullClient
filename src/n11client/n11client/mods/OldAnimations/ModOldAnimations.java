package n11client.mods.OldAnimations;

import n11client.mods.Mod;

import java.io.File;

public class ModOldAnimations extends Mod {

    private final OldAnimationsSettings config = new OldAnimationsSettings(this, new File("N11"));

    public OldAnimationsSettings getSettings() {
        return config;
    }
}
