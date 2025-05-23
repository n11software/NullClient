package n11client.gui.hud;

import n11client.mods.ModInstances;
import n11client.mods.armorstatus.ArmorStatusGUI;
import n11client.mods.clock.ClockGUI;
import n11client.mods.cps.CPSGUI;
import n11client.mods.fps.FPSGUI;
import n11client.mods.keystrokes.KeystrokesGUI;
import n11client.mods.ping.PingGUI;
import n11client.mods.togglesprintsneak.ToggleSprintSneakGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.ItemSelectable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModListView extends GuiScreen {
	public HUDManager manager;
	public ModListView(HUDManager manager) {
		this.manager = manager;
	}
    private ScrollList scrollList;
    private List<String> entries = new ArrayList<>();

    @Override
    public void initGui() {
        super.initGui();
        entries.clear();
        entries.add("");
        entries.add("");
        entries.add("Armor Status");
        entries.add("Clock");
        entries.add("CPS");
        entries.add("FPS");
        entries.add("Keystrokes");
        entries.add("Toggle Sprint/Sneak");
        entries.add("Ping");
        entries.add("Bossbar");
        entries.add("Old Animations");
        entries.add("Windowed Fullscreen");
        entries.add("Fullbright");
        entries.add("");
        entries.add("");
        scrollList = new ScrollList();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            if (scrollAmount > 0) scrollAmount = -1;
            else if (scrollAmount < 0) scrollAmount = 1;
            scrollList.scrollBy(scrollAmount * 20);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException { }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        scrollList.drawScreenNoDirt(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRendererObj, "Mod List", width / 2, 14, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(new HUDConfigScreen(manager));
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int clickedIndex = scrollList.getSlotIndexFromScreenCoords(mouseX, mouseY);
        if (clickedIndex >= 0) {
            scrollList.elementClicked(clickedIndex, false, mouseX, mouseY);
        }
    }

    private class ScrollList extends GuiSlot {

        public ScrollList() {
            super(ModListView.this.mc, ModListView.this.width, ModListView.this.height, 32, ModListView.this.height, 20);
        }

        @Override
        public void handleMouseInput() {
            super.handleMouseInput();
        }

        @Override
        protected int getSize() {
            return entries.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
            if (entries.get(index).contentEquals("")) return;
            switch (entries.get(index)) {
                case "Armor Status":
                    mc.displayGuiScreen(new ArmorStatusGUI(new ModListView(manager)));
                    break;
                case "Clock":
                    mc.displayGuiScreen(new ClockGUI(new ModListView(manager)));
                    break;
                case "CPS":
                    mc.displayGuiScreen(new CPSGUI(new ModListView(manager)));
                    break;
                case "FPS":
                    mc.displayGuiScreen(new FPSGUI(new ModListView(manager)));
                    break;
                case "Keystrokes":
                    mc.displayGuiScreen(new KeystrokesGUI(new ModListView(manager)));
                    break;
                case "Toggle Sprint/Sneak":
                    mc.displayGuiScreen(new ToggleSprintSneakGUI(new ModListView(manager)));
                    break;
                case "Ping":
                    mc.displayGuiScreen(new PingGUI(new ModListView(manager)));
                    break;
                case "Bossbar":
                    ModInstances.getBossbar().setEnabled(!ModInstances.getBossbar().isEnabled());
                    break;
                case "Old Animations":
                    ModInstances.getOldAnimations().setEnabled(!ModInstances.getOldAnimations().isEnabled());
                    break;
                case "Windowed Fullscreen":
                    ModInstances.getWindowedFullscreen().setEnabled(!ModInstances.getWindowedFullscreen().isEnabled());
                    break;
                case "Fullbright":
                    ModInstances.getFullBright().setEnabled(!ModInstances.getFullBright().isEnabled());
                    break;
            }
            // play sound
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
//            mc.displayGuiScreen(new ModView(new ModListView(manager), entries.get(index)));
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {}

        @Override
        protected void drawSlot(int index, int x, int y, int height, int mouseX, int mouseY) {
        	RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.pushMatrix();

            RenderHelper.enableGUIStandardItemLighting();
            int color = 0xFF5555;
            switch (index) {
            case 2:
            	this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.diamond_chestplate), (width / 2) - 80, y-4);
                color = ModInstances.getArmorStatus().isEnabled()?0x55FF55 : 0xFF5555;
            	break;
            case 3:
            	this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.clock), (width / 2) - 80, y-4);
                color = ModInstances.getClockMod().isEnabled()?0x55FF55 : 0xFF5555;
            	break;
            case 4:
            	this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.stone_button)), (width / 2) - 80, y-4);
                color = ModInstances.getCPSMod().isEnabled()?0x55FF55 : 0xFF5555;
            	break;
            case 5:
            	this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.redstone), (width / 2) - 80, y-4);
                color = ModInstances.getFPS().isEnabled()?0x55FF55 : 0xFF5555;
            	break;
            case 6:
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.noteblock)), (width / 2) - 80, y-4);
                color = ModInstances.getKeyStrokes().isEnabled()?0x55FF55 : 0xFF5555;
                break;
            case 7:
            	ItemStack speedPotion = new ItemStack(Items.potionitem, 1, 8194);
            	this.mc.getRenderItem().renderItemIntoGUI(speedPotion, (width / 2) - 80, y-4);
                color = ModInstances.getToggleSprintSneak().isEnabled()?0x55FF55 : 0xFF5555;
            	break;
            case 8:
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.compass), (width / 2) - 80, y-4);
                color = ModInstances.getFPS().isEnabled()?0x55FF55 : 0xFF5555;
                break;
            case 9:
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(Blocks.dragon_egg)), (width / 2) - 80, y-4);
                color = ModInstances.getBossbar().isEnabled()?0x55FF55 : 0xFF5555;
                break;
            case 10:
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.iron_sword), (width / 2) - 80, y-4);
                color = ModInstances.getOldAnimations().isEnabled()?0x55FF55 : 0xFF5555;
                break;
            case 11:
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.painting), (width / 2) - 80, y-4);
                color = ModInstances.getWindowedFullscreen().isEnabled()?0x55FF55 : 0xFF5555;
                break;
            case 12:
                this.mc.getRenderItem().renderItemIntoGUI(new ItemStack(Items.diamond), (width / 2) - 80, y-4);
                color = ModInstances.getFullBright().isEnabled()?0x55FF55 : 0xFF5555;
                break;
            }
            drawCenteredString(mc.fontRendererObj, entries.get(index), width / 2, y, color);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
