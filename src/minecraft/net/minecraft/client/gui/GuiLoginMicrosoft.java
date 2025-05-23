package net.minecraft.client.gui;

import com.microsoft.playwright.*;
import n11client.utils.Log;
import n11client.utils.SessionChanger;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiLoginMicrosoft extends GuiScreen {

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

        if (this.email.getText().length() > 0 && this.password.getText().length() > 0 && keyCode == Keyboard.KEY_RETURN) {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }
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
            ((GuiButton)this.buttonList.get(0)).enabled = false;
            ((GuiButton)this.buttonList.get(0)).displayString = "Logging in...";
            new Thread(() -> {
                Playwright playwright = null;
                try {
                    playwright = Playwright.create();
                    Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
                    BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                            .setRecordHarPath(null) // disables HAR
                            .setRecordVideoDir(null) // disables video
                    );
                    Page page = context.newPage();

                    page.navigate("https://login.live.com/oauth20_authorize.srf?client_id=6c5ec31a-2630-458e-bf5e-eca4db7f1f5d&response_type=code&redirect_uri=http://localhost:25564/&scope=XboxLive.signin%20offline_access&prompt=login");

                    page.waitForSelector("#usernameEntry");
                    page.fill("#usernameEntry", this.emailValue);

                    page.click("#view > div > div.___1hpzav7.f22iagw.f1vx9l62.f1noh358.fyym0d2 > button");

                    try {
                        page.waitForSelector("#field-8__validationMessage > span.fui-Field__validationMessageIcon.ra7h1uk.___9491ub0.f1hcrxcs > svg", new Page.WaitForSelectorOptions().setTimeout(2000));
                    } catch (Exception e) {

                    }
                    if (page.isVisible("#field-8__validationMessage > span.fui-Field__validationMessageIcon.ra7h1uk.___9491ub0.f1hcrxcs > svg")) {
                        browser.close();
                        playwright.close();
                        error = true;
                        ((GuiButton)this.buttonList.get(0)).enabled = true;
                        ((GuiButton)this.buttonList.get(0)).displayString = "Login";
                    } else {
                        page.waitForSelector("#passwordEntry");
                        page.fill("#passwordEntry", this.passwordValue);
                        page.click("#view > div > div.___1hpzav7.f22iagw.f1vx9l62.f1noh358.fyym0d2 > button");
                    }

                    try {
                        page.waitForSelector("#field-8__validationMessage", new Page.WaitForSelectorOptions().setTimeout(2000));
                    } catch (Exception e) {

                    }

                    if (page.isVisible("#field-8__validationMessage")) {
                        browser.close();
                        playwright.close();
                        error = true;
                        ((GuiButton)this.buttonList.get(0)).enabled = true;
                        ((GuiButton)this.buttonList.get(0)).displayString = "Login";
                    }

                    try {
                        page.waitForSelector("#iLandingViewAction", new Page.WaitForSelectorOptions().setTimeout(2000));
                    } catch (Exception e) {

                    }
                    if (page.isVisible("#iLandingViewAction")) {
                        page.click("#iLandingViewAction");
                    }

                    try {
                        page.waitForSelector("#view > div > div.___1hpzav7.f22iagw.f1vx9l62.f1noh358.fyym0d2 > button.fui-Button.r1alrhcs.___1me6uh6.f14es27b.f5b47ha.fhx4nu.fjodcmx.f1kwiid1.f4db1ww.ft85np5.fkhj508.fl43uef.f1i3iumi.f1b6alqh.fk6fouc.f22iagw.f4d9j23.f6dzj5z", new Page.WaitForSelectorOptions().setTimeout(2000));
                    } catch (Exception e) {
                        Log.log("I do not want to stay signed in!");
                    }
                    if (page.isVisible("#view > div > div.___1hpzav7.f22iagw.f1vx9l62.f1noh358.fyym0d2 > button.fui-Button.r1alrhcs.___1me6uh6.f14es27b.f5b47ha.fhx4nu.fjodcmx.f1kwiid1.f4db1ww.ft85np5.fkhj508.fl43uef.f1i3iumi.f1b6alqh.fk6fouc.f22iagw.f4d9j23.f6dzj5z")) {
                        page.click("#view > div > div.___1hpzav7.f22iagw.f1vx9l62.f1noh358.fyym0d2 > button.fui-Button.r1alrhcs.___1me6uh6.f14es27b.f5b47ha.fhx4nu.fjodcmx.f1kwiid1.f4db1ww.ft85np5.fkhj508.fl43uef.f1i3iumi.f1b6alqh.fk6fouc.f22iagw.f4d9j23.f6dzj5z");
                    }

                    try {
                        page.waitForSelector("#pageContent > div > form > div.___1cj7yg8.f183mx53.f1turhiw.f1rmqj0e > div > div > div > div:nth-child(2) > button", new Page.WaitForSelectorOptions().setTimeout(2000));
                    } catch (Exception e) {
                    }
                    if (page.isVisible("#pageContent > div > form > div.___1cj7yg8.f183mx53.f1turhiw.f1rmqj0e > div > div > div > div:nth-child(2) > button")) {
                        page.click("#pageContent > div > form > div.___1cj7yg8.f183mx53.f1turhiw.f1rmqj0e > div > div > div > div:nth-child(2) > button");
                    }

                    String url = page.url();
                    if (url.contains("success")) {
                        browser.close();
                        playwright.close();
                    }

                    try {
                        page.waitForSelector("body > pre", new Page.WaitForSelectorOptions().setTimeout(2000));
                    } catch (Exception e) {
                        Log.log("Logged in!");
                    }
                    if (page.isVisible("body > pre")) {
                        error = true;
                        ((GuiButton)this.buttonList.get(0)).enabled = true;
                        ((GuiButton)this.buttonList.get(0)).displayString = "Login";
                        return;
                    }

                    // Now close browser and playwright after user input
//                browser.close();
//                playwright.close();

                } catch (Exception e) {

                }
            }).start();
        }
    }

}
