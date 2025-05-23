package net.minecraft.client.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import n11client.Client;
import n11client.utils.Log;
import n11client.utils.SessionChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class GuiLogin extends GuiScreen {

    public Map<String, String> sessions = new TreeMap<>();
    public Map<String, String> usernames = new TreeMap<>();
    private Map<String, ResourceLocation> cachedSkins = new TreeMap<>();

    private ScrollList scrollList;

    public void initGui() {
        Client.getInstance().loginGUI = this;
        File tokenFile = new File(Minecraft.getMinecraft().mcDataDir, "n11client_token.json");

        if (tokenFile.exists()) {
            String json = "";
            try {
                json = new String(java.nio.file.Files.readAllBytes(tokenFile.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(json).getAsJsonObject();

            JsonObject sessionsJson = obj.getAsJsonObject("sessions");
            if (sessionsJson == null || sessionsJson.entrySet().isEmpty()) {
                Log.error("No sessions found in token file");
                Client.loginMicrosoft();
                this.mc.displayGuiScreen(new GuiLoginMicrosoft());
                return;
            }
            for (Map.Entry<String, JsonElement> entry : sessionsJson.entrySet()) {
                String session = entry.getKey();
                JsonObject value = entry.getValue().getAsJsonObject();
                String uuid = value.get("uuid").getAsString().replace("-", "");
                String username = value.get("username").getAsString();
                this.sessions.put(session, uuid);
                this.usernames.put(uuid, username);
            }
        } else {
            Log.error("Token file not found");
            Client.loginMicrosoft();
            this.mc.displayGuiScreen(new GuiLoginMicrosoft());
            return;
        }

        this.buttonList.add(new GuiButton(0, this.width/2-100, this.height-50, "Add a new account"));
        this.buttonList.add(new GuiButton(1, this.width/2-100, this.height-25, "Remove"));
        this.buttonList.get(1).enabled = false;
        this.scrollList = new ScrollList(this.mc, this.width, this.height, 32, this.height - 65 + 4, 20);
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            Client.getInstance().loginGUI = this;
            Client.loginMicrosoft();
            this.mc.displayGuiScreen(new GuiLoginMicrosoft());
        } else if (button.id == 1) {
            if (this.scrollList.selectedElement != -1) {
                String sessionKey = (String) sessions.keySet().toArray()[this.scrollList.selectedElement];
                String uuid = sessions.get(sessionKey);
                sessions.remove(sessionKey);
                usernames.remove(uuid);
                this.scrollList.selectedElement = -1;
                this.buttonList.get(1).enabled = false;
                // save the new sessions to the file
                File tokenFile = new File(Minecraft.getMinecraft().mcDataDir, "n11client_token.json");
                if (tokenFile.exists()) {
                    String json = "";
                    try {
                        json = new String(java.nio.file.Files.readAllBytes(tokenFile.toPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(json).getAsJsonObject();

                    JsonObject sessionsJson = obj.getAsJsonObject("sessions");
                    if (sessionsJson != null) {
                        sessionsJson.remove(sessionKey);
                        obj.add("sessions", sessionsJson);
                        java.nio.file.Files.write(tokenFile.toPath(), obj.toString().getBytes());
                    }
                }
            }
        } else {
            super.actionPerformed(button);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Users", this.width / 2, 20, 0xFFFFFF);
        this.scrollList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollList.handleMouseInput();
    }

    public void MSCB() {
        Minecraft.getMinecraft().addScheduledTask(() -> mc.displayGuiScreen(new GuiMainMenu()));
    }

    class ScrollList extends GuiSlot {
        public ScrollList(Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
            super(mc, width, height, top, bottom, slotHeight);
        }

        @Override
        protected int getSize() {
            return sessions.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
            this.selectedElement = index;
            if (!doubleClick) buttonList.get(1).enabled = true;
            else {
                Log.log("Clicked on " + sessions.get(index+""));
                // open json file and load session
                File tokenFile = new File(Minecraft.getMinecraft().mcDataDir, "n11client_token.json");
                if (tokenFile.exists()) {
                    String json = "";
                    try {
                        json = new String(java.nio.file.Files.readAllBytes(tokenFile.toPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JsonParser parser = new JsonParser();
                    JsonObject obj = parser.parse(json).getAsJsonObject();

                    JsonObject sessionsJson = obj.getAsJsonObject("sessions");
                    if (sessionsJson != null) {
                        String sessionKey = (String) sessions.keySet().toArray()[index];
                        JsonObject value = sessionsJson.getAsJsonObject(sessionKey);
                        String uuid = value.get("uuid").getAsString().replace("-", "");
                        String username = value.get("username").getAsString();
                        SessionChanger.getInstance().setUser(username, value.get("accessToken").getAsString(), uuid);
                        Client.getInstance().loginGUI.MSCB();
                        // set the session as active
                        obj.addProperty("active", sessionKey);
                        // save the new sessions to the file
                        try {
                            java.nio.file.Files.write(tokenFile.toPath(), obj.toString().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        @Override
        protected boolean isSelected(int index) {
            return index == selectedElement;
        }

        @Override
        protected void drawBackground() {
            GuiLogin.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int slotIndex, int x, int y, int height, int mouseX, int mouseY) {
            String sessionKey = (String) sessions.keySet().toArray()[slotIndex];
            String uuid = sessions.get(sessionKey);
            String username = usernames.get(uuid);
            String display = username;

            int headSize = 16;
            int spacing = 4;
            int textWidth = GuiLogin.this.fontRendererObj.getStringWidth(display);
            int totalWidth = headSize + spacing + textWidth;

            int slotCenterX = GuiLogin.this.width / 2;
            int startX = slotCenterX - totalWidth / 2;
            int verticalOffset = (height - headSize) / 2;
            drawPlayerHead(uuid, startX, y + verticalOffset, headSize);
            GuiLogin.this.fontRendererObj.drawString(display, startX + headSize + spacing, y + verticalOffset + 4, 0xFFFFFF);

            boolean isOtherSelected = false;
            for (int i = 0; i < sessions.size(); i++) {
                if (i != slotIndex && isSelected(i)) {
                    isOtherSelected = true;
                    break;
                }
            }

            if (username.equals(Minecraft.getMinecraft().getSession().getUsername()) && !isOtherSelected) {
                this.selectedElement = slotIndex;
            }
        }

        private void drawPlayerHead(String uuid, int x, int y, int size) {
            ResourceLocation loc = cachedSkins.get(uuid);

            if (loc == null) {
                try {
                    URL url = new URL("https://crafatar.com/avatars/" + uuid + "?overlay");

                    BufferedImage image = ImageIO.read(url);
                    if (image != null) {
                        DynamicTexture dynamicTexture = new DynamicTexture(image);
                        loc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("head_" + uuid, dynamicTexture);
                        cachedSkins.put(uuid, loc);
                    } else {
                        loc = AbstractClientPlayer.getLocationSkin("Steve");
                    }
                } catch (IOException e) {
                    loc = AbstractClientPlayer.getLocationSkin("Steve");
                }
            }

            Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
            GlStateManager.color(1F, 1F, 1F, 1F);
            drawModalRectWithCustomSizedTexture(x, y, 0, 0, size, size, size, size);
        }

    }
}
