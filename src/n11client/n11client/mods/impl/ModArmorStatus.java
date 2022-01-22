package n11client.mods.impl;

import n11client.gui.hud.ScreenPosition;
import n11client.mods.ModDraggable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class ModArmorStatus extends ModDraggable {

    public boolean isShowingDurability = false;
    private ScreenPosition pos = ScreenPosition.fromAbsolute(1216, 640);

    @Override
    public void save(ScreenPosition pos) {
        this.pos = pos;
    }

    @Override
    public ScreenPosition load() {
        return pos;
    }

    @Override
    public int getWidth() {
        return 64;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void render(ScreenPosition pos) {
        int used = 0;
        if (mc.thePlayer.getHeldItem() != null) {
            renderItemStack(pos, 0, mc.thePlayer.getHeldItem());
            used = 1;
        }
        for (int i=0;i<mc.thePlayer.inventory.armorInventory.length;i++) {
            ItemStack item = mc.thePlayer.inventory.armorInventory[i];
            renderItemStack(pos, used, item);
            if (item != null) used++;
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
        GL11.glPushMatrix();
        int yAdd = (-16*i)+64;
        if (isShowingDurability && item.getItem().isDamageable()) font.drawString(item.getMaxDamage()-item.getItemDamage()+"", (pos.getAbsoluteX()+getWidth())-font.getStringWidth(item.getMaxDamage()-item.getItemDamage()+"")-20, pos.getAbsoluteY()+yAdd+5, -1);
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(item, (pos.getAbsoluteX()+getWidth())-16, pos.getAbsoluteY()+yAdd);
        GL11.glPopMatrix();
    }

}
