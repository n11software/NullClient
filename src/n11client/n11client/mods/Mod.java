package n11client.mods;

import n11client.Client;
import n11client.event.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Mod {

    private boolean isEnabled = true;
    protected final Minecraft mc;
    protected final FontRenderer font;
    protected final Client client;

    public Mod() {
        this.mc = Minecraft.getMinecraft();
        this.font = mc.fontRendererObj;
        this.client = Client.getInstance();

        setEnabled(isEnabled);
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (isEnabled) EventManager.register(this);
        else EventManager.unregister(this);
    }

    public boolean isEnabled() { return isEnabled; }

}
