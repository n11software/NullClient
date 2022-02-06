package net.minecraft.client.gui;

import n11client.Client;
import n11client.utils.Log;
import n11client.utils.SessionChanger;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiLoginMojang extends GuiScreen {

    private GuiTextField email, password;
    private String emailValue, passwordValue;
    private boolean error = false;

    public void updateScreen() {
        this.email.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(new GuiLogin());
        } else if (this.email.isFocused()) {
            this.email.textboxKeyTyped(typedChar, keyCode);
            this.emailValue = this.email.getText();
        } else if (this.password.isFocused()) {
            this.password.textboxKeyTyped(typedChar, keyCode);
            this.passwordValue = this.password.value;
        }

        if (keyCode == 28 || keyCode == 156) this.actionPerformed((GuiButton)this.buttonList.get(0));

        if (keyCode == 15) {
            if (this.email.isFocused()) {
                this.password.setFocused(true);
                this.email.setFocused(false);
            } else {
                this.password.setFocused(false);
                this.email.setFocused(true);
            }
        }

        ((GuiButton)this.buttonList.get(0)).enabled = this.email.getText().length() > 0 && this.password.getText().length() > 0;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.email.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        this.drawCenteredString(this.fontRendererObj, "Login", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.email.drawTextBox();
        this.drawString(this.fontRendererObj, "Email/Username:", this.width / 2 - 100, this.height/2-25+fontRendererObj.FONT_HEIGHT-20, -6250336);
        this.password.drawTextBox();
        this.drawString(this.fontRendererObj, "Password:", this.width / 2 - 100, this.height/2+30+fontRendererObj.FONT_HEIGHT-30, -6250336);
        if (error) this.drawString(this.fontRendererObj, "Username or Password was incorrect!", this.width / 2 - fontRendererObj.getStringWidth("Username or Password was incorrect!")/2, this.height/2-65, new Color(255, 85, 85, 255).getRGB());
    }

    public void initGui() {
        this.email = new GuiTextField(1, this.mc.fontRendererObj, this.width / 2 - 100, this.height/2-20, 200, 20);
        this.email.setFocused(true);
        this.password = new GuiTextField(2, this.mc.fontRendererObj, this.width / 2 - 100, this.height/2+35+fontRendererObj.FONT_HEIGHT-20, 200, 20, true);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height/2+35+fontRendererObj.FONT_HEIGHT+10, 100, 20, "Login"));
        ((GuiButton)this.buttonList.get(0)).enabled = false;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            error = false;
            if (SessionChanger.getInstance().setUser(emailValue, passwordValue) == 0) this.mc.displayGuiScreen(null);
            else error = true;
        }
    }

}
