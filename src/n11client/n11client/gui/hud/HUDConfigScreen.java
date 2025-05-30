package n11client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLoginMojang;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import n11client.Client;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

public class HUDConfigScreen extends GuiScreen {
    private final HashMap<IRenderer, ScreenPosition> renderers = new HashMap<>();
    private Optional<IRenderer> selectedRenderer = Optional.empty();
    
    private HUDManager manager;

    private int prevX, prevY;

    public HUDConfigScreen(HUDManager manager) {
    	this.manager = manager;
        Collection<IRenderer> registeredRenderers = manager.getRegisteredRenderers();
        for (IRenderer renderer: registeredRenderers) {
            if (!renderer.isEnabled()) continue;
            ScreenPosition pos = renderer.load();
            if (pos == null) pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
            adjustBounds(renderer, pos);
            this.renderers.put(renderer, pos);
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();  // Ensure parent GUI is initialized
        this.buttonList.clear();  // Clear previous buttons to avoid duplicates
        final float zBackup = this.zLevel;
        this.zLevel = 201;
        ScaledResolution res = new ScaledResolution(mc);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2, "Settings"));
        this.zLevel = zBackup;
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
        	this.buttonList.remove(0);
        	this.mc.displayGuiScreen(new ModListView(manager));
        	// Add scrollable list of mods
        	
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float particalTicks) {
        super.drawDefaultBackground();
        final float zBackup = this.zLevel;
        this.zLevel = 200;
        for (IRenderer renderer: renderers.keySet()) {
            ScreenPosition pos = renderers.get(renderer);
            renderer.renderDummy(pos);
            this.drawHollowRect(pos.getAbsoluteX(), pos.getAbsoluteY(), renderer.getWidth(), renderer.getHeight(), 0xFF00FFFF);
        }

        ScaledResolution res = new ScaledResolution(mc);
        this.zLevel = zBackup;
        super.drawScreen(mouseX, mouseY, particalTicks);
    }

    private void drawHollowRect(int x, int y, int w, int h, int i) {
        this.drawHorizontalLine(x, x+w, y, i);
        this.drawHorizontalLine(x, x+w, y+h, i);
        this.drawVerticalLine(x, y+h, y, i);
        this.drawVerticalLine(x+w, y+h, y, i);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            renderers.entrySet().forEach((entry) -> {
                entry.getKey().save(entry.getValue());
            });
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        if (selectedRenderer.isPresent()) moveSelectedRenderBy(x-prevX, y-prevY);
        this.prevX = x;
        this.prevY = y;
    }

    private void moveSelectedRenderBy(int x, int y) {
        IRenderer renderer = selectedRenderer.get();
        ScreenPosition pos = renderers.get(renderer);
        pos.setAbsolute(pos.getAbsoluteX() + x, pos.getAbsoluteY() + y);
        adjustBounds(renderer, pos);
    }

    private void adjustBounds(IRenderer renderer, ScreenPosition pos) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        int x = Math.max(0, Math.min(pos.getAbsoluteX(), Math.max(width - renderer.getWidth(), 0)));
        int y = Math.max(0, Math.min(pos.getAbsoluteY(), Math.max(height - renderer.getHeight(), 0)));
        RelativePosition rp = new RelativePosition(x, y);
        pos.setRelativePos(rp);
    }

    @Override
    public void onGuiClosed() {
        for (IRenderer renderer: renderers.keySet()) {
            renderer.save(renderers.get(renderer));
        }
    }

    public boolean doesGuiPauseGame() { return true; }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
    	super.mouseClicked(x, y, button);
        this.prevX = x;
        this.prevY = y;

        loadMouseOver(x, y);
    }

    private void loadMouseOver(int x, int y) {
        this.selectedRenderer = renderers.keySet().stream().filter(new MouseOverFinder(x, y)).findFirst();
    }

    private class MouseOverFinder implements Predicate<IRenderer> {

        private int mouseX, mouseY;

        public MouseOverFinder(int x, int y) {
            this.mouseX = x;
            this.mouseY = y;
        }

        @Override
        public boolean test(IRenderer renderer) {
            ScreenPosition pos = renderers.get(renderer);
            int absoluteX = pos.getAbsoluteX();
            int absoluteY = pos.getAbsoluteY();
            if (mouseX >= absoluteX && mouseX <= absoluteX+renderer.getWidth() && mouseY >= absoluteY && mouseY <= absoluteY+renderer.getHeight()) return true;
            return false;
        }

    }

}
