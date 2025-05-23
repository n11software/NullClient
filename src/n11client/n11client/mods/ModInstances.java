package n11client.mods;

import n11client.gui.hud.HUDManager;
import n11client.mods.FullBright.ModFullBright;
import n11client.mods.OldAnimations.ModOldAnimations;
import n11client.mods.WindowedFullscreen.ModWindowedFullscreen;
import n11client.mods.armorstatus.ModArmorStatus;
import n11client.mods.bossbar.ModBossbar;
import n11client.mods.clock.Clock;
import n11client.mods.cps.CPS;
import n11client.mods.fps.ModFPS;
import n11client.mods.keystrokes.ModKeystrokes;
import n11client.mods.ping.ModPing;
import n11client.mods.togglesprintsneak.ModToggleSprintSneak;
import n11client.mods.togglesprintsneak.ToggleSprintSneakSettings;
import n11client.utils.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
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

import java.util.Timer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.TimerTask;

public class ModInstances {

    private static ModArmorStatus ArmorStatus;
    private static ModFPS FPS;
    private static ModKeystrokes KeyStrokes;
    private static ModToggleSprintSneak ToggleSprintSneak;
    private static Clock ClockMod;
    private static CPS CPSMod;
    private static ModPing Ping;
    private static ModBossbar Bossbar;
    private static ModOldAnimations oldAnimations;
    public static ModWindowedFullscreen WindowedFullscreen;
    public static ModFullBright FullBright;

    public static void sendPing() throws IOException {
        ServerAddress addr = ServerAddress.fromString(Minecraft.getMinecraft().getCurrentServerData().serverIP);
        final NetworkManager networkmanager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(addr.getIP()), addr.getPort(), false);
        networkmanager.setNetHandler(new INetHandlerStatusClient() {
            @Override
            public void onDisconnect(IChatComponent reason) {
            }

            long tBefore = 0L;
            @Override
            public void handleServerInfo(S00PacketServerInfo packetIn) {
                tBefore = Minecraft.getSystemTime();
                networkmanager.sendPacket(new C01PacketPing(tBefore));
            }

            @Override
            public void handlePong(S01PacketPong packetIn) {
                Ping.latency = Minecraft.getSystemTime() - tBefore;
                networkmanager.closeChannel(new ChatComponentText("Finished"));
            }
        });
        networkmanager.sendPacket(new C00Handshake(47, addr.getIP(), addr.getPort(), EnumConnectionState.STATUS));
        networkmanager.sendPacket(new C00PacketServerQuery());
    }

    public static class PingTimer extends TimerTask {
        @Override
        public void run() {
            if (Minecraft.getMinecraft().theWorld != null) {
                try {
                    if (Minecraft.getMinecraft().getIntegratedServer() == null) {
                        sendPing();
                    }
                    else{
                        Ping.latency = 0;
                    }
                } catch (Throwable err) {
                    err.printStackTrace();
                }
            }
        }
    }

    public static void register(HUDManager manager) {
        ArmorStatus = new ModArmorStatus();
        ArmorStatus.setEnabled(ArmorStatus.getSettings().enabled);
        manager.register(ArmorStatus);
        FPS = new ModFPS();
        FPS.setEnabled(FPS.getSettings().enabled);
        manager.register(FPS);
        KeyStrokes = new ModKeystrokes();
        KeyStrokes.setEnabled(KeyStrokes.getSettings().enabled);
        manager.register(KeyStrokes);
        ToggleSprintSneak = new ModToggleSprintSneak();
        ToggleSprintSneak.setEnabled(ToggleSprintSneak.getSettings().enabled);
        manager.register(ToggleSprintSneak);
        ClockMod = new Clock();
        ClockMod.setEnabled(ClockMod.getSettings().enabled);
        manager.register(ClockMod);
        CPSMod = new CPS();
        CPSMod.setEnabled(CPSMod.getSettings().enabled);
        manager.register(CPSMod);
        Ping = new ModPing();
        Ping.setEnabled(Ping.getSettings().enabled);
        manager.register(Ping);
        new Timer().schedule(new PingTimer(), 0, 2000);
        Bossbar = new ModBossbar();
        Bossbar.setEnabled(Bossbar.getSettings().enabled);
        oldAnimations = new ModOldAnimations();
        oldAnimations.setEnabled(oldAnimations.getSettings().enabled);
        WindowedFullscreen = new ModWindowedFullscreen();
        WindowedFullscreen.setEnabled(WindowedFullscreen.getSettings().enabled);
        FullBright = new ModFullBright();
        FullBright.getSettings().setEnabled(FullBright.getSettings().enabled);
    }

    public static void ResizeEvent() {
        if (ArmorStatus != null) ArmorStatus.ResizeEvent();
        if (FPS != null) FPS.ResizeEvent();
        if (KeyStrokes != null) KeyStrokes.ResizeEvent();
        if (ToggleSprintSneak != null) ToggleSprintSneak.ResizeEvent();
        if (ClockMod != null) ClockMod.ResizeEvent();
        if (CPSMod != null) CPSMod.ResizeEvent();
        if (Ping != null) Ping.ResizeEvent();
    }

    public static void unregister() {
        getArmorStatus().getSettings().pos = getArmorStatus().getPos();
        getArmorStatus().getSettings().enabled = getArmorStatus().isEnabled();
        getArmorStatus().getSettings().itemCount = getArmorStatus().showItemCount;
        getArmorStatus().getSettings().vertical = getArmorStatus().isVertical;
        getArmorStatus().getSettings().rightAligned = getArmorStatus().isRightAligned;
        getArmorStatus().getSettings().durability = getArmorStatus().isShowingDurability;
        getArmorStatus().getSettings().durabilityText = getArmorStatus().isShowingDurabilityText;
        getArmorStatus().getSettings().save();
        getKeyStrokes().getSettings().setPos(getKeyStrokes().getPos());
        getKeyStrokes().getSettings().enabled = getKeyStrokes().isEnabled();
        getKeyStrokes().getSettings().save();
        getToggleSprintSneak().getSettings().pos = getToggleSprintSneak().getPos();
        getToggleSprintSneak().getSettings().enabled = getToggleSprintSneak().isEnabled();
        getToggleSprintSneak().getSettings().sneakToggle = getToggleSprintSneak().isSneakToggle;
        getToggleSprintSneak().getSettings().sprintToggle = getToggleSprintSneak().isSprintToggle;
        getToggleSprintSneak().getSettings().save();
        getFPS().getSettings().pos = getFPS().getPos();
        getFPS().getSettings().enabled = getFPS().isEnabled();
        getFPS().getSettings().save();
        getCPSMod().getSettings().pos = getCPSMod().getPos();
        getCPSMod().getSettings().enabled = getCPSMod().isEnabled();
        getCPSMod().getSettings().save();
        getClockMod().getSettings().pos = getClockMod().getPos();
        getClockMod().getSettings().enabled = getClockMod().isEnabled();
        getClockMod().getSettings().hr24 = getClockMod().is24hr();
        getClockMod().getSettings().save();
        getPing().getSettings().pos = getPing().getPos();
        getPing().getSettings().enabled = getPing().isEnabled();
        getPing().getSettings().save();
        getBossbar().getSettings().enabled = getBossbar().isEnabled();
        getBossbar().getSettings().save();
        getOldAnimations().getSettings().enabled = getOldAnimations().isEnabled();
        getOldAnimations().getSettings().save();
        getWindowedFullscreen().getSettings().enabled = getWindowedFullscreen().isEnabled();
        getWindowedFullscreen().getSettings().save();
        getFullBright().getSettings().enabled = getFullBright().isEnabled();
        getFullBright().getSettings().save();
    }

    public static ModArmorStatus getArmorStatus() { return ArmorStatus; }
    public static ModFPS getFPS() { return FPS; }
    public static ModKeystrokes getKeyStrokes() { return KeyStrokes; }
    public static ModToggleSprintSneak getToggleSprintSneak() { return ToggleSprintSneak; }
    public static Clock getClockMod() { return ClockMod; }
    public static CPS getCPSMod() { return CPSMod; }
    public static ModPing getPing() { return Ping; }
    public static ModBossbar getBossbar() { return Bossbar; }
    public static ModOldAnimations getOldAnimations() { return oldAnimations; }
    public static ModWindowedFullscreen getWindowedFullscreen() { return WindowedFullscreen; }
    public static ModFullBright getFullBright() { return FullBright; }
}
