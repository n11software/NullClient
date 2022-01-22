package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.impl.ModHelloWorld;

public class ModInstances {

    private static ModHelloWorld HelloWorld;

    public static  void register(HUDManager manager) {
        HelloWorld = new ModHelloWorld();
        manager.register(HelloWorld);
    }

    public static ModHelloWorld getHelloWorld() { return HelloWorld; }
}
