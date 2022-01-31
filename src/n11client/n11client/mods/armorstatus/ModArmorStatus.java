package n11client.mods.armorstatus;

import n11client.gui.hud.RelativePosition;
import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import n11client.mods.clock.ClockSettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.io.File;

public class ModArmorStatus extends ModDraggable {

    private final ArmorStatusSettings config = new ArmorStatusSettings(this, new File("N11"));

    private ScreenPosition pos = ScreenPosition.fromRelative(config.pos);

    public ArmorStatusSettings getSettings() {
        return config;
    }

    public RelativePosition getPos() { return pos.getRelativePos(); }

    public boolean isShowingDurability = config.durability;
    public boolean isShowingDurabilityText = config.durabilityText;
    public boolean isVertical = config.vertical;
    public boolean isRightAligned = config.rightAligned;
    public boolean showItemCount = config.itemCount;

    @Override
    public void save(ScreenPosition pos) {
        this.pos = pos;
    }

    public void ResizeEvent() {
        pos.setRelativePos(new RelativePosition(pos.getRelativePos().getSector(), pos.getRelativePos().getX(), pos.getRelativePos().getY()));
    }

    @Override
    public ScreenPosition load() {
        return pos;
    }

    @Override
    public int getWidth() { return !isVertical ? 80 : isShowingDurabilityText ? 64 : 16; }

    @Override
    public int getHeight() { return isVertical ? 80 : 16; }

    @Override
    public void render(ScreenPosition pos) {
        int position = !isRightAligned&&!isVertical ? 4 : 0;
        if (mc.thePlayer.getHeldItem() != null) {
            renderItemStack(pos, position, mc.thePlayer.getHeldItem());
            if (!isRightAligned&&!isVertical) position--;
            else position++;
        }
        for (int i=0;i<mc.thePlayer.inventory.armorInventory.length;i++) {
            ItemStack item = mc.thePlayer.inventory.armorInventory[i];
            renderItemStack(pos, position, item);
            if (item!=null) {
                if (!isRightAligned&&!isVertical) position--;
                else position++;
            }
        }
    }

    @Override
    public void renderDummy(ScreenPosition pos) {
        renderItemStack(pos, 4, new ItemStack(Items.iron_helmet));
        renderItemStack(pos, 3, new ItemStack(Items.iron_chestplate));
        renderItemStack(pos, 2, new ItemStack(Items.iron_leggings));
        renderItemStack(pos, 1, new ItemStack(Items.iron_boots));
        renderItemStack(pos, 0, new ItemStack(Items.iron_sword));
    }

    private void renderItemStack(ScreenPosition pos, int i, ItemStack item) {
        if (item == null) return;
        int off = (-16*i)+64;
        if ((isShowingDurabilityText && isShowingDurability && isVertical) && item.getItem().isDamageable()) font.drawStringWithShadow(item.getMaxDamage()-item.getItemDamage()+"", isRightAligned ? (pos.getAbsoluteX()+getWidth())-font.getStringWidth(item.getMaxDamage()-item.getItemDamage()+"")-20 : pos.getAbsoluteX()+20, pos.getAbsoluteY()+off+5, new Color(config.red, config.green, config.blue, 255).getRGB());
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        float zLevel;
        zLevel = 200.0F;
        mc.getRenderItem().zLevel = 200.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, isVertical ? isRightAligned ? (pos.getAbsoluteX()+getWidth())-16 : (pos.getAbsoluteX()) : pos.getAbsoluteX()+off, isVertical ? pos.getAbsoluteY()+off : (pos.getAbsoluteY()+getHeight()-16));
        if (isShowingDurability && showItemCount) mc.getRenderItem().renderItemOverlayIntoGUI(font, item, isVertical ? isRightAligned ? (pos.getAbsoluteX()+getWidth())-16 : (pos.getAbsoluteX()) : pos.getAbsoluteX()+off, isVertical ? (pos.getAbsoluteY()+off) : (pos.getAbsoluteY()+getHeight()-16), (String)null);
        else if (isShowingDurability) mc.getRenderItem().renderItemOverlayIntoGUINoCount(font, item, isVertical ? isRightAligned ? (pos.getAbsoluteX()+getWidth())-16 : (pos.getAbsoluteX()) : pos.getAbsoluteX()+off, isVertical ? pos.getAbsoluteY()+off : (pos.getAbsoluteY()+getHeight()-16), (String)null);
        else if (showItemCount) mc.getRenderItem().renderItemOverlayIntoGUINoDurability(font, item, isVertical ? isRightAligned ? (pos.getAbsoluteX()+getWidth())-16 : (pos.getAbsoluteX()) : pos.getAbsoluteX()+off, isVertical ? pos.getAbsoluteY()+off : (pos.getAbsoluteY()+getHeight()-16), (String)null);
        zLevel = 0.0F;
        mc.getRenderItem().zLevel = 0.0F;
    }

}
