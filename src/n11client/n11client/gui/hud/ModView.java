package n11client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import n11client.mods.ModInstances;
import n11client.mods.armorstatus.ModArmorStatus;
import n11client.mods.clock.Clock;
import n11client.mods.clock.ClockSettings;
import n11client.mods.cps.CPS;
import n11client.mods.fps.ModFPS;
import n11client.mods.keystrokes.ModKeystrokes;
import n11client.mods.keystrokes.ModKeystrokes.Key;
import n11client.mods.keystrokes.ModKeystrokes.KeyStrokesMode;
import n11client.mods.togglesprintsneak.ModToggleSprintSneak;

import java.awt.ItemSelectable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModView extends GuiScreen {
	private ModListView md;
	private String ModName;
	public ModView(ModListView md, String ModName) {
		this.md = md;
		this.ModName = ModName;
	}
    private ScrollList scrollList;
    private List<String> entries = new ArrayList<>();

    private GuiTextField hex, hex2, hex3, hex4;
    
    @Override
    public void initGui() {
        super.initGui();
        entries.clear();
        md.manager.getRegisteredRenderers().forEach(renderer -> {
			if (renderer instanceof ModArmorStatus && ModName.equals("Armor Status")) {
				ModArmorStatus armorStatus = (ModArmorStatus) renderer;
				if (armorStatus.isEnabled()) {
					this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
				}
				// Enable durability
				drawString(mc.fontRendererObj, "Show the durability bar:", 15, 65, 0xFFFFFF);
				if (armorStatus.isShowingDurability) {
					this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
				}
				// Enable durability value
				drawString(mc.fontRendererObj, "Show the durability value:", 15, 90, 0xFFFFFF);
				if (armorStatus.isShowingDurabilityText) {
					this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
				}
				// Enable direction
				drawString(mc.fontRendererObj, "Direction:", 15, 115, 0xFFFFFF);
				if (armorStatus.isVertical) {
					this.buttonList.add(new GuiButton(6, this.width - 90, 110, 75, 20, "Vertical"));
				} else {
					this.buttonList.add(new GuiButton(7, this.width - 90, 110, 75, 20, "Horizontal"));
				}
				// Enable alignment
				drawString(mc.fontRendererObj, "Alignment:", 15, 140, 0xFFFFFF);
				if (!armorStatus.isRightAligned) {
					this.buttonList.add(new GuiButton(8, this.width - 90, 135, 75, 20, "Left"));
				} else {
					this.buttonList.add(new GuiButton(9, this.width - 90, 135, 75, 20, "Right"));
				}
				// Enable alignment
				drawString(mc.fontRendererObj, "Item Count:", 15, 165, 0xFFFFFF);
				if (!armorStatus.showItemCount) {
					this.buttonList.add(new GuiButton(10, this.width - 90, 160, 75, 20, "Enable"));
				} else {
					this.buttonList.add(new GuiButton(11, this.width - 90, 160, 75, 20, "Disable"));
				}
				
				// Hex Color Picker
				drawString(mc.fontRendererObj, "Hex Color:", 15, 190, 0xFFFFFF);
				// add input box for hex color
				hex = new GuiTextField(1, this.mc.fontRendererObj, this.width - 90, 185, 75, 20);
				String hexColor = String.format("#%02x%02x%02x", armorStatus.getSettings().red, armorStatus.getSettings().green, armorStatus.getSettings().blue);
				hex.setText(hexColor);
				hex.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex.setFocused(false);
			} else if (renderer instanceof Clock && ModName.equals("Clock")) {
				Clock clock = (Clock) renderer;
				if (clock.isEnabled()) {
					this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
				}
				// Enable 24 hour format
				if (clock.getSettings().hr24) {
					this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
				}

				hex = new GuiTextField(1, this.mc.fontRendererObj, this.width - 90, 85, 75, 20);
				String hexColor = String.format("#%02x%02x%02x", clock.getSettings().red, clock.getSettings().green, clock.getSettings().blue);
				hex.setText(hexColor);
				hex.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex.setFocused(false);
			} else if (renderer instanceof CPS && ModName.equals("CPS")) {
				CPS cps = (CPS) renderer;
				if (cps.isEnabled()) {
					this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
				}
				// left
				if (cps.getSettings().left) {
					this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
				}
				// right
				if (cps.getSettings().right) {
					this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
				}
				hex = new GuiTextField(1, this.mc.fontRendererObj, this.width - 90, 110, 75, 20);
				String hexColor = String.format("#%02x%02x%02x", cps.getSettings().red, cps.getSettings().green, cps.getSettings().blue);
				hex.setText(hexColor);
				hex.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex.setFocused(false);
			} else if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
				ModKeystrokes keystrokes = (ModKeystrokes) renderer;
				if (keystrokes.isEnabled()) {
					this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
				}
				// Mouse Buttons
				if (keystrokes.getSettings().mouseButtons) {
					this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
				}
				// Spacebar
				if (keystrokes.getSettings().spaceBar) {
					this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
				}
				// WASD
				if (keystrokes.getSettings().WASD) {
					this.buttonList.add(new GuiButton(6, this.width - 90, 110, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(7, this.width - 90, 110, 75, 20, "Enable"));
				}

				hex = new GuiTextField(1, this.mc.fontRendererObj, this.width - 90, 135, 75, 20);
				String hexColor = String.format("#%02x%02x%02x", keystrokes.getSettings().red, keystrokes.getSettings().green, keystrokes.getSettings().blue);
				hex.setText(hexColor);
				hex.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex.setFocused(false);

				hex2 = new GuiTextField(2, this.mc.fontRendererObj, this.width - 90, 160, 75, 20);
				hexColor = String.format("#%02x%02x%02x", keystrokes.getSettings().pressedRed, keystrokes.getSettings().pressedGreen, keystrokes.getSettings().pressedBlue);
				hex2.setText(hexColor);
				hex2.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex2.setFocused(false);

				hex3 = new GuiTextField(3, this.mc.fontRendererObj, this.width - 90, 185, 75, 20);
				hexColor = String.format("#%02x%02x%02x%02x", keystrokes.getSettings().keyBackgroundRed, keystrokes.getSettings().keyBackgroundGreen, keystrokes.getSettings().keyBackgroundBlue, keystrokes.getSettings().keyBackgroundAlpha);
				hex3.setText(hexColor);
				hex3.setMaxStringLength(9); // Hex color format: #RRGGBB
			    hex3.setFocused(false);

				hex4 = new GuiTextField(3, this.mc.fontRendererObj, this.width - 90, 210, 75, 20);
				hexColor = String.format("#%02x%02x%02x", keystrokes.getSettings().keyBackgroundPressedRed, keystrokes.getSettings().keyBackgroundPressedGreen, keystrokes.getSettings().keyBackgroundPressedBlue);
				hex4.setText(hexColor);
				hex4.setMaxStringLength(9); // Hex color format: #RRGGBB
			    hex4.setFocused(false);
			} else if (renderer instanceof ModToggleSprintSneak && ModName.equals("Toggle Sprint/Sneak")) {
				ModToggleSprintSneak toggle = (ModToggleSprintSneak) renderer;
				if (toggle.isEnabled()) {
					this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
				}
				// Sprint Toggle
				if (toggle.getSettings().sprintToggle) {
					this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
				}
				// Sneak Toggle
				if (toggle.getSettings().sneakToggle) {
					this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
				}
				// Hex Color Picker
				hex = new GuiTextField(1, this.mc.fontRendererObj, this.width - 90, 110, 75, 20);
				String hexColor = String.format("#%02x%02x%02x", toggle.getSettings().red, toggle.getSettings().green, toggle.getSettings().blue);
				hex.setText(hexColor);
				hex.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex.setFocused(false);
			} else if (renderer instanceof ModFPS && ModName.equals("FPS")) {
				ModFPS fps = (ModFPS) renderer;
				if (fps.isEnabled()) {
					this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
				} else {
					this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
				}
				
				// Hex Color Picker
				hex = new GuiTextField(1, this.mc.fontRendererObj, this.width - 90, 60, 75, 20);
				String hexColor = String.format("#%02x%02x%02x", fps.getSettings().red, fps.getSettings().green, fps.getSettings().blue);
				hex.setText(hexColor);
				hex.setMaxStringLength(7); // Hex color format: #RRGGBB
			    hex.setFocused(false);
			}
		});
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
    protected void actionPerformed(GuiButton button) throws IOException {
			md.manager.getRegisteredRenderers().forEach(renderer -> {
				if (renderer instanceof ModArmorStatus && ModName.equals("Armor Status")) {
					ModArmorStatus armorStatus = (ModArmorStatus) renderer;
		        	System.out.println("Button clicked: " + button.displayString);
					if (button.displayString.equals("Disable") && button.id == 0) {
						armorStatus.setEnabled(false);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
					} else if (button.id == 1) {
						armorStatus.setEnabled(true);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
					}
					// Enable durability
					if (button.displayString.equals("Disable") && button.id == 2) {
						armorStatus.isShowingDurability = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
					} else if (button.id == 3) {
						armorStatus.isShowingDurability = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
					}
					// Enable durability value
					if (button.displayString.equals("Disable") && button.id == 4) {
						armorStatus.isShowingDurabilityText = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
					} else if (button.id == 5) {
						armorStatus.isShowingDurabilityText = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
					}
					// Direction
					if (button.displayString.equals("Vertical") && button.id == 6) {
						armorStatus.isVertical = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(7, this.width - 90, 110, 75, 20, "Horizontal"));
					} else if (button.id == 7) {
						armorStatus.isVertical = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(6, this.width - 90, 110, 75, 20, "Vertical"));
					}
					// Alignment
					if (button.displayString.equals("Left") && button.id == 8) {
						armorStatus.isRightAligned = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(9, this.width - 90, 135, 75, 20, "Right"));
					} else if (button.id == 9) {
						armorStatus.isRightAligned = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(8, this.width - 90, 135, 75, 20, "Left"));
					}
					// Item Count
					if (button.displayString.equals("Enable") && button.id == 10) {
						armorStatus.showItemCount = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(11, this.width - 90, 160, 75, 20, "Disable"));
					} else if (button.id == 11) {
						armorStatus.showItemCount = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(10, this.width - 90, 160, 75, 20, "Enable"));
					}
				} else if (renderer instanceof Clock && ModName.equals("Clock")) {
					Clock clock = (Clock) renderer;
					if (button.displayString.equals("Disable") && button.id == 0) {
						clock.setEnabled(false);
						clock.getSettings().hr24 = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
					} else if (button.id == 1) {
						clock.setEnabled(true);
						clock.getSettings().hr24 = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
					}
					// Enable 24 hour format
					if (button.displayString.equals("Disable") && button.id == 2) {
						clock.getSettings().hr24 = false;
						ModInstances.getClockMod().hr24 = false;
						clock.hr24 = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
					} else if (button.id == 3) {
						clock.getSettings().hr24 = true;
						ModInstances.getClockMod().hr24 = true;
						clock.hr24 = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
					}
				} else if (renderer instanceof CPS && ModName.equals("CPS")) {
					CPS cps = (CPS) renderer;
					if (button.displayString.equals("Disable") && button.id == 0) {
						cps.setEnabled(false);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
					} else if (button.id == 1) {
						cps.setEnabled(true);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
					}
					// left
					if (button.displayString.equals("Disable") && button.id == 2) {
						cps.getSettings().left = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
					} else if (button.id == 3) {
						cps.getSettings().left = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
					}
					// right
					if (button.displayString.equals("Disable") && button.id == 4) {
						cps.getSettings().right = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
					} else if (button.id == 5) {
						cps.getSettings().right = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
					}
				} else if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
					ModKeystrokes keystrokes = (ModKeystrokes) renderer;
					if (button.displayString.equals("Disable") && button.id == 0) {
						keystrokes.setEnabled(false);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
					} else if (button.id == 1) {
						keystrokes.setEnabled(true);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
					}
					// Mouse Buttons
					if (button.displayString.equals("Disable") && button.id == 2) {
						keystrokes.getSettings().mouseButtons = false;
						keystrokes.getSettings().setMouseButtons(false);
						keystrokes.reloadMode();
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
					} else if (button.id == 3) {
						keystrokes.getSettings().mouseButtons = true;
						keystrokes.getSettings().setMouseButtons(true);
						keystrokes.reloadMode();
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
					}
					// Spacebar
					if (button.displayString.equals("Disable") && button.id == 4) {
						keystrokes.getSettings().spaceBar = false;
						keystrokes.getSettings().setSpaceBar(false);
						keystrokes.reloadMode();
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
					} else if (button.id == 5) {
						keystrokes.getSettings().spaceBar = true;
						keystrokes.getSettings().setSpaceBar(true);
						keystrokes.reloadMode();
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
					}
					// WASD
					if (button.displayString.equals("Disable") && button.id == 6) {
						keystrokes.getSettings().WASD = false;
						keystrokes.getSettings().setWASD(false);
						keystrokes.reloadMode();
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(7, this.width - 90, 110, 75, 20, "Enable"));
					} else if (button.id == 7) {
						keystrokes.getSettings().setWASD(true);
						keystrokes.getSettings().WASD = true;
						keystrokes.reloadMode();
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(6, this.width - 90, 110, 75, 20, "Disable"));
					}
				} else if (renderer instanceof ModToggleSprintSneak && ModName.equals("Toggle Sprint/Sneak")) {
					ModToggleSprintSneak toggle = (ModToggleSprintSneak) renderer;
					if (button.displayString.equals("Disable") && button.id == 0) {
						toggle.setEnabled(false);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
					} else if (button.id == 1) {
						toggle.setEnabled(true);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
					}
					// Sprint Toggle
					if (button.displayString.equals("Disable") && button.id == 2) {
						toggle.getSettings().sprintToggle = false;
						ModInstances.getToggleSprintSneak().isSprintToggle = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(3, this.width - 90, 60, 75, 20, "Enable"));
					} else if (button.id == 3) {
						toggle.getSettings().sprintToggle = true;
						ModInstances.getToggleSprintSneak().isSprintToggle = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(2, this.width - 90, 60, 75, 20, "Disable"));
					}
					// Sneak Toggle
					if (button.displayString.equals("Disable") && button.id == 4) {
						toggle.getSettings().sneakToggle = false;
						ModInstances.getToggleSprintSneak().isSneakToggle = false;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(5, this.width - 90, 85, 75, 20, "Enable"));
					} else if (button.id == 5) {
						toggle.getSettings().sneakToggle = true;
						ModInstances.getToggleSprintSneak().isSneakToggle = true;
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(4, this.width - 90, 85, 75, 20, "Disable"));
					}
				} else if (renderer instanceof ModFPS && ModName.equals("FPS")) {
					ModFPS fps = (ModFPS) renderer;
					if (button.displayString.equals("Disable") && button.id == 0) {
						fps.setEnabled(false);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(1, this.width - 65, 10, 50, 20, "Enable"));
					} else if (button.id == 1) {
						fps.setEnabled(true);
						this.buttonList.remove(button);
						this.buttonList.add(new GuiButton(0, this.width - 65, 10, 50, 20, "Disable"));
					}
				}
			});
    }
    


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        scrollList.drawScreenNoDirt(mouseX, mouseY, partialTicks);
        drawString(mc.fontRendererObj, ModName, 15, 14, 0xFFFFFF);

        if (hex!=null)hex.drawTextBox();
        if (hex2!=null)hex2.drawTextBox();
        if (hex3!=null)hex3.drawTextBox();
        if (hex4!=null)hex4.drawTextBox();
        md.manager.getRegisteredRenderers().forEach(renderer -> {
			if (renderer instanceof ModArmorStatus && ModName.equals("Armor Status")) {
				ModArmorStatus armorStatus = (ModArmorStatus) renderer;
				drawString(mc.fontRendererObj, "Show the durability bar:", 15, 65, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Show the durability value:", 15, 90, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Direction:", 15, 115, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Alignment:", 15, 140, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Item Count:", 15, 165, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Hex Color:", 15, 190, 0xFFFFFF);
			} else if (renderer instanceof Clock && ModName.equals("Clock")) {
				Clock clock = (Clock) renderer;
				drawString(mc.fontRendererObj, "24 Hour Format:", 15, 65, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Hex Color:", 15, 90, 0xFFFFFF);
			} else if (renderer instanceof CPS && ModName.equals("CPS")) {
				CPS cps = (CPS) renderer;
				drawString(mc.fontRendererObj, "Left", 15, 65, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Right", 15, 90, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Hex Color:", 15, 115, 0xFFFFFF);
			} else if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
				ModKeystrokes keystrokes = (ModKeystrokes) renderer;
				drawString(mc.fontRendererObj, "Mouse Buttons", 15, 65, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Spacebar", 15, 90, 0xFFFFFF);
				drawString(mc.fontRendererObj, "WASD", 15, 115, 0xFFFFFF);
				// colors (normal, pressed, normal background, pressed background)
				drawString(mc.fontRendererObj, "Normal:", 15, 140, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Pressed:", 15, 165, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Normal Background:", 15, 190, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Pressed Background:", 15, 215, 0xFFFFFF);
			} else if (renderer instanceof ModToggleSprintSneak && ModName.equals("Toggle Sprint/Sneak")) {
				ModToggleSprintSneak toggle = (ModToggleSprintSneak) renderer;
				drawString(mc.fontRendererObj, "Sprint Toggle:", 15, 65, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Sneak Toggle:", 15, 90, 0xFFFFFF);
				drawString(mc.fontRendererObj, "Hex Color:", 15, 115, 0xFFFFFF);
			} else if (renderer instanceof ModFPS && ModName.equals("FPS")) {
				ModFPS fps = (ModFPS) renderer;
				drawString(mc.fontRendererObj, "Hex Color:", 15, 65, 0xFFFFFF);
			}
		});
        

        
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(md);
            return;
        }

        // Update text field based on key input
        if (hex.isFocused() && hex != null) {
            hex.textboxKeyTyped(typedChar, keyCode);
            
            // Validate and update color
            String inputColor = hex.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{6}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                // Update armor status color (example)
                md.manager.getRegisteredRenderers().forEach(renderer -> {
                    if (renderer instanceof ModArmorStatus && ModName.equals("Armor Status")) {
                        ModArmorStatus armorStatus = (ModArmorStatus) renderer;
                        armorStatus.getSettings().red = red;
                        armorStatus.getSettings().green = green;
                        armorStatus.getSettings().blue = blue;
                    } else if (renderer instanceof Clock && ModName.equals("Clock")) {
						Clock clock = (Clock) renderer;
						clock.getSettings().red = red;
						clock.getSettings().green = green;
						clock.getSettings().blue = blue;
                    } else if (renderer instanceof CPS && ModName.equals("CPS")) {
						CPS cps = (CPS) renderer;
						cps.getSettings().red = red;
						cps.getSettings().green = green;
						cps.getSettings().blue = blue;
                    } else if (renderer instanceof ModFPS && ModName.equals("FPS")) {
                    	ModFPS fps = (ModFPS) renderer;
                    	fps.getSettings().red = red;
                    	fps.getSettings().green = green;
                    	fps.getSettings().blue = blue;
                    	fps.getSettings().save();
					} else if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
						ModKeystrokes keystrokes = (ModKeystrokes) renderer;
						keystrokes.getSettings().red = red;
						keystrokes.getSettings().green = green;
						keystrokes.getSettings().blue = blue;
						keystrokes.getSettings().setRed(red);
						keystrokes.getSettings().setGreen(green);
						keystrokes.getSettings().setBlue(blue);
					} else if (renderer instanceof ModToggleSprintSneak && ModName.equals("Toggle Sprint/Sneak")) {
						ModToggleSprintSneak toggle = (ModToggleSprintSneak) renderer;
						toggle.getSettings().red = red;
						toggle.getSettings().green = green;
						toggle.getSettings().blue = blue;
						toggle.getSettings().save();
					}
                });
            }
        } else if (hex2.isFocused() && hex2 != null) {
        	hex2.textboxKeyTyped(typedChar, keyCode);
            
            // Validate and update color
            String inputColor = hex2.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{6}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                // Update armor status color (example)
                md.manager.getRegisteredRenderers().forEach(renderer -> {
                    if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
						ModKeystrokes keystrokes = (ModKeystrokes) renderer;
						keystrokes.getSettings().pressedRed = red;
						keystrokes.getSettings().pressedGreen = green;
						keystrokes.getSettings().pressedBlue = blue;
						keystrokes.getSettings().setPressedRed(red);
						keystrokes.getSettings().setPressedGreen(green);
						keystrokes.getSettings().setPressedBlue(blue);
					}
                });
            }
        } else if (hex3.isFocused() && hex3 != null) {
        	hex3.textboxKeyTyped(typedChar, keyCode);
            
            // Validate and update color
            String inputColor = hex3.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{8}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                int alpha = Integer.parseInt(inputColor.substring(7, 9), 16);
                // Update armor status color (example)
                md.manager.getRegisteredRenderers().forEach(renderer -> {
                    if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
						ModKeystrokes keystrokes = (ModKeystrokes) renderer;
						keystrokes.getSettings().keyBackgroundRed = red;
						keystrokes.getSettings().keyBackgroundGreen = green;
						keystrokes.getSettings().keyBackgroundBlue = blue;
						keystrokes.getSettings().keyBackgroundAlpha = alpha;
						keystrokes.getSettings().setKeyBackgroundRed(red);
						keystrokes.getSettings().setKeyBackgroundGreen(green);
						keystrokes.getSettings().setKeyBackgroundBlue(blue);
						keystrokes.getSettings().setKeyBackgroundAlpha(alpha);
					}
                });
            }
        } else if (hex4.isFocused() && hex4 != null) {
        	hex4.textboxKeyTyped(typedChar, keyCode);
            
            // Validate and update color
            String inputColor = hex4.getText();
            if (inputColor.matches("#[0-9A-Fa-f]{6}")) {
                int red = Integer.parseInt(inputColor.substring(1, 3), 16);
                int green = Integer.parseInt(inputColor.substring(3, 5), 16);
                int blue = Integer.parseInt(inputColor.substring(5, 7), 16);
                // Update armor status color (example)
                md.manager.getRegisteredRenderers().forEach(renderer -> {
                    if (renderer instanceof ModKeystrokes && ModName.equals("Keystrokes")) {
						ModKeystrokes keystrokes = (ModKeystrokes) renderer;
						keystrokes.getSettings().keyBackgroundPressedRed = red;
						keystrokes.getSettings().keyBackgroundPressedGreen = green;
						keystrokes.getSettings().keyBackgroundPressedBlue = blue;
						keystrokes.getSettings().setKeyBackgroundPressedRed(red);
						keystrokes.getSettings().setKeyBackgroundPressedGreen(green);
						keystrokes.getSettings().setKeyBackgroundPressedBlue(blue);
					}
                });
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
        if (hex!=null)hex.mouseClicked(mouseX, mouseY, mouseButton);
        if (hex2!=null)hex2.mouseClicked(mouseX, mouseY, mouseButton);
        if (hex3!=null)hex3.mouseClicked(mouseX, mouseY, mouseButton);
        if (hex4!=null)hex4.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private class ScrollList extends GuiSlot {

        public ScrollList() {
            super(ModView.this.mc, ModView.this.width, ModView.this.height, 32, ModView.this.height, 20);
        }

        @Override
        protected int getSize() {
            return entries.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
            System.out.println("Clicked: " + entries.get(index));
        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {}

        @Override
        protected void drawSlot(int index, int x, int y, int height, int mouseX, int mouseY) {

        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}
