package n11client.mods.keystrokes;

import n11client.gui.hud.HUDManager;
import n11client.gui.hud.ModListView;
import n11client.mods.ModInstances;
import n11client.mods.fps.ModFPS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeystrokesGUI extends GuiScreen {
    public HUDManager manager;
    ModKeystrokes mod = ModInstances.getKeyStrokes();
    public ModListView md;
    private ScrollList scrollList;
    private List<String> entries = new ArrayList<>();


    public KeystrokesGUI(ModListView md) {
        this.md = md;
    }

    @Override
    public void initGui() {
        super.initGui();
        entries.clear();
        entries.add("");
        entries.add("Mouse Buttons:");
        entries.add("Spacebar:");
        entries.add("WASD:");
        entries.add("Normal:");
        entries.add("Pressed:");
        entries.add("Normal Background:");
        entries.add("Pressed Background:");
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
        if (button.id == 0) {
            boolean enabled = !mod.isEnabled();
            mod.setEnabled(enabled);
            mod.getSettings().enabled = enabled;
            button.displayString = mod.isEnabled() ? "Disable" : "Enable";
            mod.getSettings().save();
        } else if (button.id == 1001) {
            boolean enabled = !mod.getSettings().mouseButtons;
            mod.getSettings().mouseButtons = enabled;
            mod.getSettings().setMouseButtons(enabled);
            mod.reloadMode();
            button.displayString = !mod.getSettings().mouseButtons ? "Enable" : "Disable";
            mod.getSettings().save();
        } else if (button.id == 1002) {
            boolean enabled = !mod.getSettings().spaceBar;
            mod.getSettings().spaceBar = enabled;
            mod.getSettings().setSpaceBar(enabled);
            mod.reloadMode();
            button.displayString = !mod.getSettings().spaceBar ? "Enable" : "Disable";
            mod.getSettings().save();
        } else if (button.id == 1003) {
            boolean enabled = !mod.getSettings().WASD;
            mod.getSettings().WASD = enabled;
            mod.getSettings().setWASD(enabled);
            mod.reloadMode();
            button.displayString = !mod.getSettings().WASD ? "Enable" : "Disable";
            mod.getSettings().save();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        scrollList.drawScreenNoDirt(mouseX, mouseY, partialTicks);
        drawCenteredString(mc.fontRendererObj, "Keystrokes", width / 2, 14, 0xFFFFFF);
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
        } else if (this.scrollList.pressedHex.isFocused()) {
            this.scrollList.pressedHex.textboxKeyTyped(typedChar, keyCode);

            // Validate and update color
            String inputColor = this.scrollList.pressedHex.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{6}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                mod.getSettings().pressedRed = red;
                mod.getSettings().pressedGreen = green;
                mod.getSettings().pressedBlue = blue;
            }
        } else if (this.scrollList.BG.isFocused()) {
            this.scrollList.BG.textboxKeyTyped(typedChar, keyCode);

            // Validate and update color
            String inputColor = this.scrollList.BG.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{8}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                int alpha = Integer.parseInt(inputColor.substring(7, 9), 16);
                mod.getSettings().keyBackgroundRed = red;
                mod.getSettings().keyBackgroundGreen = green;
                mod.getSettings().keyBackgroundBlue = blue;
                mod.getSettings().keyBackgroundAlpha = alpha;
            }
        } else if (this.scrollList.pressedBG.isFocused()) {
            this.scrollList.pressedBG.textboxKeyTyped(typedChar, keyCode);

            // Validate and update color
            String inputColor = this.scrollList.pressedBG.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{6}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                mod.getSettings().keyBackgroundPressedRed = red;
                mod.getSettings().keyBackgroundPressedGreen = green;
                mod.getSettings().keyBackgroundPressedBlue = blue;
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
        this.scrollList.pressedHex.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollList.BG.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollList.pressedBG.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private class ScrollList extends GuiSlot {

        private final List<GuiButton> buttons = new ArrayList<>();

        public GuiTextField hex = new GuiTextField(1, mc.fontRendererObj, 0, 0,  72, 20);
        public GuiTextField pressedHex = new GuiTextField(2, mc.fontRendererObj, 0, 0, 72, 20);
        public GuiTextField BG = new GuiTextField(3, mc.fontRendererObj, 0, 0, 72, 20);
        public GuiTextField pressedBG = new GuiTextField(4, mc.fontRendererObj, 0, 0, 72, 20);

        public ScrollList() {
            super(KeystrokesGUI.this.mc, KeystrokesGUI.this.width, KeystrokesGUI.this.height, 32, KeystrokesGUI.this.height, 25);
            KeystrokesGUI.this.buttonList.removeIf(b -> b.id >= 1000 && b.id < 1000 + entries.size());
            buttons.clear();
            for (int i = 0; i < entries.size(); i++) {
                String value = "";
                GuiButton button = new GuiButton(i+1000, 0, 0, 75, 20, value);
                buttons.add(button);
                KeystrokesGUI.this.buttonList.add(button);
                switch (i) {
                    case 0:
                        KeystrokesGUI.this.buttonList.get(i).displayString = "";
                        break;
                    case 1:
                        KeystrokesGUI.this.buttonList.get(i).displayString = !mod.getSettings().mouseButtons ? "Enable" : "Disable";
                        break;
                    case 2:
                        KeystrokesGUI.this.buttonList.get(i).displayString = !mod.getSettings().spaceBar ? "Enable" : "Disable";
                        break;
                    case 3:
                        KeystrokesGUI.this.buttonList.get(i).displayString = !mod.getSettings().WASD ? "Enable" : "Disable";
                        break;
                    case 4:
                        String hexColor = String.format("#%02x%02x%02x", mod.getSettings().red, mod.getSettings().green, mod.getSettings().blue);
                        hex.setText(hexColor);
                        hex.setMaxStringLength(7); // Hex color format: #RRGGBB
                        hex.setFocused(false);
                        KeystrokesGUI.this.buttonList.get(i).displayString = "";
                        break;
                    case 5:
                        String pressedHexColor = String.format("#%02x%02x%02x", mod.getSettings().pressedRed, mod.getSettings().pressedGreen, mod.getSettings().pressedBlue);
                        pressedHex.setText(pressedHexColor);
                        pressedHex.setMaxStringLength(7); // Hex color format: #RRGGBB
                        pressedHex.setFocused(false);
                        KeystrokesGUI.this.buttonList.get(i).displayString = "";
                        break;
                    case 6:
                        String bgColor = String.format("#%02x%02x%02x%02x", mod.getSettings().keyBackgroundRed, mod.getSettings().keyBackgroundGreen, mod.getSettings().keyBackgroundBlue, mod.getSettings().keyBackgroundAlpha);
                        BG.setText(bgColor);
                        BG.setMaxStringLength(9); // Hex color format: #RRGGBB
                        BG.setFocused(false);
                        KeystrokesGUI.this.buttonList.get(i).displayString = "";
                        break;
                    case 7:
                        String pressedBgColor = String.format("#%02x%02x%02x", mod.getSettings().keyBackgroundPressedRed, mod.getSettings().keyBackgroundPressedGreen, mod.getSettings().keyBackgroundPressedBlue);
                        pressedBG.setText(pressedBgColor);
                        pressedBG.setMaxStringLength(7); // Hex color format: #RRGGBB
                        pressedBG.setFocused(false);
                        KeystrokesGUI.this.buttonList.get(i).displayString = "";
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

            if (index == 4) {
                // Render input field
                hex.xPosition = this.width - 35 - hex.getWidth();
                hex.yPosition = y;
                hex.drawTextBox();
            } else if (index == 5) {
                // Render input field
                pressedHex.xPosition = this.width - 35 - pressedHex.getWidth();
                pressedHex.yPosition = y;
                pressedHex.drawTextBox();
            } else if (index == 6) {
                // Render input field
                BG.xPosition = this.width - 35 - BG.getWidth();
                BG.yPosition = y;
                BG.drawTextBox();
            } else if (index == 7) {
                // Render input field
                pressedBG.xPosition = this.width - 35 - pressedBG.getWidth();
                pressedBG.yPosition = y;
                pressedBG.drawTextBox();
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
