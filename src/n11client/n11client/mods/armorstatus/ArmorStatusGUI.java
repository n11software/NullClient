package n11client.mods.armorstatus;

import n11client.gui.hud.HUDManager;
import n11client.gui.hud.ModListView;
import n11client.mods.ModInstances;
import n11client.mods.armorstatus.ModArmorStatus;
import n11client.mods.clock.Clock;
import n11client.mods.cps.CPS;
import n11client.mods.fps.ModFPS;
import n11client.mods.keystrokes.ModKeystrokes;
import n11client.mods.togglesprintsneak.ModToggleSprintSneak;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.ItemSelectable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArmorStatusGUI extends GuiScreen {
    public HUDManager manager;
    ModArmorStatus mod = ModInstances.getArmorStatus();
    public ModListView md;
    private ScrollList scrollList;
    private List<String> entries = new ArrayList<>();


    public ArmorStatusGUI(ModListView md) {
        this.md = md;
    }

    @Override
    public void initGui() {
        super.initGui();
        entries.clear();
        entries.add("");
        entries.add("Show the durability bar:");
        entries.add("Show the durability value:");
        entries.add("Direction:");
        entries.add("Alignment:");
        entries.add("Item Count:");
        entries.add("Hex Color:");
        scrollList = new ScrollList();
        this.buttonList.add(new GuiButton(0, this.width -75 -15, 10, 75, 20, mod.isEnabled() ? "Disable" : "Enable"));
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
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1001) {
            boolean isEnabled = !mod.getSettings().durability;
            mod.getSettings().durability = isEnabled;
            mod.isShowingDurability = isEnabled;
            mod.getSettings().save();
            button.displayString = mod.isShowingDurability ? "Disable" : "Enable";
        } else if (button.id == 1002) {
            boolean isEnabled = !mod.isShowingDurabilityText;
            mod.getSettings().durabilityText = isEnabled;
            mod.isShowingDurabilityText = isEnabled;
            mod.getSettings().save();
            button.displayString = mod.isShowingDurabilityText ? "Disable" : "Enable";
        } else if (button.id == 1003) {
            boolean isEnabled = !mod.getSettings().vertical;
            mod.getSettings().vertical = isEnabled;
            mod.isVertical = isEnabled;
            mod.getSettings().save();
            button.displayString = mod.isVertical ? "Vertical" : "Horizontal";
        } else if (button.id == 1004) {
            boolean isEnabled = !mod.getSettings().rightAligned;
            mod.getSettings().rightAligned = isEnabled;
            mod.isRightAligned = isEnabled;
            mod.getSettings().save();
            button.displayString = mod.isRightAligned ? "Right" : "Left";
        } else if (button.id == 1005) {
            boolean isEnabled = !mod.getSettings().itemCount;
            mod.getSettings().itemCount = isEnabled;
            mod.showItemCount = isEnabled;
            mod.getSettings().save();
            button.displayString = mod.showItemCount ? "Disable" : "Enable";
        } else if (button.id == 0) {
            boolean isEnabled = !mod.isEnabled();
            mod.setEnabled(isEnabled);
            mod.getSettings().enabled = isEnabled;
            button.displayString = mod.isEnabled() ? "Disable" : "Enable";
            mod.getSettings().save();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        scrollList.drawScreenNoDirt(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRendererObj, "Armor Status", width / 2, 14, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(md);
            return;
        }

        // Update text field based on key input
        if (this.scrollList.hex.isFocused()) {
            this.scrollList.hex.textboxKeyTyped(typedChar, keyCode);

            // Validate and update color
            String inputColor = this.scrollList.hex.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{6}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                mod.getSettings().red = red;
                mod.getSettings().green = green;
                mod.getSettings().blue = blue;
            }
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

        this.scrollList.hex.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private class ScrollList extends GuiSlot {

        private final List<GuiButton> buttons = new ArrayList<>();

        public GuiTextField hex = new GuiTextField(1, mc.fontRendererObj, 0, 0,  72, 20);

        public ScrollList() {
            super(ArmorStatusGUI.this.mc, ArmorStatusGUI.this.width, ArmorStatusGUI.this.height, 32, ArmorStatusGUI.this.height, 25);
            ArmorStatusGUI.this.buttonList.removeIf(b -> b.id >= 1000 && b.id < 1000 + entries.size());
            buttons.clear();
            for (int i = 0; i < entries.size(); i++) {
                String value = "";
                GuiButton button = new GuiButton(i+1000, 0, 0, 75, 20, value);
                buttons.add(button);
                ArmorStatusGUI.this.buttonList.add(button);
                switch (i) {
                    case 0:
                        ArmorStatusGUI.this.buttonList.get(i).displayString = "";
                        break;
                    case 1:
                        ArmorStatusGUI.this.buttonList.get(i).displayString = mod.isShowingDurability ? "Disable": "Enable";
                        break;
                    case 2:
                        ArmorStatusGUI.this.buttonList.get(i).displayString = mod.isShowingDurabilityText ? "Disable": "Enable";
                        break;
                    case 3:
                        ArmorStatusGUI.this.buttonList.get(i).displayString = mod.isVertical ? "Vertical": "Horizontal";
                        break;
                    case 4:
                        ArmorStatusGUI.this.buttonList.get(i).displayString = mod.isRightAligned ? "Right": "Left";
                        break;
                    case 5:
                        ArmorStatusGUI.this.buttonList.get(i).displayString = mod.showItemCount ? "Disable": "Enable";
                        break;
                    case 6:
                        String hexColor = String.format("#%02x%02x%02x", mod.getSettings().red, mod.getSettings().green, mod.getSettings().blue);
                        hex.setText(hexColor);
                        hex.setMaxStringLength(7); // Hex color format: #RRGGBB
                        hex.setFocused(false);
                        ArmorStatusGUI.this.buttonList.get(i).displayString = "";
                        break;
                }
            }
        }

        @Override
        protected int getSize() {
            return entries.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
            if (entries.get(index).contentEquals("")) return;
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

            // Render options here
            drawString(mc.fontRendererObj, entries.get(index), 25, y, 0xFFFFFF);

            if (buttons.get(index).displayString.isEmpty()) {
                buttons.get(index).setWidth(0);
                buttons.get(index).drawButton(mc, mouseX, mouseY);
            } else {
                buttons.get(index).drawButton(mc, mouseX, mouseY);
                buttons.get(index).xPosition = this.width - 25 - buttons.get(index).getButtonWidth();
                buttons.get(index).yPosition = y;
            }

            if (index == 6) {
                // Render input field
                hex.xPosition = this.width - 35 - hex.getWidth();
                hex.yPosition = y;
                hex.drawTextBox();
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }

    }


    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
