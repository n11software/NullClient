package n11client.mods.ping;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class ModPing extends ModDraggable {

    private final PingSettings config = new PingSettings(this, new File("N11"));

    private ScreenPosition pos = ScreenPosition.fromRelative(config.pos);

    public PingSettings getSettings() {
        return config;
    }

    public RelativePosition getPos() { return pos.getRelativePos(); }

    public void ResizeEvent() {
        pos.setRelativePos(new RelativePosition(pos.getRelativePos().getSector(), pos.getRelativePos().getX(), pos.getRelativePos().getY()));
    }

    @Override
    public void save(ScreenPosition pos) {
        this.pos = pos;
    }

    @Override
    public ScreenPosition load() {
        return pos;
    }

    public long latency = 0;

    @Override
    public int getWidth() {
        return font.getStringWidth("Ping: " + latency);
    }

    @Override
    public int getHeight() {
        return font.FONT_HEIGHT;
    }

    @Override
    public void render(ScreenPosition pos) {
        font.drawStringWithShadow("Ping: " + latency, pos.getAbsoluteX(), pos.getAbsoluteY(), new Color(config.red, config.green, config.blue).getRGB());
    }
}
