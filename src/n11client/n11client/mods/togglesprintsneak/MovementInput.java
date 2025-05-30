package n11client.mods.togglesprintsneak;

import n11client.mods.ModInstances;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;

public class MovementInput extends net.minecraft.util.MovementInput {
    private final GameSettings gameSettings;
    private int sneakWasPressed = 0, sprintWasPressed;
    private EntityPlayerSP player;
    private float originalFlySpeed = -1.0F;
    private float boostedFlySpeed = 0;
    private Minecraft mc;

    public MovementInput(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void updatePlayerMoveState() {

        player = mc.thePlayer;
        moveStrafe = 0.0F;
        moveForward = 0.0F;
        if (gameSettings.keyBindForward.isKeyDown()) moveForward++;
        if (gameSettings.keyBindBack.isKeyDown()) moveForward--;
        if (gameSettings.keyBindLeft.isKeyDown()) moveStrafe++;
        if (gameSettings.keyBindRight.isKeyDown()) moveStrafe--;

        jump = gameSettings.keyBindJump.isKeyDown();
        if (ModInstances.getToggleSprintSneak().isEnabled() && ModInstances.getToggleSprintSneak().isSneakToggle) {
            if (gameSettings.keyBindSneak.isKeyDown()) {
                if (sneakWasPressed == 0) {
                    if (sneak) sneakWasPressed=-1;
                    else if (player.isRiding() || player.capabilities.isFlying) sneakWasPressed=ModInstances.getToggleSprintSneak().keyHoldTicks+1;
                    else sneakWasPressed = 1;
                    sneak = !sneak;
                } else if (sneakWasPressed > 0) sneakWasPressed++;
            } else {
                if ((ModInstances.getToggleSprintSneak().keyHoldTicks>0)&&(sneakWasPressed>ModInstances.getToggleSprintSneak().keyHoldTicks)) sneak = false;
                sneakWasPressed = 0;
            }
        } else sneak = gameSettings.keyBindSneak.isKeyDown();

        if (sneak) {
            moveStrafe*=0.3f;
            moveForward*=.3f;
        }


        if (ModInstances.getToggleSprintSneak().isEnabled() && ModInstances.getToggleSprintSneak().isSprintToggle) {
            if (gameSettings.keyBindSprint.isKeyDown()) {
                if (sprintWasPressed == 0) {
                    if (ModInstances.getToggleSprintSneak().sprint) sprintWasPressed = -1;
                    else sprintWasPressed = 1;
                    ModInstances.getToggleSprintSneak().sprint = !ModInstances.getToggleSprintSneak().sprint;
                } else if (sprintWasPressed>0) sprintWasPressed++;
            } else {
                if ((ModInstances.getToggleSprintSneak().keyHoldTicks>0) && (sprintWasPressed>ModInstances.getToggleSprintSneak().keyHoldTicks)) ModInstances.getToggleSprintSneak().sprint = false;
                sprintWasPressed = 0;
            }
        } else ModInstances.getToggleSprintSneak().sprint = false;

        if (ModInstances.getToggleSprintSneak().sprint && moveForward > 0.0F && !player.isUsingItem() && !player.isPotionActive(Potion.blindness)) player.setSprinting(true);
    }

    public String getDisplayText() {
        String displayText = "";
        boolean isFlying=mc.thePlayer.capabilities.isFlying;
        boolean isRiding=mc.thePlayer.isRiding();
        boolean isHoldingSneak = gameSettings.keyBindSneak.isKeyDown();
        boolean isHoldingSprint = gameSettings.keyBindSprint.isKeyDown();
        if (isFlying) {
            if (originalFlySpeed>0.0F) displayText += "[Flying (Boosted)] ";
            else displayText += "[Flying] ";
        }
        if (ModInstances.getToggleSprintSneak().isSneakToggle&&sneak&&isHoldingSneak) {
            displayText+="[Sneaking (Held)] ";
        } else if (ModInstances.getToggleSprintSneak().isSneakToggle&&sneak) {
            displayText+="[Sneaking (Toggled)] ";
        } else if (ModInstances.getToggleSprintSneak().sprint && !isRiding && !isFlying) {
            if (isHoldingSprint) displayText += "[Sprinting (Held)] ";
            else displayText += "[Sprinting (Toggled)] ";
        }
        return displayText.trim();
    }
}